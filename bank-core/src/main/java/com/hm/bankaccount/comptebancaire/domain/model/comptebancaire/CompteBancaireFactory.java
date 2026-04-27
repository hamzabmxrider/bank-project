package com.hm.bankaccount.comptebancaire.domain.model.comptebancaire;

import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.operation.OperationEvent;
import org.hibernate.UnknownEntityTypeException;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

public class CompteBancaireFactory {

    public static CompteBancaire toDomain(
            UUID id,
            TypeCompteBancaire type,
            String accountNumber,
            BigDecimal solde,
            Collection<OperationEvent> events
    ) {
        return switch (type) {
            case COMPTE_COURANT ->
                    new CompteBancaire(id, TypeCompteBancaire.COMPTE_COURANT, accountNumber, solde, events);
            case COMPTE_EPARGNE ->
                    new LivretEpargne(id, TypeCompteBancaire.COMPTE_EPARGNE, accountNumber, solde, events);
            default -> throw new UnknownEntityTypeException(String.format("Le type %s est inconnu", type));
        };
    }
}
