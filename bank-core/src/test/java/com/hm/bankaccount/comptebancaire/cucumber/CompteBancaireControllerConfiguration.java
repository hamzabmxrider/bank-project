package com.hm.bankaccount.comptebancaire.cucumber;

import com.hm.bankaccount.comptebancaire.containers.config.TestContainerConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
public class CompteBancaireControllerConfiguration extends TestContainerConfig {
}
