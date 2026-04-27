package com.hm.bankaccount.comptebancaire.domain.model.produitfinancier;

import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.CompteBancaire;
import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.operation.OperationEvent;

import java.util.UUID;

public abstract class ProduitFinancier {

    private UUID id;

    private String nom;

    private Actif actif;

    public ProduitFinancier(final UUID id, final String nom, final String actif) {
        this.id = id;
        this.nom = nom;
        this.actif = new Actif(actif);
    }

    abstract void verifierEligibilite(final CompteBancaire compteBancaire, final OperationEvent operationEvent);

    public UUID getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public Actif getActif() {
        return actif;
    }
}
