package com.havenlife.dnb.service;

import com.havenlife.dnb.models.Register;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

@Component
public class RegisterService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String newRegister(Register register) {
        String sql = "insert into havenlife.register ( email, password ) values ( ?, ?)";
        if (!validate(register)) {
            return "Email or Password are required field";
        }
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, register.getEmail());
            stmt.setString(2, register.getPassword());
            return stmt;
        });
        return null;
    }


    public boolean validate(Register register) {
        if (register.getEmail() == null || register.getPassword() == null) {
            return false;
        } else if (register.getEmail() == "" || register.getPassword() == "") {
            return false;
        }
        return true;
    }
}
