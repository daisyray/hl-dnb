package com.havenlife.dnb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.havenlife.dnb.database.FileFunctions;
import com.havenlife.dnb.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserService userService;
    @Autowired
    private FileFunctions fileFunctions;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void checkMissingFirstName() {
        User user = new User();
        user.setFirstName(null);
        List<String> errors = userService.validate(user);
        assertFalse(errors.isEmpty());
    }
    @Test
    void checkBlankEmail() {
        User user = new User();
        List<String> errors = userService.validate(user);
        assertFalse(errors.isEmpty());
    }
    @Test
    void checkNullEmail() {
        User user = new User();
        List<String> errors = userService.validate(user);
        assertFalse(errors.isEmpty());
    }
    @Test
    void checkCreateUser() {
        User user = new User();
        user.setFirstName("testda");
        user.setDateOfBirth(LocalDate.of(1999,1,2));
        user.setSsn(UUID.randomUUID().toString());
        Integer id = userService.createUser(user);
        assertNotNull(id);
        List<String> usersInLine = fileFunctions.readFromFile("user.txt", -1);
        assertFalse(usersInLine.isEmpty());
        int lastIndex = usersInLine.size()-1;

        String lastUserString = usersInLine.get(lastIndex);
        try {
            User lastUserObj = objectMapper.readValue(lastUserString, User.class);
            assertEquals(id, lastUserObj.getId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void readUserTest() {

    }
}
