package com.havenlife.dnb.controllers;

import com.havenlife.dnb.service.FMUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@RequestMapping("/check-insurance")
@RestController
public class CheckInsurance {
    @Autowired private FMUtils fmUtils;
    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
        return fmUtils.render("/check-insurance-start/index.ftl");
    }

    @PostMapping(value="/calculate", produces = MediaType.TEXT_HTML_VALUE)
    public String calculate(
        @RequestParam("gender") String gender,
        @RequestParam("dob") String dob,
        @RequestParam("tobacco") String tobacco,
        @RequestParam("coverage-timeframe") String coverageTimeframe,
        @RequestParam("coverage-amount") String coverageAmount
    ) {
        // todo: Implement me!
        return fmUtils.render("/check-insurance-start/calculate-results.ftl", Map.of(
            "gender", gender,
            "dob", dob,
            "tobacco", tobacco,
            "coverage-timeframe", coverageTimeframe,
            "coverage-amount", coverageAmount,
            "premium", new Random().nextDouble(1000.0, 10_1000.0)
        ));
    }
}
