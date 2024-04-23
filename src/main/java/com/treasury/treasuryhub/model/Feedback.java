package com.treasury.treasuryhub.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "feedback", schema = "th1")
public class Feedback {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "rating")
    private int rating;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "favourite_functionalities")
    private List<String> favouriteFunctionalities;

    @Column(name = "comment")
    private String comment;

    @Column(name = "create_ts")
    @CreationTimestamp
    private LocalDateTime create_ts;
}
