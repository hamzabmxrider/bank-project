package com.hm.bankaccount.comptebancaire.application;

import com.hm.bankaccount.comptebancaire.application.out.CompteBancaireRepositoryPort;
import com.hm.bankaccount.comptebancaire.application.out.CreditBancaireRepositoryPort;
import com.hm.bankaccount.comptebancaire.application.out.EventPublisherPort;
import com.hm.bankaccount.comptebancaire.application.usecases.CompteBancaireUseCases;
import com.hm.bankaccount.comptebancaire.domain.model.*;
import com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa.SeqJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompteBancaireService implements CompteBancaireUseCases {

    private final EventPublisherPort eventPublisherPort;

    private final SeqJpaRepository seqJpaRepository;

    private final CompteBancaireRepositoryPort compteBancaireRepositoryPort;
    private final CreditBancaireRepositoryPort creditBancaireRepositoryPort;

    @Override
    @Transactional
    public CompteBancaire creerUnCompteBancaire(BigDecimal solde) {
        final String numeroCompteBancaire = this.seqJpaRepository.nextAccountNumber();
        final CompteBancaire compteBancaire = CompteBancaire.ouvrirUnCompteCourant(numeroCompteBancaire, solde);
        final CompteBancaire compteBancaireSauvegarde = this.compteBancaireRepositoryPort.persisterCompteBancaire(compteBancaire);
        this.eventPublisherPort.publish(compteBancaire.getNumeroDeCompte(), compteBancaire.getEvents());
        return compteBancaireSauvegarde;
    }

    @Override
    public CompteBancaire creerUnLivretEpargne(BigDecimal solde) {
        final String numeroCompteBancaire = this.seqJpaRepository.nextAccountNumber();
        final LivretEpargne compteBancaire = LivretEpargne.ouvrirUnLivretEpargne(numeroCompteBancaire, solde);
        final CompteBancaire compteBancaireSauvegarde = this.compteBancaireRepositoryPort.persisterCompteBancaire(compteBancaire);
        this.eventPublisherPort.publish(compteBancaire.getNumeroDeCompte(), compteBancaire.getEvents());
        return compteBancaireSauvegarde;
    }

    @Override
    @Transactional
    public CompteBancaire retrait(String numeroDeCompte, BigDecimal montant) {
        final CompteBancaire compteBancaire = this.compteBancaireRepositoryPort.findByNumeroDeCompte(numeroDeCompte);
        log.info("Opération de retrait depuis le compte {} d'un montant de {}", compteBancaire.getNumeroDeCompte(), montant);

        final Collection<ProduitFinancier> produitFinanciers = this.creditBancaireRepositoryPort.findByNumeroDeCompte(numeroDeCompte);
        final ProduitFinancierAggregator produitFinancierAggregator = ProduitFinancierAggregator.from(compteBancaire, produitFinanciers);

        produitFinancierAggregator.retrait(montant);

        final CompteBancaire compteBancaireSauvegarde = this.compteBancaireRepositoryPort.mettreAJourCompteBancaire(compteBancaire);
        this.eventPublisherPort.publish(compteBancaireSauvegarde.getNumeroDeCompte(), compteBancaire.getEvents());
        return compteBancaireSauvegarde;
    }

    @Override
    public Collection<CreditBancaire> attacherCreditBancaire(String numeroCompteBancaire, CreditBancaireType creditBancaireType, BigDecimal actif) {
        final CompteBancaire compteBancaire = this.compteBancaireRepositoryPort.findByNumeroDeCompte(numeroCompteBancaire);
        log.info("Opération de rattachement d'un crédit bancaire au compte {} de type {}", compteBancaire.getNumeroDeCompte(), creditBancaireType);

        final Collection<ProduitFinancier> produitFinanciers = this.creditBancaireRepositoryPort.findByNumeroDeCompte(numeroCompteBancaire);
        final ProduitFinancierAggregator produitFinancierAggregator = ProduitFinancierAggregator.from(compteBancaire, produitFinanciers);
        produitFinancierAggregator.attacherCreditBancaire(creditBancaireType, actif);

        final Collection<CreditBancaire> creditBancaires = this.creditBancaireRepositoryPort.creerCreditBancaire(numeroCompteBancaire, produitFinancierAggregator.getCreditsBancaires());
        this.eventPublisherPort.publish(compteBancaire.getNumeroDeCompte(), compteBancaire.getEvents());

        return creditBancaires;
    }

    @Override
    @Transactional
    public CompteBancaire depot(String numeroDeCompte, BigDecimal montant) {
        final CompteBancaire compteBancaire = this.compteBancaireRepositoryPort.findByNumeroDeCompte(numeroDeCompte);
        log.info("Opération de dépôt dans le compte {} d'un montant de {}", compteBancaire.getNumeroDeCompte(), montant);

        compteBancaire.depot(montant);

        final CompteBancaire compteBancaireSauvegarde = this.compteBancaireRepositoryPort.mettreAJourCompteBancaire(compteBancaire);
        this.eventPublisherPort.publish(compteBancaireSauvegarde.getNumeroDeCompte(), compteBancaire.getEvents());

        return compteBancaireSauvegarde;
    }
}
