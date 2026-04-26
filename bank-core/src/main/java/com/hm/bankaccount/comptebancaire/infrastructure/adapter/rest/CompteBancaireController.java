package com.hm.bankaccount.comptebancaire.infrastructure.adapter.rest;

import com.hm.bankaccount.comptebancaire.application.usecases.CompteBancaireUseCases;
import com.hm.bankaccount.comptebancaire.domain.model.CompteBancaire;
import com.hm.bankaccount.comptebancaire.infrastructure.adapter.rest.dto.CompteBancaireOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class CompteBancaireController {

    private final CompteBancaireUseCases compteBancaireUseCases;

    @PostMapping("/{solde}")
    @ResponseStatus(HttpStatus.CREATED)
    public CompteBancaireOperation creerCompte(@PathVariable BigDecimal solde) {
        final CompteBancaire compteBancaire = this.compteBancaireUseCases.creerUnCompteBancaire(solde);
        return CompteBancaireOperation.from(compteBancaire);
    }

    @PostMapping("/depot/{numeroCompteBancaire}/{montant}")
    @ResponseStatus(HttpStatus.OK)
    public CompteBancaireOperation depot(@PathVariable String numeroCompteBancaire, @PathVariable BigDecimal montant) {
        final CompteBancaire compteBancaire = this.compteBancaireUseCases.depot(numeroCompteBancaire, montant);
        return CompteBancaireOperation.from(compteBancaire);
    }

    @PostMapping("/retrait/{numeroCompteBancaire}/{montant}")
    @ResponseStatus(HttpStatus.OK)
    public CompteBancaireOperation retrait(@PathVariable String numeroCompteBancaire, @PathVariable BigDecimal montant) {
        final CompteBancaire compteBancaire = this.compteBancaireUseCases.retrait(numeroCompteBancaire, montant);
        return CompteBancaireOperation.from(compteBancaire);
    }


}
