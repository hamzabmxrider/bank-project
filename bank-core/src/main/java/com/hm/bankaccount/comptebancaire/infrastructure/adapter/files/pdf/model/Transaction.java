package com.hm.bankaccount.comptebancaire.infrastructure.adapter.files.pdf.model;

import java.math.BigDecimal;
import java.time.Instant;

public record Transaction(Instant date, String message, BigDecimal debit, BigDecimal credit, BigDecimal balance) {
}
