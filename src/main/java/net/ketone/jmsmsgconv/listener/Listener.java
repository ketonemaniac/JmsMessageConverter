package net.ketone.jmsmsgconv.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Listener {

    @JmsListener(destination = "rabbit-trader-channel")
    public void SampleJmsListenerMethod(Message<String> order) {
        log.info("Received " + order.getPayload());
    }
}
