package com.company.transaction.application;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.company.transaction.api.dto.TransactionRequest;
import com.company.transaction.infrastructure.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TransactionValidator {

    private final TransactionRepository repository;

    public Mono<TransactionRequest> validateCreate(TransactionRequest request) {
        return ensureUniqueExternalId(request.externalId()).thenReturn(request);
    }

    private Mono<Void> ensureUniqueExternalId(String externalId) {
        return repository.existsByExternalId(externalId)
                .flatMap(exists -> exists
                        ? Mono.<Void>error(new DuplicateKeyException(
                                "Transaction with externalId %s already exists".formatted(externalId)))
                        : Mono.empty());
    }
}
