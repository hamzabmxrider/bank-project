package com.hm.bankaccount.comptebancaire.domain.model.produitfinancier;

import com.hm.bankaccount.comptebancaire.domain.exception.BusinessRuleViolationException;
import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.CompteBancaire;
import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.operation.OperationEvent;
import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.operation.OperationEventType;

import java.math.BigDecimal;
import java.util.UUID;

public class DecouvertAutorise extends CreditBancaire {

    public DecouvertAutorise(UUID id, BigDecimal montant) {
        super(id, String.format("Découvert Autorisé d'un montant de %s", montant), montant);
        this.type = CreditBancaireType.DECOUVERT;
    }

    public void verifierEligibilite(CompteBancaire compteBancaire, OperationEvent operationEvent) {
        if (operationEvent.type() == OperationEventType.RETRAIT_COMPTE_BANCAIRE) {
            if (compteBancaire.getSolde().compareTo(this.montant.negate()) < 0) {
                throw new BusinessRuleViolationException(
                        String.format(
                                "Le découvert n'autorise pas le retrait de %s sur le compte %s",
                                operationEvent.montant(), compteBancaire.getNumeroDeCompte()));
            }
        }
    }
}
