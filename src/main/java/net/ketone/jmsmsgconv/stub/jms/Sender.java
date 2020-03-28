package net.ketone.jmsmsgconv.stub.jms;

import com.fasterxml.jackson.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import net.ketone.jmsmsgconv.entities.FlightSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class Sender {

    private final XmlMapper xmlMapper = new XmlMapper();

    private List<String> flights = new ArrayList<String>();

    {
        flights.add("CX101");
        flights.add("QF123");
        flights.add("MH79");
    }

    @Autowired
    JmsTemplate jmsTemplate;


    @Scheduled(fixedRate = 5000L) // every 5 seconds
    public void publish() throws IOException {

        Collections.shuffle(flights);
        final FlightSchedule flightSchedule = FlightSchedule.builder().flightNo(flights.get(0)).build();

        log.info("New Flight..." + xmlMapper.writeValueAsString(flightSchedule));

        MessageCreator messageCreator = new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    return session.createObjectMessage(xmlMapper.writeValueAsString(flightSchedule));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        jmsTemplate.send("rabbit-trader-channel", messageCreator);
    }

}
