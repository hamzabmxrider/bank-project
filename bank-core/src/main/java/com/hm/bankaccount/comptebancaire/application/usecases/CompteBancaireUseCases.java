package com.hm.bankaccount.comptebancaire.application.usecases;

import com.hm.bankaccount.comptebancaire.domain.model.CompteBancaire;
import com.hm.bankaccount.comptebancaire.domain.model.CreditBancaire;
import com.hm.bankaccount.comptebancaire.domain.model.CreditBancaireType;

import java.math.BigDecimal;
import java.util.Collection;

public interface CompteBancaireUseCases {

    CompteBancaire creerUnCompteBancaire(BigDecimal solde);

    CompteBancaire depot(String numeroDeCompte, BigDecimal montant);

    CompteBancaire retrait(String numeroDeCompte, BigDecimal montant);

    Collection<CreditBancaire> attacherCreditBancaire(String numeroCompteBancaire, CreditBancaireType creditBancaireType, BigDecimal actif);
}
