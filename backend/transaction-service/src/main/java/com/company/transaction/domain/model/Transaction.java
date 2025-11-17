package com.company.transaction.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table("transactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    private UUID id;

    @Column("external_id")
    private String externalId;

    @Column("merchant_id")
    private String merchantId;

    @Column("customer_id")
    private String customerId;

    private BigDecimal amount;

    private String currency;

    private TransactionStatus status;

    private TransactionType type;

    @Column("payment_method")
    private PaymentMethod paymentMethod;

    private String channel;

    @Column("risk_score")
    private BigDecimal riskScore;

    @Column("risk_level")
    private RiskLevel riskLevel;

    /**
     * JSON payload (stringified) storing device fingerprinting or other metadata.
     */
    private String metadata;

    @CreatedDate
    @Column("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private Instant updatedAt;

    @Version
    private Long version;
}
