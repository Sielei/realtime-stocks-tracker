package com.hs.ytks.stocks;

import com.hs.kafka.producer.KafkaStreamInitializer;
import com.hs.kafka.producer.KafkaStreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.hs")
public class YahooToKafkaServiceApplication implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(YahooToKafkaServiceApplication.class);
    private final KafkaStreamRunner streamRunner;
    private final KafkaStreamInitializer streamInitializer;

    public YahooToKafkaServiceApplication(KafkaStreamRunner streamRunner, KafkaStreamInitializer streamInitializer) {
        this.streamRunner = streamRunner;
        this.streamInitializer = streamInitializer;
    }

    public static void main(String[] args) {
        SpringApplication.run(YahooToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Starting Yahoo to Kafka Service....");
        streamInitializer.init();
        streamRunner.start();
    }
}
