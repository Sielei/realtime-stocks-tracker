package com.hs.ktsp;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface StockRepository extends JpaRepository<Stock, UUID> {
}