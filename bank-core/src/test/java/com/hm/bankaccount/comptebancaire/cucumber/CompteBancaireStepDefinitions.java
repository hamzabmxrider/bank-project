package com.hm.bankaccount.comptebancaire.cucumber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hm.bankaccount.comptebancaire.application.usecases.CompteBancaireUseCases;
import com.hm.bankaccount.comptebancaire.infrastructure.adapter.rest.dto.CompteBancaireOperation;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class CompteBancaireStepDefinitions {

    private static final String BASE_PATH = "/api/v1/accounts";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @Autowired
    private CompteBancaireUseCases compteBancaireUseCases;

    private MvcResult currentResult;

    private CompteBancaireOperation expectedBody;

    @Before
    public void resetState() {
        currentResult = null;
        expectedBody = null;
    }

    @Given("Créer un compte bancaire avec un montant de {string}")
    public void creer_un_compte_bancaire_avec_un_montant_de(String montant) throws Exception {
        BigDecimal bigMontant = new BigDecimal(montant);
        var result = mockMvc.perform(post(BASE_PATH + "/" + bigMontant)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        expectedBody = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CompteBancaireOperation.class);
    }

    @When("j'effectue un dépôt sur ce compte avec un montant de {string}")
    public void j_effectue_un_depot_sur_ce_compte_avec_un_montant_de(String montant) throws Exception {
        currentResult = mockMvc.perform(post(BASE_PATH + "/depot/" + expectedBody.getNumeroDeCompte() + "/" + montant)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Then("la réponse HTTP doit être {int}")
    public void la_reponse_http_doit_etre(int expectedStatus) {
        assertThat(currentResult.getResponse().getStatus()).isEqualTo(expectedStatus);
    }

    @And("ce compte bancaire doit avoir un solde de {string}")
    public void ce_compte_bancaire_doit_avoir_un_solde_de(String solde) throws UnsupportedEncodingException, JsonProcessingException {
        CompteBancaireOperation response = objectMapper.readValue(
                currentResult.getResponse().getContentAsString(),
                CompteBancaireOperation.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getNumeroDeCompte()).isNotBlank();
        assertThat(response.getId()).isEqualTo(expectedBody.getId());
        assertThat(response.getNumeroDeCompte()).isEqualTo(expectedBody.getNumeroDeCompte());
        assertThat(response.getMontant()).isEqualByComparingTo(solde);

    }

    @When("j'effectue un retrait de ce compte avec le montant {string}")
    public void effectuer_retrait_avec_montant(String montant) throws Exception {
        currentResult = mockMvc.perform(post(BASE_PATH + "/retrait/" + expectedBody.getNumeroDeCompte() + "/" + montant)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

}
