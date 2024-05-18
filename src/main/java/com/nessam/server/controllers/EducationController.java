package com.nessam.server.controllers;

import com.nessam.server.dataAccess.EducationDAO;
import com.nessam.server.models.Education;
import com.nessam.server.models.User;

import java.sql.Date;
import java.sql.SQLException;

public class EducationController {

    private final EducationDAO educationDAO;

    public EducationController() throws SQLException {
        this.educationDAO = new EducationDAO();

    }


    public void createEducation(User user, String schoolName, String fieldOfStudy, Date educationStartDate, Date educationEndDate, String grade, String activitiesDescription, String description, String skills, boolean notifyChanges) {
        Education education = new Education();
        education.setUser(user);
        education.setSchoolName(schoolName);
        education.setFieldOfStudy(fieldOfStudy);
        education.setEducationStartDate(educationStartDate);
        education.setEducationEndDate(educationEndDate);
        education.setGrade(grade);
        education.setActivitiesDescription(activitiesDescription);
        education.setDescription(description);
        education.setSkills(skills);
        education.setNotifyChanges(notifyChanges);


    }

    public boolean isEducationExists(String userId) {
//        if (ID == null) return false;
        return (educationDAO.getEducationById(userId) != null);
    }

}
