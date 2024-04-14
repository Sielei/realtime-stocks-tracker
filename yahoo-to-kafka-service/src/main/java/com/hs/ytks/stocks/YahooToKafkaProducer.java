package com.hs.ytks.stocks;

import com.hs.kafka.avro.model.StockPrice;
import com.hs.kafka.producer.KafkaProducer;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
class YahooToKafkaProducer implements KafkaProducer<UUID, StockPrice> {
    private static final Logger LOG = LoggerFactory.getLogger(YahooToKafkaProducer.class);
    private final KafkaTemplate<UUID, StockPrice> kafkaTemplate;

    YahooToKafkaProducer(KafkaTemplate<UUID, StockPrice> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topicName, UUID key, StockPrice message) {
        LOG.info("Sending message='{}' to topic='{}'", message, topicName);
        var kafkaResultFuture = kafkaTemplate.send(topicName, key, message);
        kafkaResultFuture.whenComplete(((uuidStockPriceSendResult, throwable) -> {
            //handle success
            if (throwable == null){
                var metadata = uuidStockPriceSendResult.getRecordMetadata();
                LOG.info("Received new metadata. Topic: {}; Partition {}; Offset {}; Timestamp {}, " +
                                "at time {}",
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp(),
                        System.nanoTime());
            }
            // handle error
            else {
                LOG.error("Error while sending message {} to topic {}", message.toString(),
                        topicName, throwable);
            }
        }));
    }

    @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            LOG.info("Closing kafka producer!");
            kafkaTemplate.destroy();
        }
    }
}
