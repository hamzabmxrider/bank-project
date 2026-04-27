package com.hm.bankaccount.comptebancaire.domain.model.produitfinancier;

import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.CompteBancaire;
import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.operation.OperationEvent;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.stream.Collectors;

public class ProduitFinancierAggregator {

    private CompteBancaire compteBancaire;

    private Collection<ProduitFinancier> produitsFinanciers;

    public ProduitFinancierAggregator(CompteBancaire compteBancaire, Collection<ProduitFinancier> produitsFinanciers) {
        this.compteBancaire = compteBancaire;
        this.produitsFinanciers = produitsFinanciers;
    }

    public static ProduitFinancierAggregator from(CompteBancaire compteBancaire, Collection<ProduitFinancier> produitFinanciers) {
        return new ProduitFinancierAggregator(compteBancaire, produitFinanciers);
    }

    public void validerTransaction(Collection<OperationEvent> operationEvents) {
        produitsFinanciers.forEach(e ->
                operationEvents.forEach(elt ->
                        e.verifierEligibilite(compteBancaire, elt)));
    }

    public void retrait(BigDecimal montant) {
        compteBancaire.retrait(montant, !produitsFinanciers.isEmpty());
        this.validerTransaction(compteBancaire.getEvents());
    }

    public void attacherCreditBancaire(CreditBancaireType creditBancaireType, BigDecimal montant) {
        // Possibilité d'utiliser une fabrique ..
        if (creditBancaireType == CreditBancaireType.DECOUVERT) {
            final DecouvertAutorise decouvertAutorise = new DecouvertAutorise(null, montant);
            compteBancaire.verifierCompatibiliteCreditBancaire(CreditBancaireType.DECOUVERT);
            produitsFinanciers.add(decouvertAutorise);
            compteBancaire.attacherProduitFinancier(decouvertAutorise);
        }
    }

    public Collection<CreditBancaire> getCreditsBancaires() {
        return produitsFinanciers.stream()
                .filter(e -> e instanceof CreditBancaire)
                .map(e -> (CreditBancaire) e)
                .collect(Collectors.toUnmodifiableList());
    }
}
