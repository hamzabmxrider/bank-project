package com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa.entity;

import com.hm.bankaccount.comptebancaire.domain.model.CreditBancaireType;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "credit_bancaire")
@Getter
public class CreditBancaireEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    UUID id;

    @Column(nullable = false)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    private CreditBancaireType type;

    @ManyToOne
    @JoinColumn(name = "numero_de_compte")
    private CompteBancaireEntity compteBancaire;

}
