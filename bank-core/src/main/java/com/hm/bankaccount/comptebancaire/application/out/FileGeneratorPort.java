package com.hm.bankaccount.comptebancaire.application.out;

import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.CompteBancaire;

import java.io.IOException;
import java.time.LocalDate;

public interface FileGeneratorPort {
    byte[] generateReleveBancaire(CompteBancaire compteBancaire, LocalDate dateDebut, LocalDate dateFin) throws IOException;
}
