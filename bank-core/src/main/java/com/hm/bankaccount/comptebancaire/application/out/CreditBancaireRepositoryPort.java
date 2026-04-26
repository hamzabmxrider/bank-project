package com.hm.bankaccount.comptebancaire.application.out;

import com.hm.bankaccount.comptebancaire.domain.model.ProduitFinancier;

import java.util.Collection;

public interface CreditBancaireRepositoryPort {

    Collection<ProduitFinancier> findByNumeroDeCompte(String numeroDeCompte);
}
