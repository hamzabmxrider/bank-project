package com.hm.bankaccount.comptebancaire.application.usecases;

import com.hm.bankaccount.comptebancaire.domain.model.CompteBancaire;

import java.math.BigDecimal;

public interface CompteBancaireUseCases {

    CompteBancaire creerUnCompteBancaire(BigDecimal solde);

    CompteBancaire depot(String numeroDeCompte, BigDecimal montant);

    CompteBancaire retrait(String numeroDeCompte, BigDecimal montant);
}
