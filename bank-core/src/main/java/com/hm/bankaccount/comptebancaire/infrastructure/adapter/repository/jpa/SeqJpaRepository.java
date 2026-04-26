package com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

@Component
public class SeqJpaRepository {


    @PersistenceContext
    private EntityManager entityManager;

    public String nextAccountNumber() {
        return entityManager.createNativeQuery("SELECT nextval('compte_bancaire_seq')::text")
                .getSingleResult()
                .toString();
    }
}