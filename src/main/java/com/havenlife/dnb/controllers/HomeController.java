package com.havenlife.dnb.controllers;

import com.havenlife.dnb.models.User;
import com.havenlife.dnb.service.FMUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/home")
@RestController
public class HomeController {
    @Autowired
    private FMUtils fm;

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String home() {
        var user = new User();
        user.setFirstName("John");
        user.setLastName("Faker");
        user.setId(1000);
        return fm.render("/home.ftl", Map.of("user", user));
    }
}
