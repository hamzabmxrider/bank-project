package com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.operation;

import java.math.BigDecimal;
import java.time.Instant;

public record OperationEvent(String numeroCompteBancaire, OperationEventType type, BigDecimal montant, BigDecimal solde,
                             String message, Instant date) {
}
