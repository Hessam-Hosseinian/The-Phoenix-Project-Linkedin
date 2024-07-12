package com.nessam.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nessam.server.dataAccess.UserEducationDAO;
import com.nessam.server.models.UserEducation;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;

public class UserEducationController {

    private final UserEducationDAO userEducationDAO;
    private final ObjectMapper objectMapper;

    public UserEducationController() throws SQLException {
        this.userEducationDAO = new UserEducationDAO();
        this.objectMapper = new ObjectMapper();
    }

    public void createUserEducation(JSONObject jsonObject) throws SQLException {
        UserEducation education = jsonToUserEducation(jsonObject);
        userEducationDAO.saveUserEducation(education);
    }

    public void updateUserEducation(JSONObject jsonObject) throws SQLException {
        UserEducation education = jsonToUserEducation(jsonObject);
        education.setId(jsonObject.getLong("educationId")); // Assuming you pass educationId in JSON

        userEducationDAO.updateUserEducation(education);
    }

    public void deleteUserEducation(long educationId) throws SQLException {
        userEducationDAO.deleteUserEducation(educationId);
    }

    public String getUserEducations(String userId) throws SQLException, JsonProcessingException {
        List<UserEducation> educations = userEducationDAO.getUserEducationsByUserEmail(userId);
        return objectMapper.writeValueAsString(educations);
    }

    // Helper method to convert JSON to UserEducation object
    private UserEducation jsonToUserEducation(JSONObject jsonObject) {
        UserEducation education = new UserEducation();

//        education.setId(jsonObject.getLong("educationId"));
        education.setSchoolName(jsonObject.getString("schoolName"));
        education.setFieldOfStudy(jsonObject.optString("field_of_study"));
        education.setStartDate(jsonObject.optString("start_date"));
        education.setEndDate(jsonObject.optString("end_date"));
        education.setDescription(jsonObject.optString("description"));
        education.setGrade(jsonObject.optString("grade"));
        education.setActivities(jsonObject.optString("activities"));
        education.setSkills(jsonObject.optString("skill"));
        education.setNotifyNetwork(jsonObject.optBoolean("notify_network"));
        education.setUser_email(jsonObject.optString("user_email"));

        return education;
    }
}
