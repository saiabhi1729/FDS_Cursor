package com.company.transaction.api.dto;

import java.math.BigDecimal;
import java.util.Map;

import com.company.transaction.domain.model.PaymentMethod;
import com.company.transaction.domain.model.TransactionType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload for creating a transaction")
public record TransactionRequest(
        @Schema(description = "Idempotent external identifier", example = "txn_123456789")
        @NotBlank @Size(max = 64)
        String externalId,

        @Schema(description = "Merchant that owns the transaction", example = "merchant_991")
        @NotBlank @Size(max = 64)
        String merchantId,

        @Schema(description = "Customer identifier", example = "customer_559")
        @NotBlank @Size(max = 64)
        String customerId,

        @Schema(description = "Transaction amount", example = "125.92")
        @NotNull @Positive
        BigDecimal amount,

        @Schema(description = "ISO currency", example = "USD")
        @NotBlank @Size(min = 3, max = 3)
        String currency,

        @Schema(description = "Transaction type", example = "PURCHASE")
        @NotNull
        TransactionType type,

        @Schema(description = "Payment method", example = "CARD")
        @NotNull
        PaymentMethod paymentMethod,

        @Schema(description = "Processing channel", example = "MOBILE")
        @NotBlank @Size(max = 32)
        String channel,

        @Schema(description = "Opaque metadata that will be persisted + sent to downstream services")
        Map<String, Object> metadata
) {
}
