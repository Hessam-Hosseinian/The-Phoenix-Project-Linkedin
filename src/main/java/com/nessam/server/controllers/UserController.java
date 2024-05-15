package com.nessam.server.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nessam.server.dataAccess.UserDAO;
import com.nessam.server.models.User;


import java.sql.SQLException;
import java.util.ArrayList;

public class UserController {
    private final UserDAO userDAO;

    public UserController() throws SQLException {
        this.userDAO = new UserDAO();
    }

    public void createUser(String ID, String email, String password, String firstName, String lastName, String additionalName, String profilePicture, String backgroundPicture, String title, String location, String profession, String seekingOpportunity) throws SQLException {
        User user = new User();
        user.setID(ID);
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

        if (isUserExists(email))
            userDAO.updateUser(user);
        else
            userDAO.saveUser(user);
    }


    public boolean isUserExists(String ID) {
        if (ID == null) return false;
        try {
            return (userDAO.getUser(ID) != null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUsers() throws SQLException, JsonProcessingException {
        ArrayList<User> users = userDAO.getUsers();
        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(users);
        return response;
    }

    public String getUserById(String id) throws SQLException, JsonProcessingException {
        User user = userDAO.getUser(id);
        if (user == null) return "No User";
        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(user);
        return response;
    }

    public String getUserByIdAndPass(String id, String pass) throws SQLException, JsonProcessingException {
        User user = userDAO.getUser(id, pass);
        if (user == null) return null;
        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(user);

        return response;
    }

    public void deleteUsers() {
        //TODO..................
    }

    public void deleteUser(String userId) {
        User user = new User();
        user.setID(userId);
        userDAO.deleteUser(user);

    }
}
