package com.hm.bankaccount.comptebancaire.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@Import(CompteBancaireControllerConfiguration.CompteBancaireUseCasesTestConfig.class)
public class CompteBancaireControllerConfiguration {

    @TestConfiguration
    static class CompteBancaireUseCasesTestConfig {

    }
}
