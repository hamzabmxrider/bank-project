package com.hm.bankaccount.comptebancaire.application;

import com.hm.bankaccount.comptebancaire.application.out.CompteBancaireRepositoryPort;
import com.hm.bankaccount.comptebancaire.application.usecases.CompteBancaireUseCases;
import com.hm.bankaccount.comptebancaire.application.out.EventPublisherPort;
import com.hm.bankaccount.comptebancaire.domain.model.CompteBancaire;
import com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa.SeqJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompteBancaireService implements CompteBancaireUseCases {

    private final EventPublisherPort eventPublisherPort;

    private final SeqJpaRepository seqJpaRepository;

    private final CompteBancaireRepositoryPort compteBancaireRepositoryPort;

    @Override
    public CompteBancaire creerUnCompteBancaire(BigDecimal solde) {
        final String numeroCompteBancaire = this.seqJpaRepository.nextAccountNumber();
        final CompteBancaire compteBancaire = CompteBancaire.ouvrirUnCompteCourant(numeroCompteBancaire, solde);
        final CompteBancaire compteBancaireSauvegarde = this.compteBancaireRepositoryPort.persisterCompteBancaire(compteBancaire);
        this.eventPublisherPort.publish(compteBancaire.getNumeroDeCompte(), compteBancaire.getEvents());
        return compteBancaireSauvegarde;
    }

    @Override
    public CompteBancaire retrait(String numeroDeCompte,  BigDecimal montant) {
        final CompteBancaire compteBancaire = this.compteBancaireRepositoryPort.findByNumeroDeCompte(numeroDeCompte);
        log.info("Opération de retrait depuis le compte {} d'un montant de {}", compteBancaire.getNumeroDeCompte(), montant);
        compteBancaire.retrait(montant);
        final CompteBancaire compteBancaireSauvegarde = this.compteBancaireRepositoryPort.mettreAJourCompteBancaire(compteBancaire);
        this.eventPublisherPort.publish(compteBancaireSauvegarde.getNumeroDeCompte(), compteBancaire.getEvents());
        return compteBancaireSauvegarde;
    }

    @Override
    public CompteBancaire depot(String numeroDeCompte, BigDecimal montant) {
        final CompteBancaire compteBancaire = this.compteBancaireRepositoryPort.findByNumeroDeCompte(numeroDeCompte);
        log.info("Opération de dépôt dans le compte {} d'un montant de {}", compteBancaire.getNumeroDeCompte(), montant);
        compteBancaire.depot(montant);
        final CompteBancaire compteBancaireSauvegarde = this.compteBancaireRepositoryPort.mettreAJourCompteBancaire(compteBancaire);
        this.eventPublisherPort.publish(compteBancaireSauvegarde.getNumeroDeCompte(), compteBancaire.getEvents());
        return compteBancaireSauvegarde;
    }
}
