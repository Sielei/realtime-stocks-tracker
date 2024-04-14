package com.hs.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "yahoo-to-kafka-service")
@Data
public class YahooToKafkaConfigData {
    private List<String> stockSymbols;
}
