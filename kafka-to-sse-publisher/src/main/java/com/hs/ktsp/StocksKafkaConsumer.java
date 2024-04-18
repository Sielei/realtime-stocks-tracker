package com.hs.ktsp;

import com.hs.app.config.KafkaConfigData;
import com.hs.kafka.admin.KafkaAdminClient;
import com.hs.kafka.avro.model.StockPrice;
import com.hs.kafka.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;


@Service
public class StocksKafkaConsumer implements KafkaConsumer<StockPrice> {
    private static final Logger LOG = LoggerFactory.getLogger(StocksKafkaConsumer.class);
    private final List<Consumer<StocksQuote>> listeners = new CopyOnWriteArrayList<>();
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final KafkaAdminClient kafkaAdminClient;
    private final KafkaConfigData kafkaConfigData;

    public StocksKafkaConsumer(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry, KafkaAdminClient kafkaAdminClient, KafkaConfigData kafkaConfigData) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConfigData = kafkaConfigData;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void onAppStarted(ApplicationStartedEvent event) {
        kafkaAdminClient.checkTopicsCreated();
        LOG.info("Topics with name {} is ready for operations!", kafkaConfigData.getTopicNamesToCreate().toArray());
        kafkaListenerEndpointRegistry.getListenerContainer("stocksListener").start();
    }

    @Override
    @KafkaListener(id = "stocksListener", topics = "${kafka-config.topic-name}")
    public void receive(StockPrice message) {
        //LOG.info("Messages : {}", message);

        publish(new StocksQuote(message.getSymbol(), message.getExchange(), message.getTradeValue(),
                message.getCurrency(), message.getTradeTime()));
    }

    public void subscribe(Consumer<StocksQuote> listener){
        LOG.info("consumer before: {}", listeners);
        listeners.add(listener);
        LOG.info("New one added, total consumer: {}", listeners.size());
    }

    public void publish(StocksQuote stockQuote) {
        //LOG.info("Processing live stock price: {}", stockPrice);
        listeners.forEach(listener -> listener.accept(stockQuote));
    }
    record StocksQuote(String symbol, String exchange, double tradeValue, String currency, Instant tradeTime){}
}
