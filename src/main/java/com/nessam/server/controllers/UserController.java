package com.nessam.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nessam.server.dataAccess.UserDAO;
import com.nessam.server.models.User;
import com.nessam.server.models.UserContactInfo;
import com.nessam.server.models.UserEducation;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;

public class UserController {
    private final UserDAO userDAO;
    private final ObjectMapper objectMapper;

    public UserController() throws SQLException {
        this.userDAO = new UserDAO();
        this.objectMapper = new ObjectMapper();
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

    public String getUserContactInfoByUserId(String email) throws SQLException, JsonProcessingException {
        UserContactInfo contactInfo = userDAO.getUserContactInfoByEmail(email);
        return contactInfo != null ? objectMapper.writeValueAsString(contactInfo) : "No User Contact Info";
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


    public void createUser(JSONObject jsonObject) throws SQLException {
        User user = jsonToUser(jsonObject);

        if (isUserExists(user.getEmail())) {
            userDAO.updateUser(user);
        } else {
            userDAO.saveUser(user);
        }


    }

    public void updateUser(JSONObject jsonObject) throws SQLException {
        User user = jsonToUser(jsonObject);

        userDAO.saveUser(user);

    }



    public void createUserContactInfo(JSONObject jsonObject) throws SQLException {
        UserContactInfo contactInfo = jsonToUserContactInfo(jsonObject);


        if (isUserExists(contactInfo.getEmail())) {
            userDAO.saveUserContactInfo(contactInfo);
            User user = userDAO.getUserByEmail(contactInfo.getEmail());
            user.setContactInfo(contactInfo);
            userDAO.updateUser(user);
        } else {
            throw new RuntimeException("User does not exist");
        }

    }
    public String getUserEducations(Long userId) throws SQLException, JsonProcessingException {
        List<UserEducation> educations = userDAO.getUserEducations(userId);
        return objectMapper.writeValueAsString(educations);
    }


    public void creteUserEducation(JSONObject jsonObject) throws SQLException {
        UserEducation userEducation = jsonToUserEducation(jsonObject);

        userDAO.saveUserEducation(userEducation);
    }
    private User jsonToUser(JSONObject jsonObject) {
        User user = new User();
        user.setEmail(jsonObject.getString("email"));
        user.setPassword(jsonObject.getString("password"));
        user.setFirstName(jsonObject.getString("firstName"));
        user.setLastName(jsonObject.getString("lastName"));
        user.setAdditionalName(jsonObject.getString("additionalName"));
        user.setProfilePicture(jsonObject.getString("profilePicture"));
        user.setBackgroundPicture(jsonObject.getString("backgroundPicture"));
        user.setTitle(jsonObject.getString("title"));
        user.setLocation(jsonObject.getString("location"));
        user.setProfession(jsonObject.getString("profession"));
        user.setSeekingOpportunity(jsonObject.getString("seekingOpportunity"));
        return user;
    }
    private UserContactInfo jsonToUserContactInfo(JSONObject jsonObject) {
        UserContactInfo contactInfo = new UserContactInfo();
        contactInfo.setProfileLink(jsonObject.getString("profileLink"));
        contactInfo.setEmail(jsonObject.getString("email"));
        contactInfo.setPhoneNumber(jsonObject.getString("phoneNumber"));
        contactInfo.setPhoneType(UserContactInfo.PhoneType.valueOf(jsonObject.getString("phoneType")));
        contactInfo.setAddress(jsonObject.getString("address"));
        contactInfo.setBirthMonth(UserContactInfo.Month.valueOf(jsonObject.getString("birthMonth")));
        contactInfo.setBirthDay(jsonObject.getInt("birthDay"));
        contactInfo.setBirthDisplayPolicy(UserContactInfo.DisplayPolicy.valueOf(jsonObject.getString("birthDisplayPolicy")));
        contactInfo.setInstantMessagingId(jsonObject.getString("instantMessagingId"));
        return contactInfo;
    }

    private UserEducation jsonToUserEducation(JSONObject jsonObject) throws SQLException {

        UserEducation education = new UserEducation();
        education.setInstitution(jsonObject.getString("institution"));
        education.setDegree(jsonObject.optString("degree"));
        education.setFieldOfStudy(jsonObject.getString("fieldOfStudy"));
        education.setStartDate(jsonObject.optString("startDate"));
        education.setEndDate(jsonObject.optString("endDate"));

        return education;
    }
}
