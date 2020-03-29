package net.ketone.jmsmsgconv.stub.web;

import net.ketone.jmsmsgconv.entities.FlightSchedule;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@RestController
public class RemoteJsonController {

    private DirectProcessor<FlightSchedule> processor = DirectProcessor.create();

    private FluxSink<FlightSchedule> sink;

    @PostConstruct
    public void init() {
        sink = processor.sink();
    }
    
    @GetMapping(value = "events", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<FlightSchedule> events() {
        return processor;
    }


    @PostMapping("accept")
    public Mono<FlightSchedule> accept(@RequestBody FlightSchedule flightInfo) {
        sink.next(flightInfo);
        return Mono.just(flightInfo);
    }
}
