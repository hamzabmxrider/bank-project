package com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa.entity;

import com.hm.bankaccount.comptebancaire.domain.model.DomainEvent;
import com.hm.bankaccount.comptebancaire.domain.model.DomainEventType;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "compte_bancaire_events")
public class CompteBancaireEventsEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    String numeroDeCompte;

    @Enumerated(EnumType.STRING)
    DomainEventType type;

    String message;

    public CompteBancaireEventsEntity(UUID id, String numeroDeCompte, DomainEventType type, String message) {
        this.id = id;
        this.numeroDeCompte = numeroDeCompte;
        this.type = type;
        this.message = message;
    }

    public CompteBancaireEventsEntity() {

    }

    public static CompteBancaireEventsEntity from(String numeroDeCompte, DomainEvent e) {
        return new CompteBancaireEventsEntity(null, numeroDeCompte, e.type(), e.message());
    }
}
