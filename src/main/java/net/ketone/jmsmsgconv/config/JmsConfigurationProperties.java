package net.ketone.jmsmsgconv.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app.jms")
@Data
public class JmsConfigurationProperties {

    private List<Endpoint> endpoints;

    @Data
    static class Endpoint {

        private String queueName;

    }

}
