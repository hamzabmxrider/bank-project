package com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa;

import com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa.entity.CompteBancaireEventsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

@Repository
public interface EventJpaRepository extends JpaRepository<CompteBancaireEventsEntity, UUID> {

    Collection<CompteBancaireEventsEntity> findByNumeroDeCompteAndDateBetweenOrderByDateDesc(final String numeroDeCompte, final Instant dateDebut, Instant dateFin);

}
