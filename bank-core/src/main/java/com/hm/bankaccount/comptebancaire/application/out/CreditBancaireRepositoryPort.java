package com.hm.bankaccount.comptebancaire.application.out;

import com.hm.bankaccount.comptebancaire.domain.model.produitfinancier.CreditBancaire;
import com.hm.bankaccount.comptebancaire.domain.model.produitfinancier.ProduitFinancier;

import java.util.Collection;

public interface CreditBancaireRepositoryPort {

    Collection<ProduitFinancier> findByNumeroDeCompte(String numeroDeCompte);

    Collection<CreditBancaire> creerCreditBancaire(String numeroDeCompte, Collection<CreditBancaire> produitsFinanciers);
}
