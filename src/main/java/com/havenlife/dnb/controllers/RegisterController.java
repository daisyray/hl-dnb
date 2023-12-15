package com.havenlife.dnb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.havenlife.dnb.models.Register;
import com.havenlife.dnb.models.WebResponse;
import com.havenlife.dnb.service.RegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    RegisterService registerService;
    @Autowired
    ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);
    @PostMapping(value = "/new-registration", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object > register(@RequestBody Register register) {
        logger.debug("creating new-registration {}", register);
        boolean ok = registerService.validate(register);
        if(!ok) {
            return ResponseEntity.status(406).body("Validation failed");
        }
        Register returnRegisterObj = registerService.newRegister(register);
        return ResponseEntity.ok(returnRegisterObj);
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public String registeredList (@RequestParam (value = "limit", required = false, defaultValue = "10") Integer maxRow) {
        // validate param
        List<Register> registeredList = registerService.registerList(maxRow);
        try {
            return objectMapper.writeValueAsString(registeredList);
        } catch (Exception e) {
            return "Error converting to json";
        }
    }
    @DeleteMapping(value = "/delete-registration", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteRegistration(@RequestParam (value = "id", required = true) Integer id) {
        if (id < 0) {
            return ResponseEntity.badRequest().body("Id not found");
        }
        String deleteErrorMessage = registerService.deleteRegistration(id);
        if(deleteErrorMessage != null) {
            return ResponseEntity.badRequest().body(deleteErrorMessage);
        } else {
            return ResponseEntity.ok("deleted");
        }
    }

    @PutMapping(value = "/update-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updatePassword(@RequestParam(value="id", required = true) Integer id,
                                   @RequestParam(value = "password", required = true) String password) {
        if ( id < 0) {
            return null;
        }
        if(password.isBlank()) {
            return ResponseEntity.status(406).body("validation failed");
        }
        String errorMsg = registerService.updatePassword(id, password);
        if(errorMsg == null) {
            return ResponseEntity.ok("updated");
        } else {
            return ResponseEntity.badRequest().body(errorMsg);
        }
    }

}
