package com.havenlife.dnb.controllers;

import com.havenlife.dnb.models.Application;
import com.havenlife.dnb.models.Policy;
import net.datafaker.Faker;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

public class PolicyController {
    @GetMapping(value = "/policies", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Policy> policies(@RequestParam(value = "rowsLimit", required = false) Integer limit) {
        List<Policy> allPolicies = new ArrayList<>();

        if (limit == null || limit < 1) {
            System.out.println("Limit value is null");
            return allPolicies;
        } else if (limit > 1000) {
            limit = 10;
        }
        Faker faker = new Faker();
        for(int i= 0; i <= limit; i++) {

        }

        return allPolicies;
    }

}
