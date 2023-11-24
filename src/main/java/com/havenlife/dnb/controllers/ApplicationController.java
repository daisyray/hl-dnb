package com.havenlife.dnb.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.havenlife.dnb.database.FileFunctions;
import com.havenlife.dnb.models.Application;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class ApplicationController {
    @Value("${applications.store.file}")
    private String storeFile;

    @Value("${rows.limit.max.value}")
    private Integer displaycallLimits;

    @Autowired
    private FileFunctions fileFunctions;

    @Autowired
    private ObjectMapper objectMapper;
    public ApplicationController() {
    }
    @GetMapping(value = "/list-applications", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Application> application(@RequestParam(value = "rowsLimit", required = false) Integer rowsLimit) {
        List<Application> applications = new ArrayList<>();
        if (rowsLimit == null || rowsLimit < 1) {
            System.out.println("the value of callLimit " + rowsLimit);
            System.out.println("callLimit value is null");
            return applications;
        } else if (rowsLimit > displaycallLimits) {
            rowsLimit = displaycallLimits;
        }
        List<String> readContent = fileFunctions.readFromFile(storeFile, rowsLimit);
        objectMapper.registerModule(new JavaTimeModule());

        for (int i = 0; i < readContent.size(); i++) {
            String oneLine = readContent.get(i);
            try {
                Application app = objectMapper.readValue(oneLine, Application.class);
                applications.add(app);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return applications;
    }

    @GetMapping(value = "/get-application/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Application> getApplication(@PathVariable (value = "applicationId") Integer id) throws JsonProcessingException {
       List<String> allApplicationsFromFile = fileFunctions.readFromFile(storeFile, -1);

        for (int i = 0; i < allApplicationsFromFile.size(); i++) {
            Application appObj = objectMapper.readValue(allApplicationsFromFile.get(i), Application.class);
            if(Objects.equals(id, appObj.getId())) {
                return ResponseEntity.ok(appObj);
            }
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping(value = "/create-application", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String createApplication(@RequestBody Application app) {
        try {
            String appJson = objectMapper.writeValueAsString(app);
            System.out.println(appJson);
            fileFunctions.writeToFile(storeFile, appJson);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Wrote to file ");
        return "ok";
    }
    @PostMapping(value = "/create-applications", produces = MediaType.TEXT_PLAIN_VALUE)
    public String createMultipleApplication(@RequestParam (value = "rowsLimit", required = false) Integer rowsLimit) {

        Faker faker = new Faker();
        System.out.println("callLimit =  " + rowsLimit);
        for (int i = 1; i <= rowsLimit; i++) {
            Application application = new Application();
            application.setId(i);
            application.setApplicationId(faker.number().digits(6));
            application.setCoverageAmount(faker.number().numberBetween(50000, 1000000));
            application.setCoverageYear(faker.number().numberBetween(5, 25));
            application.setSmoker(faker.bool().bool());
            application.setSubmitted(faker.bool().bool());
            application.setCreatedDate(faker.date().birthday().toLocalDateTime().toLocalDate());
            application.setUpdatedDate(faker.date().birthday().toLocalDateTime().toLocalDate());

            createApplication(application);
        }
        return "ok";
    }
    @DeleteMapping(value = "/delete-application", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteApplication(@RequestParam(value = "id") Integer id ) {
        fileFunctions.deleteApplication(id,storeFile);
    }

    @PutMapping(value = "/update-application", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateApplication(@RequestBody Application app) {
        fileFunctions.updateApplication(app, storeFile);
    }
}