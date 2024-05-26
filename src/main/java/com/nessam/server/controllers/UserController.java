package com.nessam.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nessam.server.dataAccess.UserDAO;
import com.nessam.server.models.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserController {
    private final UserDAO userDAO;
    private final ObjectMapper objectMapper;

    public UserController() throws SQLException {
        this.userDAO = new UserDAO();
        this.objectMapper = new ObjectMapper();
    }

    public void createUser(String email, String password, String reapetedPass, String firstName, String lastName, String additionalName, String profilePicture, String backgroundPicture, String title, String location, String profession, String seekingOpportunity) throws SQLException {

        if (!password.equals(reapetedPass)) {
            throw new SQLException("Passwords do not match");
        }
        User user = new User();

        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAdditionalName(additionalName);
        user.setProfilePicture(profilePicture);
        user.setBackgroundPicture(backgroundPicture);
        user.setTitle(title);
        user.setLocation(location);
        user.setProfession(profession);
        user.setSeekingOpportunity(seekingOpportunity);

        if (isUserExists(user.getEmail())) {
            userDAO.updateUser(user);
        } else {
            userDAO.saveUser(user);
        }
    }

    public boolean isUserExists(String email) {
        try {
            return userDAO.getUserByEmail(email) != null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUsers() throws SQLException, JsonProcessingException {
        List<User> users = userDAO.getAllUsers();
        return objectMapper.writeValueAsString(users);
    }

    public String getUserById(String email) throws SQLException, JsonProcessingException {
        User user = userDAO.getUserByEmail(email);
        return user != null ? objectMapper.writeValueAsString(user) : "No User";
    }

    public String getUserByEmailAndPass(String email, String pass) throws SQLException, JsonProcessingException {
        User user = userDAO.getUserByEmail(email, pass);
        return user != null ? objectMapper.writeValueAsString(user) : "No User";
    }

    public void deleteUsers() {
        try {
            userDAO.deleteAllUsers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(String identifier) {
        try {

            userDAO.deleteUserByEmail(identifier);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
