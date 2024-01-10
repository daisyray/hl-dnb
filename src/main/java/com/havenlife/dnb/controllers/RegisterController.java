package com.havenlife.dnb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.havenlife.dnb.models.Register;
import com.havenlife.dnb.service.FMUtils;
import com.havenlife.dnb.service.PasswordResetService;
import com.havenlife.dnb.service.RegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.havenlife.dnb.service.ResetLinkResult;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    RegisterService registerService;
    @Autowired
    PasswordResetService passwordResetService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private FMUtils fm;
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);
    @PostMapping(value = "/new-registration", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object > register(@RequestBody Register register) {
        logger.debug("creating new-registration {}", register);
        boolean ok = registerService.validate(register);
        if(!ok) {
            return ResponseEntity.status(406).body("Validation failed");
        }
        Register returnRegisterObj = registerService.newRegister(register);
        return ResponseEntity.ok().body(returnRegisterObj);
    }

    @PostMapping(value = "/forgot-password", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> forgotPassword(@RequestParam (value = "email", required = true) String email){
        ResetLinkResult resetLinkResult = passwordResetService.createResetPasswordLink(email);
        if (resetLinkResult.getErrorMsg() != null) {
            return ResponseEntity.badRequest().body(resetLinkResult.getErrorMsg());
        }
        Integer id = resetLinkResult.getId();
        String resetLink = "http://localhost:8080/register/new-password/" + id;
        return ResponseEntity.ok().body(resetLink);
    }
    @GetMapping(value = "/new-password/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> newPassword(@PathVariable ("id") Integer id) {
        String email = passwordResetService.getEmailPswdEmail(id);
        if( email== null ) {
            return ResponseEntity.badRequest().body("Invalid id");
        }
        String html = fm.render("/register/reset-password.ftl", Map.of("email", email));
        return ResponseEntity.ok(html);
    }
    @PostMapping(value = "/new-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> newPassword(@RequestParam (value = "email") String email ,
                                              @RequestParam (value = "password") String password,
                                              @RequestParam (value = "confirm-password") String confirmPassword){
        String changePswdResp = passwordResetService.changePassword(email, password, confirmPassword);
        if (changePswdResp != null) {
            return ResponseEntity.badRequest().body(changePswdResp);
        }
        return ResponseEntity.ok("Your new password has been updated");
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
