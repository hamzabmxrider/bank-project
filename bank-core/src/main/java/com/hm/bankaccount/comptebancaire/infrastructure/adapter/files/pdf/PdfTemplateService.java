package com.hm.bankaccount.comptebancaire.infrastructure.adapter.files.pdf;

import com.hm.bankaccount.comptebancaire.application.out.FileGeneratorPort;
import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.CompteBancaire;
import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.LivretEpargne;
import com.hm.bankaccount.comptebancaire.domain.model.comptebancaire.operation.OperationEventType;
import com.hm.bankaccount.comptebancaire.infrastructure.adapter.files.pdf.model.CompteBancaireSnapshot;
import com.hm.bankaccount.comptebancaire.infrastructure.adapter.files.pdf.model.Transaction;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class PdfTemplateService implements FileGeneratorPort {

    private final SpringTemplateEngine templateEngine;

    @Override
    public byte[] generateReleveBancaire(CompteBancaire compteBancaire, final LocalDate dateDebut, final LocalDate dateFin) throws IOException {
        final CompteBancaireSnapshot compteBancaireSnapshot = new CompteBancaireSnapshot(
                compteBancaire.getNumeroDeCompte(),
                dateDebut,
                dateFin,
                compteBancaire.getSolde(),
                compteBancaire.getEvents()
                        .stream()
                        .map(e -> new Transaction(
                                e.date(),
                                e.message(),
                                e.type() == OperationEventType.RETRAIT_COMPTE_BANCAIRE ? e.montant() : new BigDecimal("0"),
                                e.type() == OperationEventType.DEPOT_COMPTE_BANCAIRE ? e.montant() : new BigDecimal("0"),
                                e.solde()
                        )).toList());
        Context context = new Context();
        context.setVariable("numeroDeCompte", compteBancaireSnapshot.numeroDeCompte());
        context.setVariable("events", compteBancaireSnapshot.events());
        context.setVariable("dateDebut", dateDebut);
        context.setVariable("dateFin", dateFin);
        context.setVariable("typeCompteBancaire", compteBancaire instanceof LivretEpargne ? "Livret d'épargne" : "Compte Courant");
        context.setVariable("solde", compteBancaireSnapshot.events().get(compteBancaireSnapshot.events().size() - 1).balance());

        String html = templateEngine.process("relevebancaire", context);

        PdfRendererBuilder builder = new PdfRendererBuilder();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        builder.withHtmlContent(html, null);
        builder.toStream(out);
        builder.run();

        return out.toByteArray();
    }
}
