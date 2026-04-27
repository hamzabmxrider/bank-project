package com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa;

import com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa.entity.CreditBancaireEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface CreditBancaireJpaRepository extends JpaRepository<CreditBancaireEntity, UUID> {

    @Query("SELECT u FROM CreditBancaireEntity u WHERE u.compteBancaire.numeroDeCompte = :numeroDeCompte")
    Collection<CreditBancaireEntity> findByNumeroDeCompte(@Param("numeroDeCompte") String numeroDeCompte);
}
