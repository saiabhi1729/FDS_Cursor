package com.company.transaction.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;

import org.springframework.stereotype.Component;

import com.company.transaction.api.dto.TransactionRequest;
import com.company.transaction.domain.model.PaymentMethod;
import com.company.transaction.domain.model.RiskLevel;

@Component
public class TransactionRiskAnalyzer {

    public RiskAssessment assess(TransactionRequest request) {
        BigDecimal score = BigDecimal.TEN; // baseline

        if (request.amount().compareTo(new BigDecimal("1000")) > 0) {
            score = score.add(BigDecimal.valueOf(25));
        }

        if (!"USD".equalsIgnoreCase(request.currency())) {
            score = score.add(BigDecimal.valueOf(5));
        }

        if (request.paymentMethod() == PaymentMethod.CRYPTO) {
            score = score.add(BigDecimal.valueOf(20));
        }

        if (isNightTime()) {
            score = score.add(BigDecimal.valueOf(10));
        }

        RiskLevel level = deriveRiskLevel(score);
        return new RiskAssessment(score.setScale(2, RoundingMode.HALF_UP), level);
    }

    private boolean isNightTime() {
        LocalTime now = LocalTime.now();
        return now.isAfter(LocalTime.of(0, 0)) && now.isBefore(LocalTime.of(6, 0));
    }

    private RiskLevel deriveRiskLevel(BigDecimal score) {
        if (score.compareTo(BigDecimal.valueOf(60)) >= 0) {
            return RiskLevel.CRITICAL;
        }
        if (score.compareTo(BigDecimal.valueOf(40)) >= 0) {
            return RiskLevel.HIGH;
        }
        if (score.compareTo(BigDecimal.valueOf(20)) >= 0) {
            return RiskLevel.MEDIUM;
        }
        return RiskLevel.LOW;
    }
}
