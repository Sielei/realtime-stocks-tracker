package com.hs.ktsp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.hs")
public class KafkaToSSEApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaToSSEApplication.class, args);
    }
}
