package com.treasury.treasuryhub.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "stock", schema = "th1")
public class Stock {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "exchange")
    private String exchange;

    @Column(name = "exchange_short_name")
    private String exchangeShortName;

    @Column(name = "type")
    private String type;

    @Column(name = "modify_ts")
    @UpdateTimestamp
    private LocalDateTime modify_ts;
}
