package com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa.entity;

import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.operation.OperationEvent;
import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.operation.OperationEventType;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "compte_bancaire_events")
@Getter
public class CompteBancaireEventsEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    String numeroDeCompte;

    Instant date;

    BigDecimal solde;

    BigDecimal montant;

    @Enumerated(EnumType.STRING)
    OperationEventType type;

    String message;

    public CompteBancaireEventsEntity(UUID id, String numeroDeCompte, OperationEventType type, String message, BigDecimal montant, BigDecimal solde, Instant date) {
        this.id = id;
        this.numeroDeCompte = numeroDeCompte;
        this.type = type;
        this.message = message;
        this.date = date;
        this.montant = montant;
        this.solde = solde;
    }

    public CompteBancaireEventsEntity() {

    }

    public static CompteBancaireEventsEntity from(String numeroDeCompte, OperationEvent e) {
        return new CompteBancaireEventsEntity(null, numeroDeCompte, e.type(), e.message(), e.montant(), e.solde(), e.date());
    }
}
