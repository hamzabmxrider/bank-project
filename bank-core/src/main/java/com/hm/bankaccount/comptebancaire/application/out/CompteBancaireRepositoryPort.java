package com.hm.bankaccount.comptebancaire.application.out;

import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.CompteBancaire;

import java.time.LocalDate;

public interface CompteBancaireRepositoryPort {

    CompteBancaire mettreAJourCompteBancaire(CompteBancaire compteBancaire);

    CompteBancaire persisterCompteBancaire(CompteBancaire compteBancaire);

    CompteBancaire findByNumeroDeCompte(String numeroDeCompte);

    CompteBancaire findByNumeroDeCompte(String numeroDeCompte, LocalDate dateDebut, LocalDate dateFin);
}
