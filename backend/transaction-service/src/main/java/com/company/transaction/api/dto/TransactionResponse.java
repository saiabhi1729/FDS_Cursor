package com.company.transaction.api.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.company.transaction.domain.model.PaymentMethod;
import com.company.transaction.domain.model.RiskLevel;
import com.company.transaction.domain.model.TransactionStatus;
import com.company.transaction.domain.model.TransactionType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a transaction returned to API clients")
public record TransactionResponse(
        UUID id,
        String externalId,
        String merchantId,
        String customerId,
        BigDecimal amount,
        String currency,
        TransactionStatus status,
        TransactionType type,
        PaymentMethod paymentMethod,
        String channel,
        BigDecimal riskScore,
        RiskLevel riskLevel,
        Instant createdAt,
        Instant updatedAt
) {
}
