package com.shc.kafka.producers;

import com.shc.kafka.entities.OutboxEvent;
import com.shc.kafka.repositories.OutboxRepository;
import com.shc.kafka.services.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.data.domain.PageRequest.of;

@RequiredArgsConstructor
@Component
public class OutboxPublisher {
    private final OutboxService outboxService;
    private final OutboxRepository outboxRepo;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.outbox.batch-size:50}")
    private int batchSize;

    @Scheduled(fixedDelayString = "${app.outbox.poll-interval-ms:2000}")
    public void publishPending() {
        List<OutboxEvent> batch = outboxRepo.findUnpublishedBatch(of(0, batchSize));
        if (batch.isEmpty()) return;

        kafkaTemplate.executeInTransaction(k -> {
            for (OutboxEvent ev : batch) {
                // key = aggregateId (partitioning by aggregate)
                k.send(ev.getTopic(), ev.getAggregateId(), ev.getPayload());
            }
            return true;
        });

        outboxService.markPublished(batch);
    }
}
