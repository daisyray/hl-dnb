package com.havenlife.dnb.service;

import com.havenlife.dnb.models.Application;
import com.havenlife.dnb.models.WebResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.LifecycleAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ApplicationService {
        @Autowired
        private JdbcTemplate jdbcTemplate;
        @Autowired
        private NamedParameterJdbcTemplate namedJdbcTemplate;
        private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);

        public WebResponse createApplicationDB(Application application) {
                List<String> errors = validateApplication(application);
                if(!errors.isEmpty()) {
                        return new WebResponse(errors);
                }
                Integer userId = application.getUserId();
                System.out.println("user id is " + userId);
                String sql = "insert into havenlife.applications (user_id, coverage_amount, coverage_years, is_smoker," +
                        " is_submitted, is_us_citizen, salary, has_drivers_license, has_cancer, profession) " +
                        "values (:userId, :cAmount, :cYear, :isSmoker, :isSubmitted, :usCitizen, :salary, :license, " +
                        ":cancer, :profession)";
                try {
                        KeyHolder kh = new GeneratedKeyHolder();
                        MapSqlParameterSource mpsp = new MapSqlParameterSource();
                        mpsp.addValue("userId", application.getUserId());
                        mpsp.addValue("cAmount", application.getCoverageAmount());
                        mpsp.addValue("cYear", application.getCoverageYear());
                        mpsp.addValue("isSmoker", application.isSmoker());
                        mpsp.addValue("salary", application.getSalary());
                        mpsp.addValue("license", application.isHasLicense());
                        mpsp.addValue("cancer", application.isHasCancer());
                        mpsp.addValue("usCitizen", application.isCitizen());
                        mpsp.addValue("profession", application.getProfession());
                        mpsp.addValue("isSubmitted", application.isSubmitted());

                        namedJdbcTemplate.update(sql, mpsp, kh, new String[]{"id"});
                        Integer id = kh.getKey().intValue();
                        return new WebResponse(id);
                } catch (Exception e) {
                        String errorMsg = "Database error";
                        logger.error(errorMsg, e);
                        return new WebResponse(errorMsg);
                }
        }
        public boolean checkUserId(Integer id) {
                String sql = "select * from havenlife.applications a where user_id = " + id;
                List<Map<String, Object>> rowId = jdbcTemplate.queryForList(sql);
                if (rowId.size() > 1) {
                }
        }

        private List<String> validateApplication(Application application) {
                List<String> validationErrors = new ArrayList<>();
                try {
                        if (application.getUserId() == null || application.getUserId() == 0) {
                                validationErrors.add("UserId is missing");
                        }
                        if (application.getCoverageAmount() == null || application.getCoverageAmount() == 0) {
                                validationErrors.add("coverage amount is missing");
                        }
                        if (application.getCoverageYear() == null || application.getCoverageYear() == 0) {
                                validationErrors.add("coverage year is missing");
                        }
                        if (application.getSalary() == null || application.getSalary() == 0) {
                                validationErrors.add("salary is missing");
                        }

                } catch (Exception e) {
                        validationErrors.add("validation error" + e.getMessage());
                }
                return validationErrors;
        }
}
