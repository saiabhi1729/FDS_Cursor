package com.company.transaction.infrastructure.messaging;

import org.springframework.stereotype.Component;

import com.company.transaction.domain.model.Transaction;
import com.company.transaction.infrastructure.messaging.event.TransactionCreatedEvent;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;

@Component
@RequiredArgsConstructor
public class TransactionEventPublisher {

    private final TransactionTopicsProperties topicsProperties;
    private final TransactionEventProducer producer;

    public Mono<Transaction> publishTransactionCreated(Transaction transaction) {
        TransactionCreatedEvent event = producer.toEvent(transaction);
        return producer.send(topicsProperties.transactionsTopic(), event)
                .thenReturn(transaction);
    }
}
