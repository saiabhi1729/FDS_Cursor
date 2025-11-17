package com.company.transaction.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "platform.topics")
public record TransactionTopicsProperties(String transactionsTopic, String fraudAlertsTopic) {
}
