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

    Stock save(Stock stock){
        return stockRepository.save(stock);
    }
}
