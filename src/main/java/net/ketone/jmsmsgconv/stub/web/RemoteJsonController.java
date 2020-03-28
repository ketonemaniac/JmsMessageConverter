package net.ketone.jmsmsgconv.stub.web;

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

    private DirectProcessor<String> processor = DirectProcessor.create();

    private FluxSink<String> sink;

    @PostConstruct
    public void init() {
        sink = processor.sink();
    }


    @GetMapping("ping")
    public Mono<String> ping() {
        sink.next("hi");
        return Mono.just("hi");
    }


    @GetMapping("events")
    public Flux<String> events() {
        return processor;
    }


    @PostMapping("accept")
    public Mono<String> accept(@RequestBody String flightInfo) {
        sink.next(flightInfo);
        return Mono.just(flightInfo);
    }
}
