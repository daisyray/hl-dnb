package com.havenlife.dnb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.havenlife.dnb.database.FileFunctions;
import com.havenlife.dnb.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class UserService {
    @Autowired
    private FileFunctions fileFunctions;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> validate(User user) {
        List<String> validationErrors = new ArrayList<>();
        try {
            if ((user.getFirstName() == null) || (user.getFirstName().isBlank())) {
                validationErrors.add("FirstName is missing");
            }
            if (user.getDateOfBirth() == null) {
                validationErrors.add("Date is missing");
            } else if (user.getDateOfBirth().isAfter(LocalDate.now())) {
                validationErrors.add(" Date of birth is in future");
            }
            if ((user.getSsn() == null) || (user.getSsn().isBlank())) {
                validationErrors.add("SSN is missing");
            }
            if (user.getStateAbbrev().length() != 2) {
                validationErrors.add("State abbreviation should only be 2 characters");
            }
            if (user.getPhoneNumber().length() > 25) {
                validationErrors.add("Phone number should not be more than 25 characters long");
            }

            if (findUserIdForSsn(user.getSsn()) != null) {
                validationErrors.add("Duplicate SSN");
            }
        } catch (Exception e) {
            validationErrors.add("Validation errors " + e.getMessage());
        }
        return validationErrors;
    }
    public Integer createUser(User userObj) {
        int randomId = ThreadLocalRandom.current().nextInt();
        try {
            userObj.setId(randomId);
            String userAsString = objectMapper.writeValueAsString(userObj);
            System.out.println(userAsString);
            fileFunctions.writeToFile("user.txt", userAsString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return userObj.getId(); //if successful
    }
    public User getUserWithId(Integer id) {
        String sql = "select * from havenlife.users u where u.id = ?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, id);

        if(rows.isEmpty()) {
            return null;
        }
        Map<String,Object> row = rows.get(0);
        User userObj = new User();

        userObj.setFirstName((String) row.get("first_name"));
        userObj.setLastName((String) row.get("last_name"));
        userObj.setSsn((String) row.get("ssn"));
        userObj.setId((Integer) row.get("id"));
        userObj.setZip((Integer) row.get("zip"));
        userObj.setPhoneNumber((String) row.get("phone_number"));
            return userObj;
    }
    public List<User> getUserByName(String firstname, String lastName) {
//        String sql = "select * from havenlife.users u2 where first_name = ? and last_name = ?";
        String sql = "select * from havenlife.users u2 where first_name like ? or last_name like ?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, firstname+"%", lastName+"%");
        List<User> users = new ArrayList<>();
        for(Map<String, Object> row: rows) {
            User userObj = new User();
            userObj.setFirstName((String) row.get("first_name"));
            userObj.setLastName((String) row.get("last_name"));
            userObj.setSsn((String) row.get("ssn"));
            userObj.setId((Integer) row.get("id"));
            userObj.setZip((Integer) row.get("zip"));
            userObj.setPhoneNumber((String) row.get("phone_number"));
            users.add(userObj);
        }
        return users;
    }

    public Integer findUserIdForSsn(String ssn) {
        String sql = "select u.id from havenlife.users u where u.ssn = ?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, ssn);
        if (rows.isEmpty()) {
            return null;
        }
        return (Integer) rows.get(0).get("id");
    }
    public String updateUserDB(User user) {
        if(user.getId() == null ) {
            return "Id is required";
        }
        String sql = "update havenlife.users set (first_name, last_name, register_id, gender, ssn, date_of_birth, address_line1, address_line2, state_abbrev, city, zip, phone_number) = (?, ?, ?, cast(? as havenlife.user_gender), ?, ?, ?, ?, ?, ?, ?, ?) where id = ?";
        jdbcTemplate.update(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql);
            int i = 1;
            stmt.setString(i++, user.getFirstName());
            stmt.setString(i++, user.getLastName());
            stmt.setInt(i++, user.getRegisterId());
            stmt.setString(i++, user.getGender());
            stmt.setString(i++, user.getSsn());
            stmt.setDate(i++, Date.valueOf(user.getDateOfBirth()));
            stmt.setString(i++, user.getAddressLine1());
            stmt.setString(i++, user.getAddressLine2());
            stmt.setString(i++, user.getStateAbbrev());
            stmt.setString(i++, user.getCity());
            stmt.setInt(i++, user.getZip());
            stmt.setString(i++, user.getPhoneNumber());
            stmt.setInt(i++, user.getId());
            return stmt;
        });
        return null;
    }
    public List<User> readUsersFromDB(Integer numOfRows, String orderByColumn) {
        try {
            if(numOfRows == null || numOfRows < 1 || numOfRows > 1000) {
                System.out.println("Number of Rows value is " + numOfRows + "which is invalid");
                numOfRows = 100;
            }
            if ( orderByColumn == null) {
                orderByColumn = "first_name";
            }
            String sql = "select * from havenlife.users u order by " + orderByColumn + " limit ?";
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, numOfRows);
            List<User> users = new ArrayList<>();
            for (Map<String, Object> map : rows) {
                User userObj = new User();
                userObj.setId((Integer) map.get("id"));
                userObj.setFirstName((String) map.get("first_name"));
                userObj.setLastName((String) map.get("last_name"));
                Date dt = (Date)map.get("date_of_birth");
                if (dt != null) {
                    userObj.setDateOfBirth(dt.toLocalDate());
                }
                userObj.setSsn((String) map.get("ssn"));
                userObj.setGender((String) map.get("gender"));
                userObj.setAddressLine1((String) map.get("address_line1"));
                userObj.setAddressLine2((String) map.get("address_line2"));
                userObj.setCity((String) map.get("city"));
                userObj.setStateAbbrev((String) map.get("state_abbrev"));
                userObj.setZip((Integer) map.get("zip"));
                userObj.setRegisterId((Integer) map.get("register_id"));
                userObj.setPhoneNumber((String) map.get("phone_number"));

                users.add(userObj);
                System.out.println(map);
            }
            return users;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String deleteUserFromDb(Integer id) {
        String sql = "delete from havenlife.users where id = ?";
        Integer deletedRowCount = jdbcTemplate.update(sql, id);
        if(deletedRowCount != 1) {
            return "Id doesn't exist";
        }
        return null;
    }
    public Integer createUserDB(User user) {
        String sql = "insert into havenlife.users (first_name, last_name, register_id, gender, ssn, date_of_birth, address_line1, address_line2, state_abbrev, city, zip, phone_number) values (?, ?, ?, cast(? as havenlife.user_gender), ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder kh = new GeneratedKeyHolder();
        System.out.println("Daisy object " + user);
        jdbcTemplate.update(conn -> {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            stmt.setString(i++, user.getFirstName());
            stmt.setString(i++, user.getLastName());
            stmt.setInt(i++, user.getRegisterId());
            stmt.setString(i++, user.getGender());
            stmt.setString(i++, user.getSsn());
            stmt.setDate(i++, Date.valueOf(user.getDateOfBirth()));
            stmt.setString(i++, user.getAddressLine1());
            stmt.setString(i++, user.getAddressLine2());
            stmt.setString(i++, user.getStateAbbrev());
            stmt.setString(i++, user.getCity());
            stmt.setInt(i++, user.getZip());
            stmt.setString(i++, user.getPhoneNumber());

            return stmt;
        }, kh);
        int id;
        if (kh.getKeys().size() > 1) {
            id = (Integer) kh.getKeys().get("id");
        } else {
            id = (Integer) kh.getKey();
        }
        return id;
    }

    public List<User> readsUsers(Integer numberOfUsersToRead) {
        List<User> allUsersObjs = new ArrayList<>();
        if (numberOfUsersToRead < 1) {
            return allUsersObjs;
        }
        try {
            List<String> usersInString = fileFunctions.readFromFile("user.txt", numberOfUsersToRead);
            for (int i = 0; i < usersInString.size(); i++) {
                User userObj = objectMapper.readValue(usersInString.get(i), User.class);
                allUsersObjs.add(userObj);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return allUsersObjs;
    }
}
// add another variable String orderByColumnName on readUsersFromDB to sort by that column, if nothing give sort by firstname
