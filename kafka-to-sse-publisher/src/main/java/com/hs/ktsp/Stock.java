package com.hs.ktsp;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "stocks")
@NoArgsConstructor @AllArgsConstructor @Builder
class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    private String symbol;
    private String exchange;
    private String currency;
    private double price;
    private double dayHighPrice;
    private double dayLowPrice;
    private double previousClosePrice;
    private Integer volumeTraded;
    private Long tradeTime;

}