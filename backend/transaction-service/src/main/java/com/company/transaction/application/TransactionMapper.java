package com.company.transaction.application;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.company.transaction.api.dto.TransactionRequest;
import com.company.transaction.api.dto.TransactionResponse;
import com.company.transaction.domain.model.RiskLevel;
import com.company.transaction.domain.model.Transaction;
import com.company.transaction.domain.model.TransactionStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

    private static final Logger log = LoggerFactory.getLogger(TransactionMapper.class);

    private final ObjectMapper objectMapper;

    public Transaction toEntity(TransactionRequest request, RiskAssessment riskAssessment) {
        return Transaction.builder()
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
                .riskScore(riskAssessment.score())
                .riskLevel(riskAssessment.level())
                .metadata(serializeMetadata(request.metadata()))
                .build();
    }

    public TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
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
                transaction.getUpdatedAt());
    }

    private String serializeMetadata(Map<String, Object> metadata) {
        if (metadata == null || metadata.isEmpty()) {
            return "{}";
        }
        try {
            return objectMapper.writeValueAsString(metadata);
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize metadata, falling back to empty object", e);
            return "{}";
        }
    }
}
