package com.company.transaction.domain.model;

public enum TransactionStatus {
    PENDING,
    AUTHORIZED,
    SETTLED,
    DECLINED,
    REVERSED,
    CANCELLED,
    FRAUD_SUSPECTED
}
