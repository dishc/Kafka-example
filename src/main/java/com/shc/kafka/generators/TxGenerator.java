package com.shc.kafka.generators;

import com.shc.kafka.entities.TransactionEntity;
import com.shc.kafka.services.TransactionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Log4j2
@Component
public class TxGenerator {

    private final TransactionService txService;

    public TxGenerator(TransactionService txService) {
        this.txService = txService;
    }

    @Scheduled(fixedDelayString = "5000")
    public void generate() {
        try {
            TransactionEntity tx = new TransactionEntity();
            tx.setExternalId(UUID.randomUUID().toString());
            tx.setUserId(ThreadLocalRandom.current().nextLong(1, 10));
            tx.setAmount(ThreadLocalRandom.current().nextDouble(1.0, 1000.0));
            tx.setType("DEPOSIT");
            txService.createTransaction(tx);
            log.info("Generated tx: {}", tx.getExternalId());
        } catch (Exception e) {
            log.error(e);
        }
    }
}