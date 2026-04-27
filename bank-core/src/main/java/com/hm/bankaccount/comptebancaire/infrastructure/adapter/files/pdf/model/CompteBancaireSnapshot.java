package com.hm.bankaccount.comptebancaire.infrastructure.adapter.files.pdf.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CompteBancaireSnapshot(String numeroDeCompte, LocalDate dateDebut, LocalDate dateFin, BigDecimal solde,
                                     List<Transaction> events) {
}
