package com.hm.bankaccount.comptebancaire.domain.model.produitfinancier;

import java.math.BigDecimal;
import java.util.UUID;

public abstract class CreditBancaire extends ProduitFinancier {

    protected BigDecimal montant;

    protected CreditBancaireType type;

    protected CreditBancaire(UUID id, String nom, BigDecimal montant) {
        super(id, nom, montant.toPlainString());
        this.montant = montant;
    }

    public static CreditBancaire toDomain(UUID id, BigDecimal montant, CreditBancaireType type) {
        if (type == CreditBancaireType.DECOUVERT)
            return new DecouvertAutorise(id, montant);
        throw new UnsupportedOperationException();
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public CreditBancaireType getType() {
        return type;
    }
}
