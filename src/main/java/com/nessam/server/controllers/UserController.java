package com.nessam.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nessam.server.dataAccess.UserDAO;
import com.nessam.server.models.User;

import java.sql.SQLException;
import java.util.List;

public class UserController {
    private final UserDAO userDAO;
    private final ObjectMapper objectMapper;

    public UserController() throws SQLException {
        this.userDAO = new UserDAO();
        this.objectMapper = new ObjectMapper();
    }

    public void createUser(String email, String password, String firstName, String lastName, String additionalName, String profilePicture, String backgroundPicture, String title, String location, String profession, String seekingOpportunity) throws SQLException {
        User user = new User(email, password, firstName, lastName, additionalName, profilePicture, backgroundPicture, title, location, profession, seekingOpportunity);

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
        User user = userDAO.getUserByEmailAndPassword(email, pass);
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

    public String searchUser(String keyword) throws SQLException, JsonProcessingException {
        List<User> users = userDAO.searchByName(keyword);
        return objectMapper.writeValueAsString(users);
    }
}
