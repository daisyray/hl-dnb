package com.havenlife.dnb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.havenlife.dnb.database.FileFunctions;
import com.havenlife.dnb.models.User;
import com.havenlife.dnb.models.WebResponse;
import com.havenlife.dnb.service.FMUtils;
import com.havenlife.dnb.service.UserService;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("/user")
@RestController
public class UsersController {
    @Autowired
    private FileFunctions fileFunctions;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private FMUtils fm;

    @PostMapping(value = "/create-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse> createUser(@RequestBody User userBody) {
        List<String> errors = userService.validate(userBody);

        if(!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(new WebResponse(errors));
        }
        int id = userService.createUserDB(userBody);
        WebResponse resp = new WebResponse(id);
        System.out.println("Returning ");
        return ResponseEntity.ok(resp);
    }

    @GetMapping(value = "/get-users", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsers(@RequestParam(value = "numberOfUsers", required = false) Integer numberOfUsers,
                               @RequestParam(value = "orderBy", required = false) String columnName) {
        return userService.readUsersFromDB(numberOfUsers, columnName);
    }

    @GetMapping(value = "/xxx", produces = MediaType.TEXT_PLAIN_VALUE)
    public String  getUsers() {
        Set<String> testSet = new TreeSet<>();
        Random rand = new Random();
        for(int i=0; i<11; i++) {
            String toPrint = rand.nextInt(100) + "test";
            testSet.add( toPrint );
            System.out.println(toPrint);
        }

        String setInString = "";
        for ( String s: testSet) {
            setInString = setInString+s+"," ;
            System.out.println(s);
        }

        return setInString;
    }
    @DeleteMapping(value = "/delete-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteUser(@RequestParam(value = "id") Integer id) {
        String deleteErrorMsg = userService.deleteUserFromDb(id);
        if (deleteErrorMsg == null) {
            return ResponseEntity.ok("");
        }
        return ResponseEntity.badRequest().body(deleteErrorMsg);
    }
    @PutMapping(value = "/update-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateUser(@RequestBody User userBody) {
         String updateDbResponse = userService.updateUserDB(userBody);
         if(updateDbResponse != null) {
             return ResponseEntity.ok("");
         }
         return ResponseEntity.badRequest().body(updateDbResponse);
    }

    //give an id, get the user
    @GetMapping(value = "/get-user-id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUser(@PathVariable(value = "id") Integer id) {
        return userService.getUserWithId(id);
    }
    @GetMapping(value="/get-user-name/{fName}/{lastName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUserName(@PathVariable(value = "fName") String firstName,
                            @PathVariable(value = "lastName", required = false) String lastName) {
        List<User> rows =  userService.getUserByName(firstName, lastName);
        return rows;
    }

    @GetMapping(value = "/login-screen", produces = MediaType.TEXT_HTML_VALUE)
    public String loginScreen() {
        return fm.render("/login/login.ftl");
    }

    @PostMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> loginNow(
        @RequestParam("email") String email,
        @RequestParam("password") String password
    ) {
        var errors = new HashMap<String,String>();
        if (!StringUtils.hasText(email)) {
            errors.put("email", "Email is missing");
        }
        if (!StringUtils.hasText(password)) {
            errors.put("password", "Password is missing");
        }
        if (errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.SEE_OTHER)
                    .header(HttpHeaders.LOCATION, "/home/").build();
        } else {
            var content = fm.render("/login/login.ftl", Map.of(
                "email", email,
                "password", password,
                "errors", errors
            ));
            return ResponseEntity.badRequest().body(content);
        }
    }

    @GetMapping(value = "/logout", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> logout() {
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .header(HttpHeaders.LOCATION, "/user/login-screen").build();
    }

    @PostMapping(value = "/forgot-password", produces = MediaType.TEXT_HTML_VALUE)
    public String forgotPasswordScreen(@RequestParam("email") String email) {
        if (!StringUtils.hasText(email)) {
            return fm.render("/login/login.ftl", Map.of("errors", Map.of("email", "Missing Email")));
        }
        return fm.render("/login/login.ftl", Map.of("recovery", "true", "email", email));
    }
}
// add a delete controller