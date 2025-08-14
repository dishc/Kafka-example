package com.shc.kafka.services;

import com.shc.kafka.entities.OutboxEvent;
import com.shc.kafka.repositories.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OutboxService {

    private final OutboxRepository outboxRepo;

    @Transactional(transactionManager = "transactionManager")
    public void markPublished(List<OutboxEvent> batch) {
        for (OutboxEvent e : batch) {
            e.setPublished(true);
            e.setPublishedAt(LocalDateTime.now());
            outboxRepo.save(e);
        }
    }
}
