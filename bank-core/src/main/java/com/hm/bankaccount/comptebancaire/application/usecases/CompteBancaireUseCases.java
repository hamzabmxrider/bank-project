package com.hm.bankaccount.comptebancaire.application.usecases;

import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.CompteBancaire;
import com.hm.bankaccount.comptebancaire.domain.model.produitfinancier.CreditBancaire;
import com.hm.bankaccount.comptebancaire.domain.model.produitfinancier.CreditBancaireType;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;

public interface CompteBancaireUseCases {

    CompteBancaire creerUnCompteBancaire(BigDecimal solde);

    CompteBancaire creerUnLivretEpargne(BigDecimal solde);

    CompteBancaire depot(String numeroDeCompte, BigDecimal montant);

    CompteBancaire retrait(String numeroDeCompte, BigDecimal montant);

    Collection<CreditBancaire> attacherCreditBancaire(String numeroCompteBancaire, CreditBancaireType creditBancaireType, BigDecimal actif);

    byte[] redigerReleveBancaire(String numeroCompteBancaire, LocalDate dateDebut, LocalDate dateFin) throws IOException;
}
