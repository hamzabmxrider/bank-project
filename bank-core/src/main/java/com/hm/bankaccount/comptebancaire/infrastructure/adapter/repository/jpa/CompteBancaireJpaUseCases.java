package com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa;

import com.hm.bankaccount.comptebancaire.application.out.CompteBancaireRepositoryPort;
import com.hm.bankaccount.comptebancaire.domain.model.CompteBancaire;
import com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa.entity.CompteBancaireEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CompteBancaireJpaUseCases implements CompteBancaireRepositoryPort {

    private final CompteBancaireJpaRepository compteBancaireJpaRepository;

    @Override
    public CompteBancaire persisterCompteBancaire(CompteBancaire compteBancaire) {
        log.info("Création du compte bancaire {}", compteBancaire.getNumeroDeCompte());
        final CompteBancaireEntity compteBancaireEntity = CompteBancaireEntity.from(compteBancaire);
        final CompteBancaireEntity result = this.compteBancaireJpaRepository.save(compteBancaireEntity);
        return CompteBancaire.toDomain(result.getId(), result.getNumeroDeCompte(), result.getSolde());
    }

    @Override
    public CompteBancaire findByNumeroDeCompte(String numeroDeCompte) {
        CompteBancaireEntity compteBancaireEntity = this.compteBancaireJpaRepository
                .findByNumeroDeCompte(numeroDeCompte)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Le compte numéro %s est inexistant.", numeroDeCompte)));
        return CompteBancaire.toDomain(compteBancaireEntity.getId(), compteBancaireEntity.getNumeroDeCompte(), compteBancaireEntity.getSolde());
    }

    @Override
    public CompteBancaire mettreAJourCompteBancaire(CompteBancaire compteBancaire) {
        log.info("Mise à jour du compte bancaire {}", compteBancaire.getNumeroDeCompte());
        this.compteBancaireJpaRepository.update(compteBancaire.getId(), compteBancaire.getNumeroDeCompte(), compteBancaire.getSolde());
        return compteBancaire;
    }

}
