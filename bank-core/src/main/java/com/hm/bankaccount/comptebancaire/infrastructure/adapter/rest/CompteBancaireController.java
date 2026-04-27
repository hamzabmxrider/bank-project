package com.hm.bankaccount.comptebancaire.infrastructure.adapter.rest;

import com.hm.bankaccount.comptebancaire.application.usecases.CompteBancaireUseCases;
import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.CompteBancaire;
import com.hm.bankaccount.comptebancaire.domain.model.produitfinancier.CreditBancaire;
import com.hm.bankaccount.comptebancaire.domain.model.produitfinancier.CreditBancaireType;
import com.hm.bankaccount.comptebancaire.infrastructure.adapter.rest.dto.CompteBancaireOperation;
import com.hm.bankaccount.comptebancaire.infrastructure.adapter.rest.dto.CreditBancaireOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class CompteBancaireController {

    private final CompteBancaireUseCases compteBancaireUseCases;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompteBancaireOperation creerCompte(@RequestParam BigDecimal solde) {
        final CompteBancaire compteBancaire = this.compteBancaireUseCases.creerUnCompteBancaire(solde);
        return CompteBancaireOperation.from(compteBancaire);
    }

    @GetMapping("/relevedecompte/{numeroCompteBancaire}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> releveDeCompteBancaire(
            @PathVariable String numeroCompteBancaire,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateDebut,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateFin) throws IOException {

        final byte[] pdf = this.compteBancaireUseCases.redigerReleveBancaire(numeroCompteBancaire, dateDebut, dateFin);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=releve_compte.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @PostMapping("/livretepargne")
    @ResponseStatus(HttpStatus.CREATED)
    public CompteBancaireOperation creerCompteLivretEpargne(@RequestParam BigDecimal solde) {
        final CompteBancaire compteBancaire = this.compteBancaireUseCases.creerUnLivretEpargne(solde);
        return CompteBancaireOperation.from(compteBancaire);
    }

    @PostMapping("/creditbancaire/decouvert/{numeroCompteBancaire}")
    @ResponseStatus(HttpStatus.CREATED)
    public Collection<CreditBancaireOperation> ajouterCreditBancaire(@PathVariable String numeroCompteBancaire, @RequestParam BigDecimal plafond) {
        final Collection<CreditBancaire> creditBancaires =
                this.compteBancaireUseCases.attacherCreditBancaire(
                        numeroCompteBancaire, CreditBancaireType.DECOUVERT, plafond);

        return creditBancaires
                .stream()
                .map(e ->
                        CreditBancaireOperation.from(numeroCompteBancaire, e))
                .toList();
    }

    @PostMapping("/depot/{numeroCompteBancaire}")
    @ResponseStatus(HttpStatus.OK)
    public CompteBancaireOperation depot(@PathVariable String numeroCompteBancaire, @RequestParam BigDecimal montant) {
        final CompteBancaire compteBancaire = this.compteBancaireUseCases.depot(numeroCompteBancaire, montant);
        return CompteBancaireOperation.from(compteBancaire);
    }

    @PostMapping("/retrait/{numeroCompteBancaire}")
    @ResponseStatus(HttpStatus.OK)
    public CompteBancaireOperation retrait(@PathVariable String numeroCompteBancaire, @RequestParam BigDecimal montant) {
        final CompteBancaire compteBancaire = this.compteBancaireUseCases.retrait(numeroCompteBancaire, montant);
        return CompteBancaireOperation.from(compteBancaire);
    }


}
