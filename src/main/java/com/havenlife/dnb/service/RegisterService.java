package com.havenlife.dnb.service;

import com.havenlife.dnb.models.Register;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RegisterService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Register newRegister(Register register) {
        String sql = "insert into havenlife.register ( email, password ) values ( ?, ?)";
        KeyHolder kh = new GeneratedKeyHolder();
        if (!validate(register)) {
            return null;
        }
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, register.getEmail());
            stmt.setString(2, register.getPassword());
            return stmt;
        }, kh);
        Integer generatedId = (Integer) kh.getKey();
        System.out.println("generated id " + generatedId);
        Register newRegisterObj = new Register();
        newRegisterObj.setId(generatedId);
        newRegisterObj.setEmail(register.getEmail());
        newRegisterObj.setPassword(register.getPassword());
        return newRegisterObj ;
    }
    public List<Register> registerList(Integer limit) {
        String sql = "select * from havenlife.register r order by email limit ?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, limit);

        List<Register> registeredList = new ArrayList<>();
        for(Map<String, Object> row: rows) {
            Register register = new Register();
            register.setId((Integer) row.get("id"));
            register.setEmail((String) row.get("email"));
            register.setPassword((String) row.get("password"));
            registeredList.add(register);
        }
        return registeredList;
    }
    public String updatePassword(Integer id, String password) {
        String sql = "update havenlife.register set password v = ? where id = ?";

        try {
            int rowsUpdated = jdbcTemplate.update(conn -> {
                PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, password);
                preparedStatement.setInt(2, id);

                return preparedStatement;
            });
            if (rowsUpdated != 1) {
                return "Invalid id";
            }
        } catch (Exception e) {
            return "Failed to update ";
        }
        return null;
    }
    public String deleteRegistration(Integer id ) {
        String sql = "delete from havenlife.register where id = ?";
        String error = null;
        try {
            int rowAffected = jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, id);
                return preparedStatement;
            });
            if (rowAffected != 1) {
                error = "Invalid id";
            }
        } catch (Exception e) {
            error = "Database exception";
            e.printStackTrace();
        }
        return error;
    }

    public Integer getRegisterId(String email, String password ) {
        String sql = "select max(id) as id from havenlife.register r where email = ? and password = ?";
        Map<String, Object> row = jdbcTemplate.queryForMap(sql, email, password);
        if(row.isEmpty()) {
            return null;
        }
        Integer id = (Integer) row.get("id");
        return id;
    }

    public boolean validate(Register register) {
        if (register.getEmail() == null || register.getPassword() == null) {
            return false;
        } else if (register.getEmail().isBlank() || register.getPassword().isBlank()) {
            return false;
        }
        //validate for duplicate email
        Integer id = getRegisterId(register.getEmail(), register.getPassword());
        if (id != null) {
            return false;
        }
        return true;
    }
}
