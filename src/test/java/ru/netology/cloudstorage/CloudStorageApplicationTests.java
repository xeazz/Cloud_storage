package ru.netology.cloudstorage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.netology.cloudstorage.model.Request.AuthorizationRequest;
import ru.netology.cloudstorage.model.Response.AuthorizationResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class CloudServiceTApplicationTests {

    private static final int PORT_DB = 3306;
    private static final int PORT_SERVER = 8080;

    private static final String DATABASE_NAME = "Netology_diplom";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "root";

    private static final Network CLOUD_NETWORK = Network.newNetwork();
    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    public static MySQLContainer<?> databaseContainer = new MySQLContainer<>("mysql")
            .withNetwork(CLOUD_NETWORK)
            .withExposedPorts(PORT_DB)
            .withDatabaseName(DATABASE_NAME)
            .withUsername(DATABASE_USERNAME)
            .withPassword(DATABASE_PASSWORD);
    @Container
    public static GenericContainer<?> serverContainer = new GenericContainer<>("backend")
            .withNetwork(CLOUD_NETWORK)
            .withExposedPorts(PORT_SERVER)
            .withEnv(Map.of("SPRING_DATASOURCE_URL", "jdbc:mysql://localhost:" + PORT_DB + "/" + DATABASE_NAME))
            .dependsOn(databaseContainer);

    @Test
    void contextDatabase() {
        Assertions.assertTrue(databaseContainer.isRunning());
    }

    @Test
    void contextServer() {
        Assertions.assertFalse(serverContainer.isRunning());
    }

    @Test
    void testAuthorization() {
        try {
            URI loginUri = new URI("http://localhost:8080/login");
            AuthorizationRequest authorizationRequest = new AuthorizationRequest("admin@mail.ru", "admin");
            HttpEntity<AuthorizationRequest> requestLogin = new HttpEntity<>(authorizationRequest);
            ResponseEntity<AuthorizationResponse> loginResponse = restTemplate.postForEntity(loginUri, requestLogin, AuthorizationResponse.class);
            String token = loginResponse.getBody().getToken();
            Assertions.assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
            Assertions.assertNotNull(token);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}