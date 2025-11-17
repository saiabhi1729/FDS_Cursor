package com.company.transaction.infrastructure.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;

import com.company.transaction.domain.model.Transaction;
import com.company.transaction.infrastructure.messaging.event.TransactionCreatedEvent;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TransactionEventProducer {

    private static final Logger log = LoggerFactory.getLogger(TransactionEventProducer.class);

    private final ReactiveKafkaProducerTemplate<String, TransactionCreatedEvent> producerTemplate;

    public TransactionCreatedEvent toEvent(Transaction transaction) {
        return new TransactionCreatedEvent(
                transaction.getId(),
                transaction.getExternalId(),
                transaction.getMerchantId(),
                transaction.getCustomerId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getStatus(),
                transaction.getType(),
                transaction.getPaymentMethod(),
                transaction.getChannel(),
                transaction.getRiskScore(),
                transaction.getRiskLevel(),
                transaction.getCreatedAt(),
                transaction.getMetadata());
    }

    public Mono<Void> send(String topic, TransactionCreatedEvent event) {
        return producerTemplate.send(topic, event.id().toString(), event)
                .doOnSuccess(result -> log.debug("Sent transaction event {} to {}", event.id(), topic))
                .doOnError(error -> log.error("Failed to publish transaction {}", event.id(), error))
                .then();
    }
}
