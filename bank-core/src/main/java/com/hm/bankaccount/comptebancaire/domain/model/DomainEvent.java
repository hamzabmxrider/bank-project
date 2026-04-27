package com.hm.bankaccount.comptebancaire.domain.model;

import java.time.Instant;

public record DomainEvent(DomainEventType type, String message, Instant date) {
}
