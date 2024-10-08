package com.treasury.treasuryhub.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "transaction_category", schema = "th1")
public class TransactionCategory {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "name")
    private String name;

    @Column(name = "transaction_type")
    private String transactionType;

    //TH-32 temporarily adding budget here to ease up the solution a bit - might upgrade later with versioning
    @Column(name = "budget")
    private Double budget;
}
