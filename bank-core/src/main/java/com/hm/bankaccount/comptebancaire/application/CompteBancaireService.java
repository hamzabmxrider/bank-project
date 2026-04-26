package com.hm.bankaccount.comptebancaire.application;

import com.hm.bankaccount.comptebancaire.application.out.CompteBancaireRepositoryPort;
import com.hm.bankaccount.comptebancaire.application.out.CreditBancaireRepositoryPort;
import com.hm.bankaccount.comptebancaire.application.out.EventPublisherPort;
import com.hm.bankaccount.comptebancaire.application.usecases.CompteBancaireUseCases;
import com.hm.bankaccount.comptebancaire.domain.model.CompteBancaire;
import com.hm.bankaccount.comptebancaire.domain.model.DecouvertAutorise;
import com.hm.bankaccount.comptebancaire.domain.model.ProduitFinancier;
import com.hm.bankaccount.comptebancaire.domain.model.ProduitFinancierAggregator;
import com.hm.bankaccount.comptebancaire.infrastructure.adapter.repository.jpa.SeqJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

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
    @Transactional
    public CompteBancaire retrait(String numeroDeCompte, BigDecimal montant) {
        final CompteBancaire compteBancaire = this.compteBancaireRepositoryPort.findByNumeroDeCompte(numeroDeCompte);
        log.info("Opération de retrait depuis le compte {} d'un montant de {}", compteBancaire.getNumeroDeCompte(), montant);

        final Collection<ProduitFinancier> produitFinanciers = this.creditBancaireRepositoryPort.findByNumeroDeCompte(numeroDeCompte);
        final ProduitFinancierAggregator produitFinancierAggregator = ProduitFinancierAggregator.from(compteBancaire,
                List.of(new DecouvertAutorise(UUID.randomUUID(), new BigDecimal("5000"))));

        produitFinancierAggregator.retrait(montant);

        final CompteBancaire compteBancaireSauvegarde = this.compteBancaireRepositoryPort.mettreAJourCompteBancaire(compteBancaire);
        this.eventPublisherPort.publish(compteBancaireSauvegarde.getNumeroDeCompte(), compteBancaire.getEvents());
        return compteBancaireSauvegarde;
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
