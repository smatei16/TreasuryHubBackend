package com.treasury.treasuryhub.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    private Integer transactionCategoryId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "source_account_id")
    private Integer sourceAccountId;

    @Column(name = "destination_account_id")
    private Integer destinationAccountId;

    @Column(name = "merchant")
    private String merchant;

    @Column(name = "details")
    private String details;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "date")
    private LocalDateTime date;

    //TODO add currency

    @Column(name = "create_ts")
    @CreationTimestamp
    private LocalDateTime create_ts;

    @Column(name = "modify_ts")
    @UpdateTimestamp
    private LocalDateTime modify_ts;
}
