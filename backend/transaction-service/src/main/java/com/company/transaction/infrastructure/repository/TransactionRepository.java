package com.company.transaction.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.company.transaction.domain.model.Transaction;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionRepository extends ReactiveCrudRepository<Transaction, UUID> {

    Mono<Boolean> existsByExternalId(String externalId);

    Flux<Transaction> findByMerchantIdOrderByCreatedAtDesc(String merchantId);

    Mono<Long> countByMerchantId(String merchantId);
}
