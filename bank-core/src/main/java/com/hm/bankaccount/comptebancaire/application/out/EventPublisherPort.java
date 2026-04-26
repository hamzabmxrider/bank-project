package com.hm.bankaccount.comptebancaire.application.out;

import com.hm.bankaccount.comptebancaire.domain.model.OperationEvent;

import java.util.Collection;

public interface EventPublisherPort {

    void publish(String numeroDeCompte, Collection<OperationEvent> operationEvent);
}
