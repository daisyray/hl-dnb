package com.havenlife.dnb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Map;

@Component
public class PasswordResetService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);
    public ResetLinkResult createResetPasswordLink(String email) {

        String sql = "select id from havenlife.register r where email = ?";
        try {
            Map<String, Object> registeredIdMap = jdbcTemplate.queryForMap(sql, email);

            final Integer registerId = (Integer) registeredIdMap.get("id");

            KeyHolder kh = new GeneratedKeyHolder();
            String sqlInsert = "insert into havenlife.password_reset (register_id) values (:rid)";

            try {
                MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
                sqlParameterSource.addValue("rid", registerId);
                namedJdbcTemplate.update(sqlInsert, sqlParameterSource, kh, new String[]{"id"});
                return new ResetLinkResult(kh.getKey().intValue(), null);
            } catch (Exception e) {
                String errorMsg = "Failed to insert table";
                logger.error(errorMsg, e);
                return new ResetLinkResult(null, errorMsg);
            }
        } catch (EmptyResultDataAccessException e) {
            ResetLinkResult resetLinkResult = new ResetLinkResult(null, "Email not found");
            return resetLinkResult;
        }
    }
    public String getEmailPswdEmail(Integer id) {
        String sql = "select email from havenlife.register r , havenlife.password_reset pr where pr.id = ? and r.id = pr.register_id";
        String email = null;
        try {
            Map<String, Object> sqlOutPut = jdbcTemplate.queryForMap(sql, id);
            email = (String) sqlOutPut.get("email");
        } catch (EmptyResultDataAccessException e) {
            logger.error("Invalid id");
        }
        return email;
    }
    public String changePassword(String email, String password, String confirmPassword) {
        String error = null;

        if (email.isBlank()) {
            error = "Email is missing";
        } else if (password.isBlank()) {
            error = "Password is missing";
        } else if (confirmPassword.isBlank()) {
            error = "Confirm password is missing";
        } else {
            final String finalEmail = email.trim();
            final String finalPassword = password.trim();
            confirmPassword = confirmPassword.trim();

            if (!password.equals(confirmPassword)) {
                error = "Password doesn't match";
            } else {
                String sql = "update havenlife.register set password = ? where email = ?";
                try {
                    int rowUpdated = jdbcTemplate.update(con -> {
                        PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        statement.setString(1, finalPassword);
                        statement.setString(2, finalEmail);
                        return statement;
                    });
                    if (rowUpdated != 1) {
                        error = "email not found";
                    }
                } catch (Exception e) {
                    error = "Database error, unable to update";
                    logger.warn(error + email, e);
                }
            }
        }
        return error;
    }
}


