package com.havenlife.dnb.service;

import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FMUtils {
    @Autowired
    private Configuration fm;

    public String render(String templatePath) {
        return render(templatePath, Map.of());
    }

    public String render(String templatePath, Map<String, Object> data) {
        try {
            var template = fm.getTemplate(templatePath);
            var errors = data.get("errors");
            Map<String,String> errorMap = new HashMap<>();
            if (errors != null) {
                errorMap = (Map<String, String>) errors;
            }
            Map<String, Object> withErrors = new HashMap<>(data);
            withErrors.put("errorMap", errorMap);
            var writer = new StringWriter();
            template.process(withErrors, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
