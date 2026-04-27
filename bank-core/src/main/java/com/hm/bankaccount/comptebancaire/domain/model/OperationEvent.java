package com.hm.bankaccount.comptebancaire.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public record OperationEvent(OperationEventType type, BigDecimal amount, String message, Instant date) {
}
