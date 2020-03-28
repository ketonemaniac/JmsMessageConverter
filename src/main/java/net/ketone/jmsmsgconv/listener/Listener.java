package net.ketone.jmsmsgconv.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.ketone.jmsmsgconv.entities.FlightSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class Listener {

    @Autowired
    private WebClient senderWebClient;

    //    @JmsListener(destination = "rabbit-trader-channel")
    public void handleMessage(FlightSchedule flightSchedule) throws JsonProcessingException {
        log.info("Received " + flightSchedule);

        senderWebClient.post().uri("accept").body(
                BodyInserters.fromValue(flightSchedule)
        )
        .retrieve()
        .bodyToMono(String.class)
                .doOnSuccess(str -> log.info("sent: " + str))
        .subscribe();


    }
}
