package com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa;

import com.hm.bankaccount.comptebancaire.application.out.CreditBancaireRepositoryPort;
import com.hm.bankaccount.comptebancaire.domain.model.produitfinancier.CreditBancaire;
import com.hm.bankaccount.comptebancaire.domain.model.produitfinancier.ProduitFinancier;
import com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa.entity.CompteBancaireEntity;
import com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa.entity.CreditBancaireEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CreditBancaireRepository implements CreditBancaireRepositoryPort {

    private final CreditBancaireJpaRepository creditBancaireJpaRepository;

    private final CompteBancaireJpaRepository compteBancaireJpaRepository;

    @Override
    public Collection<ProduitFinancier> findByNumeroDeCompte(String numeroDeCompte) {
        return this.creditBancaireJpaRepository.findByNumeroDeCompte(numeroDeCompte)
                .stream()
                .map(e ->
                        CreditBancaire.toDomain(
                                e.getId(),
                                e.getMontant(),
                                e.getType()))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<CreditBancaire> creerCreditBancaire(String numeroDeCompte, Collection<CreditBancaire> produitsFinanciers) {
        final CompteBancaireEntity compteBancaireEntity = this.compteBancaireJpaRepository
                .findByNumeroDeCompte(numeroDeCompte)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Le compte %s est introuvable", numeroDeCompte)));

        final Collection<CreditBancaireEntity> creditBancaireEntities = produitsFinanciers
                .stream()
                .map(e -> CreditBancaireEntity.from(compteBancaireEntity, e))
                .toList();
        return this.creditBancaireJpaRepository
                .saveAll(creditBancaireEntities)
                .stream()
                .map(e ->
                        CreditBancaire.toDomain(
                                e.getId(),
                                e.getMontant(),
                                e.getType()))
                .toList();
    }
}
