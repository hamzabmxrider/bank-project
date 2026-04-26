package com.hm.bankaccount.comptebancaire.domain.model;

import com.hm.bankaccount.comptebancaire.domain.exception.BusinessRuleViolationException;

import java.math.BigDecimal;
import java.util.UUID;

public class DecouvertAutorise extends CreditBancaire {

    public DecouvertAutorise(UUID id, BigDecimal montant) {
        super(id, montant);
        this.type = CreditBancaireType.DECOUVERT;
    }

    public void verifierEligibilite(CompteBancaire compteBancaire, OperationEvent operationEvent) {
        if(operationEvent.type() == OperationEventType.RETRAIT_COMPTE_BANCAIRE) {
            if(compteBancaire.getSolde().abs().compareTo(this.montant) > 0) {
                throw new BusinessRuleViolationException(
                        String.format(
                                "Le découvert n'autorise pas le retrait de %s sur le compte %s",
                                operationEvent.amount(), compteBancaire.getNumeroDeCompte()));
            }
        }
    }
}
