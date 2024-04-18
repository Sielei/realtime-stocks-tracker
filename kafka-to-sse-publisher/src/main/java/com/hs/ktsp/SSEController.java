package com.hs.ktsp;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/stocks")
@CrossOrigin(origins = "http://localhost:8183/")
public class SSEController {
    private final StocksKafkaConsumer stocksKafkaConsumer;

    public SSEController(StocksKafkaConsumer stocksKafkaConsumer) {
        this.stocksKafkaConsumer = stocksKafkaConsumer;
    }


    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<ServerSentEvent<StocksKafkaConsumer.StocksQuote>> stockPriceConsumer(){
        return Flux.create(sink -> stocksKafkaConsumer.subscribe(sink::next))
                .map(stockQuote -> ServerSentEvent.<StocksKafkaConsumer.StocksQuote>builder()
                        .data((StocksKafkaConsumer.StocksQuote) stockQuote)
                        .event("stockQuote")
                        .build());
    }
}
