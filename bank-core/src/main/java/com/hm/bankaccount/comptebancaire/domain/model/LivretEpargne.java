package com.hm.bankaccount.comptebancaire.domain.model;

import com.hm.bankaccount.comptebancaire.domain.exception.BusinessRuleViolationException;

import java.math.BigDecimal;
import java.util.UUID;

public class LivretEpargne extends CompteBancaire {

    BigDecimal plafondDepot;

    private LivretEpargne(UUID id,
                          String NumeroDeCompte,
                          BigDecimal solde) {
        super(id, NumeroDeCompte, solde);
        this.plafondDepot = new BigDecimal("22950");
    }

    public static LivretEpargne ouvrirUnLivretEpargne(String numeroDeCompte, BigDecimal soldeInitial) {
        if (soldeInitial.signum() < 0) {
            throw new BusinessRuleViolationException("Le montant du dépôt initial doit être supérieur ou égal à 0");
        }
        if ("".equals(numeroDeCompte) || numeroDeCompte == null) {
            throw new BusinessRuleViolationException("Le numéro de compte ne doit pas être null");
        }
        return new LivretEpargne(null, numeroDeCompte, soldeInitial);
    }

    @Override
    public synchronized void depot(BigDecimal montant) {
        if (plafondDepot.subtract(montant).compareTo(new BigDecimal("0")) > 0) {
            throw new BusinessRuleViolationException("Le montant du dépôt doit être supérieur à 0");
        }
        super.depot(montant);
    }


    @Override
    public boolean estCompatible(final CreditBancaireType creditBancaireType) {
        return creditBancaireType != CreditBancaireType.DECOUVERT;
    }
}
