package com.hm.bankaccount.comptebancaire.domain.model.comptebancaire;

import com.hm.bankaccount.comptebancaire.domain.exception.BusinessRuleViolationException;
import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.operation.OperationEvent;
import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.operation.OperationEventType;
import com.hm.bankaccount.comptebancaire.domain.model.produitfinancier.CreditBancaireType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class LivretEpargne extends CompteBancaire {

    BigDecimal plafondDepot;

    protected LivretEpargne(UUID id,
                            TypeCompteBancaire type,
                            String NumeroDeCompte,
                            BigDecimal solde,
                            Collection<OperationEvent> events) {
        super(id, type, NumeroDeCompte, solde, events);
        this.plafondDepot = new BigDecimal("22950");
    }

    public static LivretEpargne ouvrirUnLivretEpargne(String numeroDeCompte, BigDecimal soldeInitial) {
        if (soldeInitial.signum() < 0) {
            throw new BusinessRuleViolationException("Le montant du dépôt initial doit être supérieur ou égal à 0");
        }
        if ("".equals(numeroDeCompte) || numeroDeCompte == null) {
            throw new BusinessRuleViolationException("Le numéro de compte ne doit pas être null");
        }
        final LivretEpargne livretEpargne = new LivretEpargne(
                null, TypeCompteBancaire.COMPTE_EPARGNE, numeroDeCompte, soldeInitial, new ArrayList<>());
        livretEpargne.events.add(
                new OperationEvent(
                        numeroDeCompte,
                        OperationEventType.OUVERTURE_COMPTE,
                        soldeInitial,
                        soldeInitial,
                        String.format("Ouverture du compte %s avec un dépôt initial de %s", numeroDeCompte, soldeInitial),
                        Instant.now()));
        return livretEpargne;
    }

    @Override
    public synchronized void depot(BigDecimal montant) {
        if (plafondDepot.subtract(montant).compareTo(new BigDecimal("0")) > 0) {
            throw new BusinessRuleViolationException("Le montant du dépôt doit être supérieur à 0");
        }
        super.depot(montant);
    }

    @Override
    public void verifierCompatibiliteCreditBancaire(final CreditBancaireType creditBancaireType) {
        super.verifierCompatibiliteCreditBancaire(creditBancaireType);
        if (creditBancaireType == CreditBancaireType.DECOUVERT) {
            throw new BusinessRuleViolationException("Un compte livret d'épargne ne peut avoir de découvert autorisé");
        }
    }


}
