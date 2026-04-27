package com.hm.bankaccount.comptebancaire.infrastructure.adapter.rest.dto;

import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.CompteBancaire;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CompteBancaireOperation {

    private UUID id;

    private String numeroDeCompte;

    private BigDecimal montant;

    public static CompteBancaireOperation from(CompteBancaire compteBancaire) {
        return new CompteBancaireOperation(
                compteBancaire.getId(),
                compteBancaire.getNumeroDeCompte(),
                compteBancaire.getSolde());
    }
}
