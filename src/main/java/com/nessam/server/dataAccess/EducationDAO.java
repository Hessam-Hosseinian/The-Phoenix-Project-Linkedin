package com.nessam.server.dataAccess;

import com.nessam.server.models.Education;
import com.nessam.server.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EducationDAO {
    private final Connection connection;


    public EducationDAO() throws SQLException {
        this.connection = DatabaseConnectionManager.getConnection();


        createEducationTable();

    }

    public void createEducationTable() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS Education ( userID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                        "                           schoolName VARCHAR(40),\n" +
                        "                           fieldOfStudy VARCHAR(40),\n" +
                        "                           educationStartDate DATE,\n" +
                        "                           educationEndDate DATE,\n" +
                        "                           grade VARCHAR(40),\n" +
                        "                           activitiesDescription VARCHAR(500),\n" +
                        "                           description VARCHAR(1000),\n" +
                        "                           skills VARCHAR(40) ,\n" +
                        "                           notifyChanges BOOLEAN,\n" +
                        "                           FOREIGN KEY (userID) REFERENCES Users(ID));");
        statement.executeUpdate();
    }

    public void saveEducation(Education education) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO Education (userID, schoolName, fieldOfStudy, educationStartDate, educationEndDate, grade, activitiesDescription, description, skills, notifyChanges) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        statement.setString(1, education.getUserID());
        statement.setString(2, education.getSchoolName());
        statement.setString(3, education.getFieldOfStudy());
        statement.setDate(4, java.sql.Date.valueOf(education.getEducationStartDate().toLocalDate()));
        statement.setDate(5, java.sql.Date.valueOf(education.getEducationEndDate().toLocalDate()));
        statement.setString(6, education.getGrade());
        statement.setString(7, education.getActivitiesDescription());
        statement.setString(8, education.getDescription());
        statement.setString(9, education.getSkills());
        statement.setBoolean(10, education.isNotifyChanges());

        statement.executeUpdate();
    }
}
