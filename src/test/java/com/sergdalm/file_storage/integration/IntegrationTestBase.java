package com.sergdalm.file_storage.integration;

import com.sergdalm.file_storage.config.AmazonS3Configuration;
import com.sergdalm.file_storage.config.JacksonConfiguration;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@Import({AmazonS3Configuration.class, JacksonConfiguration.class})
@Sql({
        "classpath:sql/data.sql"
})
@WithMockUser(username = "test@gmail.com", password = "test", authorities = {"ADMIN"})
@ExtendWith(SpringExtension.class)
public abstract class IntegrationTestBase {

    private final static S3Mock api = new S3Mock.Builder().build();

    private static final MySQLContainer<?> container = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("file_storage");

    @BeforeAll
    static void runContainer() {
        container.start();
        api.start();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }

    @AfterAll
    static void shutDown() {
        api.shutdown();
    }
}
