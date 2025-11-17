package com.company.transaction.application;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.company.transaction.api.dto.PagedResponse;
import com.company.transaction.api.dto.TransactionRequest;
import com.company.transaction.api.dto.TransactionResponse;
import com.company.transaction.domain.model.TransactionStatus;
import com.company.transaction.infrastructure.messaging.TransactionEventPublisher;
import com.company.transaction.infrastructure.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final TransactionValidator validator;
    private final TransactionRiskAnalyzer riskAnalyzer;
    private final TransactionMapper mapper;
    private final TransactionEventPublisher eventPublisher;

    public Mono<TransactionResponse> createTransaction(TransactionRequest request) {
        return validator.validateCreate(request)
                .map(valid -> mapper.toEntity(valid, riskAnalyzer.assess(valid)))
                .flatMap(repository::save)
                .flatMap(eventPublisher::publishTransactionCreated)
                .map(mapper::toResponse);
    }

    public Mono<TransactionResponse> getTransaction(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Transaction %s not found".formatted(id))))
                .map(mapper::toResponse);
    }

    public Mono<PagedResponse<TransactionResponse>> getTransactionsForMerchant(String merchantId, int page, int size) {
        Assert.isTrue(page >= 0, "page must be positive");
        Assert.isTrue(size > 0 && size <= 100, "size must be between 1 and 100");

        Flux<TransactionResponse> responses = repository.findByMerchantIdOrderByCreatedAtDesc(merchantId)
                .skip((long) page * size)
                .take(size)
                .map(mapper::toResponse);

        Mono<Long> total = repository.countByMerchantId(merchantId);

        return Mono.zip(responses.collectList(), total)
                .map(tuple -> new PagedResponse<>(tuple.getT1(), tuple.getT2(), page, size));
    }

    public Mono<TransactionResponse> updateStatus(UUID id, TransactionStatus status) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Transaction %s not found".formatted(id))))
                .flatMap(entity -> {
                    entity.setStatus(status);
                    return repository.save(entity);
                })
                .map(mapper::toResponse);
    }
}
