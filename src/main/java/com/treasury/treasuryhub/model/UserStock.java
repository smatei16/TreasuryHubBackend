package com.treasury.treasuryhub.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user_stock", schema = "th1")
public class UserStock {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "purchase_price")
    private Double purchasePrice;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @Column(name = "create_ts")
    @CreationTimestamp
    private LocalDateTime create_ts;

    @Column(name = "modify_ts")
    @UpdateTimestamp
    private LocalDateTime modify_ts;
}
