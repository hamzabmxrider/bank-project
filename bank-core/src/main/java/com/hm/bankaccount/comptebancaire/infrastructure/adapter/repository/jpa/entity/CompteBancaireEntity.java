package com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa.entity;

import com.hm.bankaccount.comptebancaire.domain.model.CompteBancaire;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "compte_bancaire")
@Getter
public class CompteBancaireEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    UUID id;

    @Column(unique = true)
    private String numeroDeCompte;

    @Column(nullable = false)
    private BigDecimal solde;

    public CompteBancaireEntity(UUID id, String numeroDeCompte, BigDecimal solde) {
        this.id = id;
        this.numeroDeCompte = numeroDeCompte;
        this.solde = solde;
    }

    protected CompteBancaireEntity() {
        // RAF
    }

    public static CompteBancaireEntity from(CompteBancaire compteBancaire) {
        return new CompteBancaireEntity(
                null,
                compteBancaire.getNumeroDeCompte(),
                compteBancaire.getSolde());
    }
}
