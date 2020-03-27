package net.ketone.jmsmsgconv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JmsMessageConverterApplication {

	public static void main(String[] args) {
		SpringApplication.run(JmsMessageConverterApplication.class, args);
	}

}
