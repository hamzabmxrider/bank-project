package com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa;

import com.hm.bankaccount.comptebancaire.application.out.EventPublisherPort;
import com.hm.bankaccount.comptebancaire.domain.model.DomainEvent;
import com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa.entity.CompteBancaireEventsEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@RequiredArgsConstructor
public class EventPublisherJpaRepository implements EventPublisherPort {

    private final EventJpaRepository eventPublisherJpaRepository;

    @Override
    public void publish(String numeroDeCompte, Collection<DomainEvent> domainEvent) {
        final Collection<CompteBancaireEventsEntity> compteBancaireEventsEntities = domainEvent
                .stream()
                .map(e -> CompteBancaireEventsEntity.from(numeroDeCompte, e))
                .toList();
        this.eventPublisherJpaRepository.saveAll(compteBancaireEventsEntities);
    }

}
