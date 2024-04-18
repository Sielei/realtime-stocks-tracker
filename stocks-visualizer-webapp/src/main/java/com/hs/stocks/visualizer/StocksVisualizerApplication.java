package com.hs.stocks.visualizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.hs")
public class StocksVisualizerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StocksVisualizerApplication.class, args);
    }
}
