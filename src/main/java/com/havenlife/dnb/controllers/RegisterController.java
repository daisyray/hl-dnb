package com.havenlife.dnb.controllers;

import com.havenlife.dnb.models.Register;
import com.havenlife.dnb.models.WebResponse;
import com.havenlife.dnb.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {
    @Autowired
    RegisterService registerService;
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String > register(@RequestBody Register register) {
        registerService.validate(register);
        registerService.newRegister(register);
        return ResponseEntity.ok("");
    }

}
