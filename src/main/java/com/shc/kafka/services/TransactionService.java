package com.shc.kafka.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shc.kafka.entities.OutboxEvent;
import com.shc.kafka.entities.TransactionEntity;
import com.shc.kafka.repositories.OutboxRepository;
import com.shc.kafka.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TransactionService {

    @Value("${app.topic.name}")
    private String topicName;

    private final TransactionRepository txRepo;
    private final OutboxRepository outboxRepo;
    private final ObjectMapper mapper;

    @SuppressWarnings("UnusedReturnValue")
    @Transactional(transactionManager = "transactionManager")
    public TransactionEntity createTransaction(TransactionEntity tx) throws Exception {
        txRepo.save(tx);

        OutboxEvent event = new OutboxEvent();
        event.setAggregateType("TRANSACTION");
        event.setAggregateId(tx.getExternalId());
        event.setTopic(topicName);
        event.setPayload(mapper.writeValueAsString(tx));
        outboxRepo.save(event);

        return tx;
    }
}