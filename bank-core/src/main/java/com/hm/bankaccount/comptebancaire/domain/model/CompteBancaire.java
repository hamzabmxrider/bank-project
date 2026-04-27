package com.hm.bankaccount.comptebancaire.domain.model;


import com.hm.bankaccount.comptebancaire.domain.exception.BusinessRuleViolationException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class CompteBancaire {

    private UUID id;

    private final String NumeroDeCompte;

    private BigDecimal solde;

    private Collection<OperationEvent> events;

    protected CompteBancaire(
            UUID id,
            String NumeroDeCompte,
            BigDecimal solde
    ) {
        this.id = id;
        this.NumeroDeCompte = NumeroDeCompte;
        this.solde = solde;
        this.events = new ArrayList<>();
        this.events.add(
                new OperationEvent(
                        OperationEventType.OUVERTURE_COMPTE,
                        solde,
                        String.format("Ouverture du compte %s avec un dépôt initial de %s", getNumeroDeCompte(), solde),
                        Instant.now())
        );
    }

    public static CompteBancaire ouvrirUnCompteCourant(String numeroDeCompte, BigDecimal soldeInitial) {
        if (soldeInitial.signum() < 0) {
            throw new BusinessRuleViolationException("Le montant du dépôt initial doit être supérieur ou égal à 0");
        }
        if ("".equals(numeroDeCompte) || numeroDeCompte == null) {
            throw new BusinessRuleViolationException("Le numéro de compte ne doit pas être null");
        }
        return new CompteBancaire(null, numeroDeCompte, soldeInitial);
    }

    public static CompteBancaire toDomain(
            UUID id,
            String accountNumber,
            BigDecimal solde
    ) {
        return new CompteBancaire(id, accountNumber, solde);
    }

    public boolean estCompatible(final CreditBancaireType creditBancaireType) {
        return true;
    }

    public synchronized void depot(BigDecimal montant) {
        if (montant.signum() < 0) {
            throw new BusinessRuleViolationException("Le montant du dépôt doit être supérieur à 0");
        }

        solde = solde.add(montant);
        this.events.add(
                new OperationEvent(
                        OperationEventType.DEPOT_COMPTE_BANCAIRE,
                        montant,
                        String.format("Dépôt de %s sur le compte %s", montant, getNumeroDeCompte()),
                        Instant.now()));
    }

    public synchronized void retrait(BigDecimal montant, boolean estCredit) {
        if (montant.signum() < 0) {
            throw new BusinessRuleViolationException("Le montant du dépôt doit être supérieur à 0");
        }
        BigDecimal nextBalance = solde.subtract(montant);

        if (!estCredit && nextBalance.compareTo(new BigDecimal(0)) < 0) {
            throw new BusinessRuleViolationException("Fonds insuffisants.");
        }

        solde = nextBalance;
        this.events.add(
                new OperationEvent(
                        OperationEventType.RETRAIT_COMPTE_BANCAIRE,
                        montant,
                        String.format("Retrait de %s sur le compte %s", montant, getNumeroDeCompte()),
                        Instant.now()));
    }

    public synchronized void attacherProduitFinancier(ProduitFinancier produitFinancier) {
        this.events.add(
                new OperationEvent(
                        OperationEventType.RATTACHEMENT_PRODUIT_FINANCIER,
                        new BigDecimal(produitFinancier.getActif().getValue()),
                        produitFinancier.getNom(),
                        Instant.now()
                ));
    }

    public UUID getId() {
        return id;
    }

    public String getNumeroDeCompte() {
        return NumeroDeCompte;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public Collection<OperationEvent> getEvents() {
        return Collections.unmodifiableCollection(events);
    }
}
