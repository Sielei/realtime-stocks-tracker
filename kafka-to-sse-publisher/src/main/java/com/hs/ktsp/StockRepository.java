package com.hs.ktsp;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

interface StockRepository extends JpaRepository<Stock, UUID> {
    Optional<Stock> findBySymbolAndTradeTime(String symbol, Instant tradeTime);
}