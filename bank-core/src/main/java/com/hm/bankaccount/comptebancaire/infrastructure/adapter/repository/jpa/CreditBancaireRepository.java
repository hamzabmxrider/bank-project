package com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa;

import com.hm.bankaccount.comptebancaire.application.out.CreditBancaireRepositoryPort;
import com.hm.bankaccount.comptebancaire.domain.model.CreditBancaire;
import com.hm.bankaccount.comptebancaire.domain.model.ProduitFinancier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CreditBancaireRepository implements CreditBancaireRepositoryPort {

    private final CreditBancaireJpaRepository creditBancaireJpaRepository;

    @Override
    public Collection<ProduitFinancier> findByNumeroDeCompte(String numeroDeCompte) {
        return this.creditBancaireJpaRepository.findByNumeroDeCompte(numeroDeCompte)
                .stream()
                .map(e ->
                        CreditBancaire.toDomain(
                                e.getId(),
                                e.getMontant(),
                        e.getType()))
                .collect(Collectors.toUnmodifiableList());
    }
}
