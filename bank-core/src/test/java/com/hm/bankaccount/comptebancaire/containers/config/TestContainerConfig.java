package com.hm.bankaccount.comptebancaire.containers.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.MountableFile;

public class TestContainerConfig {

    @Container
    public static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15-bullseye")
                    .withDatabaseName("bank_account")
                    .withUsername("bank_account")
                    .withPassword("bank_account")
                    .withCopyFileToContainer(
                         // Used for host file path   MountableFile.forHostPath(
                            //Paths.get("postgresql/initdb").toAbsolutePath().toString() will constitute full path to bank-core/postgresql/initdb not resources

                            MountableFile.forClasspathResource("postgresql/initdb"),
                            "/docker-entrypoint-initdb.d"
                    );


    static {
        postgres.start();
    }

    @DynamicPropertySource
    static void datasourceProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

}
