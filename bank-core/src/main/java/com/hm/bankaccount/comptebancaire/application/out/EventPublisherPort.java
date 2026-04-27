package com.hm.bankaccount.comptebancaire.application.out;

import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.operation.OperationEvent;

import java.time.LocalDate;
import java.util.Collection;

public interface EventPublisherPort {

    void publish(String numeroDeCompte, Collection<OperationEvent> operationEvent);

    Collection<OperationEvent> findByNumeroDeCompte(String numeroCompteBancaire, final LocalDate dateDebut, final LocalDate dateFin);

}
