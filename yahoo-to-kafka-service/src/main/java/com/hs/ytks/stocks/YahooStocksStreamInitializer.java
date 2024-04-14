package com.hs.ytks.stocks;

import com.hs.kafka.producer.KafkaStreamInitializer;
import com.hs.app.config.KafkaConfigData;
import com.hs.kafka.admin.KafkaAdminClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class YahooStocksStreamInitializer implements KafkaStreamInitializer {
    private static final Logger LOG = LoggerFactory.getLogger(YahooStocksStreamInitializer.class);
    private final KafkaConfigData kafkaConfigData;
    private final KafkaAdminClient kafkaAdminClient;

    public YahooStocksStreamInitializer(KafkaConfigData kafkaConfigData, KafkaAdminClient kafkaAdminClient) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaAdminClient = kafkaAdminClient;
    }

    @Override
    public void init() {
        kafkaAdminClient.createKafkaTopicsWithRetry();
        kafkaAdminClient.checkSchemaRegistry();
        LOG.info("Topics with name {} is ready for operations!", kafkaConfigData.getTopicNamesToCreate().toArray());
    }
}
