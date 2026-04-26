package com.hm.bankaccount.comptebancaire.application.out;

import com.hm.bankaccount.comptebancaire.domain.model.DomainEvent;

import java.util.Collection;

public interface EventPublisherPort {

    void publish(String numeroDeCompte, Collection<DomainEvent> domainEvent);
}
