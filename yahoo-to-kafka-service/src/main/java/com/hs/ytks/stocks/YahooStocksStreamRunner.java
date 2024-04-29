package com.hs.ytks.stocks;

import com.hs.app.config.YahooToKafkaConfigData;
import com.hs.kafka.avro.model.StockPrice;
import com.hs.kafka.producer.KafkaProducer;
import com.hs.kafka.producer.KafkaStreamRunner;
import com.hs.app.config.KafkaConfigData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.Executors;

@Component
public class YahooStocksStreamRunner implements KafkaStreamRunner {
    private static final Logger LOG = LoggerFactory.getLogger(YahooStocksStreamRunner.class);
    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducer<UUID, StockPrice> kafkaProducer;
    private final YahooToKafkaConfigData yahooToKafkaConfigData;
    private final YahooFinanceQuery query;

    public YahooStocksStreamRunner(KafkaConfigData kafkaConfigData, KafkaProducer<UUID, StockPrice> kafkaProducer, YahooToKafkaConfigData yahooToKafkaConfigData, YahooFinanceQuery query) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaProducer = kafkaProducer;
        this.yahooToKafkaConfigData = yahooToKafkaConfigData;
        this.query = query;
    }

    @Override
    public void start() {
        var tickers = yahooToKafkaConfigData.getStockSymbols();
        var executor = Executors.newSingleThreadExecutor();
        while (true){
            for (var ticker: tickers){
                executor.execute(() ->{
                    var stockPrice = query.getStockFromChart(ticker);
                    LOG.info("Publishing stock price for: {}", stockPrice.getSymbol());
                    kafkaProducer.send(kafkaConfigData.getTopicName(), UUID.randomUUID(), stockPrice);
                });
            }
        }
    }
}