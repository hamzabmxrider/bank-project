package com.hm.bankaccount.comptebancaire.domain.model;

import com.hm.bankaccount.comptebancaire.domain.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CompteBancaireTest {

    @Test
    void ouvrirUnCompteCourant_doit_creer_nouveau_compte_avec_event_vide() {
        CompteBancaire compteBancaire = CompteBancaire.ouvrirUnCompteCourant("ACC-001", new BigDecimal("125.50"));

        assertThat(compteBancaire.getId()).isNull();
        assertThat(compteBancaire.getNumeroDeCompte()).isEqualTo("ACC-001");
        assertThat(compteBancaire.getSolde()).isEqualByComparingTo("125.50");
        assertThat(compteBancaire.getEvents()).isEmpty();
    }

    @Test
    void toDomain_doit_reconstruire_objet_compte_bancaire() {
        UUID id = UUID.randomUUID();

        CompteBancaire compteBancaire = CompteBancaire.toDomain(id, "ACC-002", new BigDecimal("900.00"));

        assertThat(compteBancaire.getId()).isEqualTo(id);
        assertThat(compteBancaire.getNumeroDeCompte()).isEqualTo("ACC-002");
        assertThat(compteBancaire.getSolde()).isEqualByComparingTo("900.00");
        assertThat(compteBancaire.getEvents()).isEmpty();
    }

    @Test
    void depot_doit_alimenter_la_balance_et_produire_depot_event() {
        CompteBancaire compteBancaire = CompteBancaire.ouvrirUnCompteCourant("ACC-003", new BigDecimal("100.00"));

        compteBancaire.depot(new BigDecimal("50.25"));

        assertThat(compteBancaire.getSolde()).isEqualByComparingTo("150.25");
        assertThat(compteBancaire.getEvents()).hasSize(1);

        DomainEvent event = compteBancaire.getEvents().iterator().next();
        assertThat(event.type()).isEqualTo(DomainEventType.DEPOT_COMPTE_BANCAIRE);
        assertThat(event.message()).isEqualTo("Dépôt de 50.25 sur le compte ACC-003");
        assertThat(event.date()).isNotNull();
    }

    @Test
    void depot_doit_rejeter_montant_negatif() {
        CompteBancaire compteBancaire = CompteBancaire.ouvrirUnCompteCourant("ACC-004", new BigDecimal("100.00"));

        assertThatThrownBy(() -> compteBancaire.depot(new BigDecimal("-1")))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessage("Le montant du dépôt doit être supérieur à 0");
        assertThat(compteBancaire.getSolde()).isEqualByComparingTo("100.00");
        assertThat(compteBancaire.getEvents()).isEmpty();
    }

    @Test
    void retrait_doit_debiter_balance_et_creer_event_retrait() {
        CompteBancaire compteBancaire = CompteBancaire.ouvrirUnCompteCourant("ACC-005", new BigDecimal("100.00"));

        compteBancaire.retrait(new BigDecimal("40.00"));

        assertThat(compteBancaire.getSolde()).isEqualByComparingTo("60.00");
        assertThat(compteBancaire.getEvents()).hasSize(1);

        DomainEvent event = compteBancaire.getEvents().iterator().next();
        assertThat(event.type()).isEqualTo(DomainEventType.RETRAIT_COMPTE_BANCAIRE);
        assertThat(event.message()).isEqualTo("Retrait de 40.00 sur le compte ACC-005");
        assertThat(event.date()).isNotNull();
    }

    @Test
    void retrait_doit_rejeter_montant_negatif() {
        CompteBancaire compteBancaire = CompteBancaire.ouvrirUnCompteCourant("ACC-006", new BigDecimal("100.00"));

        assertThatThrownBy(() -> compteBancaire.retrait(new BigDecimal("-1")))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessage("Le montant du dépôt doit être supérieur à 0");
        assertThat(compteBancaire.getSolde()).isEqualByComparingTo("100.00");
        assertThat(compteBancaire.getEvents()).isEmpty();
    }

    @Test
    void retrait_doit_rejeter_retrait_avec_fonds_insuffisants_et_ne_rien_modifier() {
        CompteBancaire compteBancaire = CompteBancaire.ouvrirUnCompteCourant("ACC-007", new BigDecimal("25.00"));

        assertThatThrownBy(() -> compteBancaire.retrait(new BigDecimal("25.01")))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessage("Fonds insuffisants.");
        assertThat(compteBancaire.getSolde()).isEqualByComparingTo("25.00");
        assertThat(compteBancaire.getEvents()).isEmpty();
    }

    @Test
    void getEvents_doit_interdire_la_modification_de_la_liste_devents() {
        CompteBancaire compteBancaire = CompteBancaire.ouvrirUnCompteCourant("ACC-008", new BigDecimal("10.00"));

        assertThatThrownBy(() -> compteBancaire.getEvents().add(
                new DomainEvent(DomainEventType.DEPOT_COMPTE_BANCAIRE, "test", null)))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
