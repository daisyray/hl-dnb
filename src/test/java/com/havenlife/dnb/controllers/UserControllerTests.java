package com.havenlife.dnb.controllers;

import com.havenlife.dnb.models.User;
import com.havenlife.dnb.models.WebResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebDriverScope;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {
    @Value("${local.server.port}")
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void faketest() {
        assertTrue(1 == 1);
    }

    @Test
    void createNewUserWorks() {
        try {
            String urlString = "http://localhost:" + port + "/create-user";
            URI uri = new URI(urlString);
            User user = new User();
            user.setFirstName(UUID.randomUUID().toString());
            user.setDateOfBirth(LocalDate.now().minusMonths(10));
            user.setSsn(UUID.randomUUID().toString());
            RequestEntity<User> requestEntity = RequestEntity.post(uri).contentType(MediaType.APPLICATION_JSON).body(user);
            ResponseEntity<WebResponse> response = restTemplate.exchange(requestEntity, WebResponse.class);
            assertNotNull(response);
            assertEquals(200, response.getStatusCode().value());
            WebResponse webResponse = response.getBody();
            assertNotNull(webResponse);
            assertNotNull(webResponse.getId());
            assertNull(webResponse.getErrors());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void errorMessageWhenEmailisEmpty() {
        try {
            String urlString = "http://localhost:" + port + "/create-user";
            URI uri = new URI(urlString);
            User user = new User();
            user.setFirstName(UUID.randomUUID().toString());
            user.setDateOfBirth(LocalDate.now().minusYears(20));
            user.setSsn(UUID.randomUUID().toString());
            RequestEntity<User> requestEntity = RequestEntity.post(uri).contentType(MediaType.APPLICATION_JSON).body(user);
            ResponseEntity<WebResponse> response = restTemplate.exchange(requestEntity, WebResponse.class);
            assertEquals(400, response.getStatusCode().value());
            WebResponse webResponse = response.getBody();
            assertNotNull(webResponse);
            assertNull(webResponse.getId());
            List<String> errors = webResponse.getErrors();
            assertNotNull(errors);
            assertEquals(1, errors.size());
            assertTrue(errors.contains("Email is missing"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
