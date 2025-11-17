package com.company.transaction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.company.transaction.api.dto.TransactionRequest;
import com.company.transaction.api.dto.TransactionResponse;
import com.company.transaction.application.RiskAssessment;
import com.company.transaction.application.TransactionMapper;
import com.company.transaction.application.TransactionRiskAnalyzer;
import com.company.transaction.application.TransactionService;
import com.company.transaction.application.TransactionValidator;
import com.company.transaction.domain.model.PaymentMethod;
import com.company.transaction.domain.model.RiskLevel;
import com.company.transaction.domain.model.Transaction;
import com.company.transaction.domain.model.TransactionStatus;
import com.company.transaction.domain.model.TransactionType;
import com.company.transaction.infrastructure.messaging.TransactionEventPublisher;
import com.company.transaction.infrastructure.repository.TransactionRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;

    @Mock
    private TransactionValidator validator;

    @Mock
    private TransactionRiskAnalyzer riskAnalyzer;

    @Mock
    private TransactionMapper mapper;

    @Mock
    private TransactionEventPublisher eventPublisher;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void createTransactionShouldPersistAndEmitEvent() {
        TransactionRequest request = new TransactionRequest(
                "ext-1",
                "merchant-1",
                "customer-1",
                BigDecimal.valueOf(100),
                "USD",
                TransactionType.PURCHASE,
                PaymentMethod.CARD,
                "WEB",
                null);

        Transaction entity = Transaction.builder()
                .id(UUID.randomUUID())
                .externalId(request.externalId())
                .merchantId(request.merchantId())
                .customerId(request.customerId())
                .amount(request.amount())
                .currency(request.currency())
                .status(TransactionStatus.PENDING)
                .type(request.type())
                .paymentMethod(request.paymentMethod())
                .channel(request.channel())
                .riskScore(BigDecimal.TEN)
                .riskLevel(RiskLevel.MEDIUM)
                .metadata("{}")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        TransactionResponse response = new TransactionResponse(
                entity.getId(),
                entity.getExternalId(),
                entity.getMerchantId(),
                entity.getCustomerId(),
                entity.getAmount(),
                entity.getCurrency(),
                entity.getStatus(),
                entity.getType(),
                entity.getPaymentMethod(),
                entity.getChannel(),
                entity.getRiskScore(),
                entity.getRiskLevel(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());

        when(validator.validateCreate(request)).thenReturn(Mono.just(request));
        when(riskAnalyzer.assess(request)).thenReturn(new RiskAssessment(BigDecimal.TEN, RiskLevel.MEDIUM));
        when(mapper.toEntity(any(), any())).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(eventPublisher.publishTransactionCreated(entity)).thenReturn(Mono.just(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        StepVerifier.create(transactionService.createTransaction(request))
                .expectNext(response)
                .verifyComplete();
    }
}
