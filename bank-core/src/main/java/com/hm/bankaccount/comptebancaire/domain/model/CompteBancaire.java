package com.hm.bankaccount.comptebancaire.domain.model;


import com.hm.bankaccount.comptebancaire.domain.exception.BusinessRuleViolationException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

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

    private CompteBancaire(
            UUID id,
            @NotEmpty String NumeroDeCompte,
            @Min(value = 0) BigDecimal solde
    ) {
        this.id = id;
        this.NumeroDeCompte = NumeroDeCompte;
        this.solde = solde;
        this.events = new ArrayList<>();
    }

    public static CompteBancaire ouvrirUnCompteCourant(String numeroDeCompte, BigDecimal soldeInitial) {
        return new CompteBancaire(null, numeroDeCompte, soldeInitial);
    }

    public static CompteBancaire toDomain(
            UUID id,
            String accountNumber,
            BigDecimal solde
    ) {
        return new CompteBancaire(id, accountNumber, solde);
    }

    public synchronized void depot(BigDecimal montant) {
        if(montant.signum() < 0) {
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
        if(montant.signum() < 0) {
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
