package com.company.transaction.application;

import java.math.BigDecimal;

import com.company.transaction.domain.model.RiskLevel;

public record RiskAssessment(BigDecimal score, RiskLevel level) {
}
