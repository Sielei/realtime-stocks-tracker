package com.hs.kafka.admin;

import com.hs.app.config.KafkaConfigData;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@EnableRetry
@Configuration
public class KafkaAdminConfig {
    private final KafkaConfigData kafkaConfigData;

    public KafkaAdminConfig(KafkaConfigData kafkaConfigData) {
        this.kafkaConfigData = kafkaConfigData;
    }

    @Bean
    AdminClient adminClient(){
        return AdminClient.create(Map.of(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,
                kafkaConfigData.getBootstrapServers()));
    }

    @Bean
    WebClient webClient(){
        return WebClient.builder().build();
    }
}
