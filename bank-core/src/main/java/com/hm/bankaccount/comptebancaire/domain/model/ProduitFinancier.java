package com.hm.bankaccount.comptebancaire.domain.model;

public interface ProduitFinancier {

    void verifierEligibilite(final CompteBancaire compteBancaire, final OperationEvent operationEvent);
}
