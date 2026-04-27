package com.hm.bankaccount.comptebancaire.domain.model.comptebancaire;


import com.hm.bankaccount.comptebancaire.domain.exception.BusinessRuleViolationException;
import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.operation.OperationEvent;
import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.operation.OperationEventType;
import com.hm.bankaccount.comptebancaire.domain.model.produitfinancier.CreditBancaireType;
import com.hm.bankaccount.comptebancaire.domain.model.produitfinancier.ProduitFinancier;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class CompteBancaire {

    private UUID id;

    TypeCompteBancaire type;

    private final String numeroDeCompte;

    private BigDecimal solde;

    protected Collection<OperationEvent> events;

    protected CompteBancaire(UUID id,
                             TypeCompteBancaire type,
                             String numeroDeCompte,
                             BigDecimal solde,
                             Collection<OperationEvent> events) {
        this.id = id;
        this.type = type;
        this.numeroDeCompte = numeroDeCompte;
        this.solde = solde;
        this.events = events;
    }

    protected CompteBancaire(
            UUID id,
            TypeCompteBancaire type,
            String NumeroDeCompte,
            BigDecimal solde
    ) {
        this.id = id;
        this.type = type;
        this.numeroDeCompte = NumeroDeCompte;
        this.solde = solde;
        this.events = new ArrayList<>();
    }

    public static CompteBancaire ouvrirUnCompteCourant(String numeroDeCompte, BigDecimal soldeInitial) {
        if (soldeInitial.signum() < 0) {
            throw new BusinessRuleViolationException("Le montant du dépôt initial doit être supérieur ou égal à 0");
        }
        if ("".equals(numeroDeCompte) || numeroDeCompte == null) {
            throw new BusinessRuleViolationException("Le numéro de compte ne doit pas être null");
        }
        final CompteBancaire compteBancaire = new CompteBancaire(
                null, TypeCompteBancaire.COMPTE_COURANT, numeroDeCompte, soldeInitial);
        compteBancaire.events.add(
                new OperationEvent(
                        numeroDeCompte,
                        OperationEventType.OUVERTURE_COMPTE,
                        soldeInitial,
                        soldeInitial,
                        String.format("Ouverture du compte %s avec un dépôt initial de %s", numeroDeCompte, soldeInitial),
                        Instant.now()));
        return compteBancaire;
    }

    public void verifierCompatibiliteCreditBancaire(final CreditBancaireType creditBancaireType) {
        // RAF
    }

    public synchronized void depot(BigDecimal montant) {
        if (montant.signum() < 0) {
            throw new BusinessRuleViolationException("Le montant du dépôt doit être supérieur à 0");
        }

        solde = solde.add(montant);
        this.events.add(
                new OperationEvent(
                        getNumeroDeCompte(),
                        OperationEventType.DEPOT_COMPTE_BANCAIRE,
                        montant,
                        solde,
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
                        getNumeroDeCompte(),
                        OperationEventType.RETRAIT_COMPTE_BANCAIRE,
                        montant,
                        solde,
                        String.format("Retrait de %s sur le compte %s", montant, getNumeroDeCompte()),
                        Instant.now()));
    }

    public synchronized void attacherProduitFinancier(ProduitFinancier produitFinancier) {
        this.events.add(
                new OperationEvent(
                        getNumeroDeCompte(),
                        OperationEventType.RATTACHEMENT_PRODUIT_FINANCIER,
                        new BigDecimal(produitFinancier.getActif().getValue()),
                        solde,
                        produitFinancier.getNom(),
                        Instant.now()
                ));
    }

    public UUID getId() {
        return id;
    }

    public String getNumeroDeCompte() {
        return numeroDeCompte;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public TypeCompteBancaire getType() {
        return type;
    }

    public Collection<OperationEvent> getEvents() {
        return Collections.unmodifiableCollection(events);
    }
}
