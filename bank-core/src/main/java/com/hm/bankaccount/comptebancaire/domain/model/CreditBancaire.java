package com.hm.bankaccount.comptebancaire.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public abstract class CreditBancaire implements ProduitFinancier {
    private UUID id;
    protected BigDecimal montant;
    protected CreditBancaireType type;

    public CreditBancaire(UUID id,  BigDecimal montant) {
        this.montant = montant;
        this.id = id;
    }

    public static CreditBancaire toDomain(UUID id, BigDecimal montant, CreditBancaireType type) {
        if(type == CreditBancaireType.DECOUVERT)
            return new DecouvertAutorise(id, montant);
        return new CreditBancaire(id, montant) {
            @Override
            public void verifierEligibilite(CompteBancaire compteBancaire, OperationEvent operationEvent) {
                // RAF
            }
        };
    }
}
