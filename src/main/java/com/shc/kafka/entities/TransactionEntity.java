package com.shc.kafka.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "transactions", indexes = {@Index(columnList = "externalId", unique = true)})
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String externalId;

    private Long userId;
    private Double amount;
    private String type;

    private LocalDateTime createdAt = LocalDateTime.now();
}

