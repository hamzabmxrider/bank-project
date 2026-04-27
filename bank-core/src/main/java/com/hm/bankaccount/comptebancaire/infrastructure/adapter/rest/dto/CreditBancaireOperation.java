package com.hm.bankaccount.comptebancaire.infrastructure.adapter.rest.dto;

import com.hm.bankaccount.comptebancaire.domain.model.produitfinancier.CreditBancaire;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreditBancaireOperation {

    UUID id;

    String numeroDeCompte;

    String nom;

    String actif;

    public static CreditBancaireOperation from(String numeroDeCompte, CreditBancaire creditBancaire) {
        return new CreditBancaireOperation(
                creditBancaire.getId(),
                numeroDeCompte,
                creditBancaire.getNom(),
                creditBancaire.getActif().getValue());
    }
}
