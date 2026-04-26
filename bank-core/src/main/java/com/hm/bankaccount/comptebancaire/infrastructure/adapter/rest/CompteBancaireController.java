package com.hm.bankaccount.comptebancaire.infrastructure.adapter.rest;

import com.hm.bankaccount.comptebancaire.application.usecases.CompteBancaireUseCases;
import com.hm.bankaccount.comptebancaire.domain.model.CompteBancaire;
import com.hm.bankaccount.comptebancaire.infrastructure.adapter.rest.dto.CompteBancaireOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class CompteBancaireController {

    private final CompteBancaireUseCases compteBancaireUseCases;

    @PostMapping("/{solde}")
    @ResponseStatus(HttpStatus.CREATED)
    public CompteBancaireOperation creerCompte(@PathVariable BigDecimal solde) {
        final CompteBancaire compteBancaire = this.compteBancaireUseCases.creerUnCompteBancaire(solde);
        return CompteBancaireOperation.from(compteBancaire);
    }

    @PostMapping("/depot")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CompteBancaireOperation depot(@Valid @RequestBody CompteBancaireOperation compteBancaireOperation) {
        final CompteBancaire compteBancaire = this.compteBancaireUseCases.depot(compteBancaireOperation.getNumeroDeCompte(), compteBancaireOperation.getMontant());
        return CompteBancaireOperation.from(compteBancaire);
    }

    @PostMapping("/retrait")
    @ResponseStatus(HttpStatus.CREATED)
    public CompteBancaireOperation retrait(@PathVariable CompteBancaireOperation compteBancaireOperation) {
        final CompteBancaire compteBancaire = this.compteBancaireUseCases.retrait(compteBancaireOperation.getNumeroDeCompte(), compteBancaireOperation.getMontant());
        return CompteBancaireOperation.from(compteBancaire);
    }


}
