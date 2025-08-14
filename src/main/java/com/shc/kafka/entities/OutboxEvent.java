package com.shc.kafka.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "outbox")
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String aggregateType;

    @Column(nullable = false)
    private String aggregateId;

    @Column(nullable = false)
    private String topic;

    @Lob
    @Column(nullable = false)
    private String payload;

    @Column(nullable = false)
    private boolean published = false;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime publishedAt;
}
