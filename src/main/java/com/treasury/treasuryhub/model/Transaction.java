package com.treasury.treasuryhub.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "transaction", schema = "th1")
public class Transaction {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "transaction_category_id")
    private int transactionCategoryId;

    @Column(name = "type")
    private String type;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "source_account_id")
    private int sourceAccountId;

    @Column(name = "destination_account_id")
    private int destinationAccountId;

    @Column(name = "details")
    private String details;

    //TODO add currency

    @Column(name = "create_ts")
    @CreationTimestamp
    private LocalDateTime create_ts;
}
