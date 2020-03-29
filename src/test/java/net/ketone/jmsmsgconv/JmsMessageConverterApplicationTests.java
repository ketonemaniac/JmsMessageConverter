package net.ketone.jmsmsgconv;

import lombok.extern.slf4j.Slf4j;
import net.ketone.jmsmsgconv.entities.FlightSchedule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(initializers = {
		JmsMessageConverterApplicationTests.Initializer.class
})
@Testcontainers
@Slf4j
class JmsMessageConverterApplicationTests {

	@Autowired
	private WebTestClient webTestClient;
	@Autowired
	private WebClient senderWebClient;

	@LocalServerPort
	private int localServerPort;

    @Container
	public static GenericContainer<?> rabbitMQContainer = new GenericContainer("rabbitmq:3.7.18-management")
			.withExposedPorts(5672)
			.withExposedPorts(15672)
			.waitingFor(Wait.forLogMessage(".*Server startup complete.*", 1));

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            List<String> pairs = new ArrayList<>();
            // rabbitmq
            pairs.add("spring.rabbitmq.port=" + rabbitMQContainer.getMappedPort(5672));
			log.info("Exposing port 15672=" + rabbitMQContainer.getMappedPort(15672));
            TestPropertyValues.of(pairs).applyTo(configurableApplicationContext.getEnvironment());

		}
	}


	@Test
	void contextLoads() throws InterruptedException {
		FluxExchangeResult<FlightSchedule> result =webTestClient
				.mutate()
				.responseTimeout(Duration.ofMillis(10000))
				.build()
				.get().uri("events")
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_STREAM_JSON) //
				.returnResult(FlightSchedule.class);

		StepVerifier.create(result.getResponseBody()) //
				.expectNextMatches(s -> Arrays.asList("CX101", "QF123","MH79").contains(s.getFlightNo()))
				.thenCancel()
				.verify();
		;
	}
}
