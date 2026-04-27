package com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa;

import com.hm.bankaccount.comptebancaire.application.out.EventPublisherPort;
import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.operation.OperationEvent;
import com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa.entity.CompteBancaireEventsEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collection;

@Repository
@RequiredArgsConstructor
public class EventPublisherJpaRepository implements EventPublisherPort {

    private final EventJpaRepository eventPublisherJpaRepository;

    @Override
    public void publish(String numeroDeCompte, Collection<OperationEvent> operationEvent) {
        final Collection<CompteBancaireEventsEntity> compteBancaireEventsEntities = operationEvent
                .stream()
                .map(e -> CompteBancaireEventsEntity.from(numeroDeCompte, e))
                .toList();
        this.eventPublisherJpaRepository.saveAll(compteBancaireEventsEntities);
    }

    @Override
    public Collection<OperationEvent> findByNumeroDeCompte(String numeroCompteBancaire, final LocalDate dateDebut, final LocalDate dateFin) {
        Instant debutInstant = dateDebut
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant();

        Instant finInstant = dateFin
                .plusDays(1)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant();
        return this.eventPublisherJpaRepository.findByNumeroDeCompteAndDateBetweenOrderByDateDesc(
                        numeroCompteBancaire, debutInstant, finInstant)
                .stream()
                .map(e -> new OperationEvent(
                        e.getNumeroDeCompte(),
                        e.getType(),
                        e.getMontant(),
                        e.getSolde(),
                        e.getMessage(),
                        e.getDate()
                )).toList();
    }

}
