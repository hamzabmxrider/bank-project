package com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa;

import com.hm.bankaccount.comptebancaire.domain.model.CompteBancaire;
import com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa.entity.CompteBancaireEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface CompteBancaireJpaRepository extends JpaRepository<CompteBancaireEntity, UUID> {

    CompteBancaireEntity findByNumeroDeCompte(String numeroDeCompte);

    @Modifying
    @Query("UPDATE CompteBancaireEntity u SET u.numeroDeCompte = :numeroDeCompte, u.solde = :solde WHERE u.id = :id")
    void update(@Param("id") UUID id, @Param("numeroDeCompte") String numeroDeCompte, @Param("solde") BigDecimal solde);

}
