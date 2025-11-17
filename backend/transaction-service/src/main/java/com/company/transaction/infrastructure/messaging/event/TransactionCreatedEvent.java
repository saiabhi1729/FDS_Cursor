package com.company.transaction.infrastructure.messaging.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.company.transaction.domain.model.PaymentMethod;
import com.company.transaction.domain.model.RiskLevel;
import com.company.transaction.domain.model.TransactionStatus;
import com.company.transaction.domain.model.TransactionType;

public record TransactionCreatedEvent(
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
        String metadata
) {
}
