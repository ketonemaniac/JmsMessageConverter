package net.ketone.jmsmsgconv.config;

import com.fasterxml.jackson.xml.XmlMapper;
import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.rabbitmq.jms.client.message.RMQObjectMessage;
import lombok.extern.slf4j.Slf4j;
import net.ketone.jmsmsgconv.entities.FlightSchedule;
import net.ketone.jmsmsgconv.listener.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.IOException;

@Configuration
@EnableJms
@Slf4j
public class JmsConfiguration implements JmsListenerConfigurer {

    @Value("${queueName}")
    private String queueName;
    @Autowired
    private Listener listener;

    private final XmlMapper xmlMapper = new XmlMapper();

    /**
     * For one single consumer
     * @param connectionFactory
     * @return
     */
//    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory
                = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        return factory;
    }

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar jmsListenerEndpointRegistrar) {
        MessageListenerAdapter messageListener = new MessageListenerAdapter(listener);

        /**
         * This is JMS 1.1 compliant only.
         */
        messageListener.setMessageConverter(new MessageConverter() {
            @Override
            public Message toMessage(Object o, Session session) throws JMSException, MessageConversionException {
                throw new RuntimeException();
            }

            @Override
            public Object fromMessage(Message message) throws JMSException, MessageConversionException {
                FlightSchedule schedule = FlightSchedule.builder().build();
                String body = ((RMQObjectMessage) message).getObject().toString();
                try {
                    schedule = xmlMapper.readValue(body, FlightSchedule.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                schedule.setTimestamp(message.getJMSTimestamp());
                return schedule;
            }
        });

        SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
        endpoint.setId(queueName+"Endpoint");
        endpoint.setDestination(queueName);
        endpoint.setMessageListener(messageListener);

        jmsListenerEndpointRegistrar.registerEndpoint(endpoint,jmsListenerContainerFactory(connectionFactory()));


    }

    /**
     * This is for RabbitMQ. Replace this by whatever equivalent MQ Factory
     * @return
     */
    @Bean
    ConnectionFactory connectionFactory() {
        RMQConnectionFactory factory = new RMQConnectionFactory();
        return factory;
    }

}
