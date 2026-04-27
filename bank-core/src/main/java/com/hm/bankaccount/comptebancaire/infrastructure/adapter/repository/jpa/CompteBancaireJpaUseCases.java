package com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa;

import com.hm.bankaccount.comptebancaire.application.out.CompteBancaireRepositoryPort;
import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.CompteBancaire;
import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.CompteBancaireFactory;
import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.operation.OperationEvent;
import com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa.entity.CompteBancaireEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CompteBancaireJpaUseCases implements CompteBancaireRepositoryPort {

    private final CompteBancaireJpaRepository compteBancaireJpaRepository;

    private final EventPublisherJpaRepository eventPublisherJpaRepository;

    @Override
    public CompteBancaire persisterCompteBancaire(CompteBancaire compteBancaire) {
        log.info("Création du compte bancaire {}", compteBancaire.getNumeroDeCompte());
        final CompteBancaireEntity compteBancaireEntity = CompteBancaireEntity.from(compteBancaire);
        final CompteBancaireEntity result = this.compteBancaireJpaRepository.save(compteBancaireEntity);
        return CompteBancaireFactory.toDomain(result.getId(), result.getType(), result.getNumeroDeCompte(), result.getSolde(), new ArrayList<>());
    }

    @Override
    public CompteBancaire findByNumeroDeCompte(String numeroDeCompte, LocalDate dateDebut, LocalDate dateFin) {
        final Collection<OperationEvent> operationEvents = this.eventPublisherJpaRepository.findByNumeroDeCompte(numeroDeCompte, dateDebut, dateFin);
        CompteBancaireEntity compteBancaireEntity = this.compteBancaireJpaRepository
                .findByNumeroDeCompte(numeroDeCompte)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Le compte numéro %s est inexistant.", numeroDeCompte)));
        return CompteBancaireFactory.toDomain(
                compteBancaireEntity.getId(),
                compteBancaireEntity.getType(),
                compteBancaireEntity.getNumeroDeCompte(),
                compteBancaireEntity.getSolde(),
                operationEvents);
    }

    @Override
    public CompteBancaire findByNumeroDeCompte(String numeroDeCompte) {
        CompteBancaireEntity compteBancaireEntity = this.compteBancaireJpaRepository
                .findByNumeroDeCompte(numeroDeCompte)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Le compte numéro %s est inexistant.", numeroDeCompte)));
        // A remplacer par une factory
        return CompteBancaireFactory.toDomain(
                compteBancaireEntity.getId(),
                compteBancaireEntity.getType(),
                compteBancaireEntity.getNumeroDeCompte(),
                compteBancaireEntity.getSolde(),
                new ArrayList<>());
    }

    @Override
    public CompteBancaire mettreAJourCompteBancaire(CompteBancaire compteBancaire) {
        log.info("Mise à jour du compte bancaire {}", compteBancaire.getNumeroDeCompte());
        this.compteBancaireJpaRepository.update(compteBancaire.getId(), compteBancaire.getNumeroDeCompte(), compteBancaire.getSolde());
        return compteBancaire;
    }

}
