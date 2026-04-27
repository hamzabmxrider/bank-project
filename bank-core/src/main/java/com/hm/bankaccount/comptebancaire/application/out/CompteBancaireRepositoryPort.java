package com.hm.bankaccount.comptebancaire.application.out;

import com.hm.bankaccount.comptebancaire.domain.model.CompteBancaire;

public interface CompteBancaireRepositoryPort {

    CompteBancaire mettreAJourCompteBancaire(CompteBancaire compteBancaire);

    CompteBancaire persisterCompteBancaire(CompteBancaire compteBancaire);

    CompteBancaire findByNumeroDeCompte(String numeroDeCompte);
}
