package com.hs.ytks.stocks;

import com.hs.kafka.avro.model.StockPrice;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@Component
class YahooFinanceQuery {
    private final WebClient webClient;

    YahooFinanceQuery(WebClient webClient) {
        this.webClient = webClient;
    }

    public StockPrice getStockFromChart(String symbol){
        var response = webClient.get()
                .uri("https://query1.finance.yahoo.com/v7/finance/chart/" + symbol)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return getStockPriceFromChartJson(((LinkedHashMap<Object, Object>) response));
    }

    private StockPrice getStockPriceFromChartJson(LinkedHashMap<Object, Object> response) {
        var chart = response.get("chart");
        var chartResult = ((LinkedHashMap<Object, Object>) chart).get("result");
        var stockDetails = ((ArrayList<Object>) chartResult).get(0);
        var metaDetails = ((LinkedHashMap<Object, Object>) stockDetails).get("meta");
        var symbol = ((LinkedHashMap<Object, Object>) metaDetails).get("symbol");
        var exchange = ((LinkedHashMap<Object, Object>) metaDetails).get("fullExchangeName");
        var tradeValue = ((LinkedHashMap<Object, Object>) metaDetails).get("regularMarketPrice");
        var currency = ((LinkedHashMap<Object, Object>) metaDetails).get("currency");
        var tradeTime = ((LinkedHashMap<Object, Object>) metaDetails).get("regularMarketTime");
        var dayHighPrice = ((LinkedHashMap<Object, Object>) metaDetails).get("regularMarketDayHigh");
        var dayLowPrice = ((LinkedHashMap<Object, Object>) metaDetails).get("regularMarketDayLow");
        var previousClosePrice = ((LinkedHashMap<Object, Object>) metaDetails).get("previousClose");
        var volumeTraded = ((LinkedHashMap<Object, Object>) metaDetails).get("regularMarketVolume");
        return StockPrice.newBuilder()
                .setSymbol((String) symbol)
                .setExchange((String) exchange)
                .setPrice((Double) tradeValue)
                .setDayHighPrice((Double) dayHighPrice)
                .setDayLowPrice((Double) dayLowPrice)
                .setPreviousClosePrice((Double) previousClosePrice)
                .setVolumeTraded((Integer) volumeTraded)
                .setCurrency((String) currency)
                .setTradeTime((Integer) tradeTime)
                .build();
    }


}
