package com.shc.kafka.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shc.kafka.entities.TransactionEntity;
import com.shc.kafka.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Component
@RequiredArgsConstructor
public class BankConsumer {

    private final TransactionRepository txRepo;
    private final ObjectMapper mapper;

    @KafkaListener(topics = "${app.topic.name}")
    @Transactional(transactionManager = "kafkaTransactionManager")
    public void listen(ConsumerRecord<String, String> record) throws Exception {
        TransactionEntity tx = mapper.readValue(record.value(), TransactionEntity.class);

        // Idempotency: если запись уже есть — пропускаем
        if (txRepo.findByExternalId(tx.getExternalId()).isPresent()) {
            log.info("Already processed: {}", tx.getExternalId());
            return;
        }

        txRepo.save(tx);
        log.info("Consumed and applied: {}", tx.getExternalId());
    }
}
