package com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa;

import com.hm.bankaccount.comptebancaire.application.out.CompteBancaireRepositoryPort;
import com.hm.bankaccount.comptebancaire.domain.model.CompteBancaire;
import com.hm.bankaccount.comptebancaire.application.usecases.CompteBancaireUseCases;
import com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa.entity.CompteBancaireEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CompteBancaireJpaUseCases implements CompteBancaireRepositoryPort{

    private final CompteBancaireJpaRepository compteBancaireJpaRepository;

    @Override
    public CompteBancaire persisterCompteBancaire(CompteBancaire compteBancaire) {
        this.log.info("Création du compte bancaire {}", compteBancaire.getNumeroDeCompte());
        final CompteBancaireEntity compteBancaireEntity = CompteBancaireEntity.from(compteBancaire);
        final CompteBancaireEntity result = this.compteBancaireJpaRepository.save(compteBancaireEntity);
        return CompteBancaire.toDomain(result.getId(), result.getNumeroDeCompte(), result.getSolde());
    }

    @Override
    public CompteBancaire findByNumeroDeCompte(String numeroDeCompte) {
        return this.compteBancaireJpaRepository.findByNumeroDeCompte(numeroDeCompte);
    }

    @Override
    public CompteBancaire mettreAJourCompteBancaire(CompteBancaire compteBancaire) {
        this.log.info("Mise à jour du compte bancaire {}", compteBancaire.getNumeroDeCompte());
        final CompteBancaireEntity compteBancaireEntity = CompteBancaireEntity.from(compteBancaire);
        final CompteBancaireEntity result = this.compteBancaireJpaRepository.save(compteBancaireEntity);
        return CompteBancaire.toDomain(result.getId(), result.getNumeroDeCompte(), result.getSolde());
    }

}
