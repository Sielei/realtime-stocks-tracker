package com.hs.ktsp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class StocksService {
    private final StockRepository stockRepository;

    StocksService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    void save(Stock stock){
        var existingStock = stockRepository.findBySymbolAndTradeTime(stock.getSymbol(), stock.getTradeTime());
        if (existingStock.isEmpty()){
            stockRepository.save(stock);
        }
    }
}
