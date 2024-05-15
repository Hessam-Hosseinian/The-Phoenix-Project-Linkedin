package com.nessam.server.dataAccess;


import com.nessam.server.models.CurrentJobPosition;
import com.nessam.server.models.User;

import java.sql.*;
import java.util.ArrayList;

public class UserDAO {

    private final Connection connection;


    public UserDAO() throws SQLException {
        this.connection = DatabaseConnectionManager.getConnection();
        createUserTable();
//

    }
    //-------------------------------------------------------------------------------------------------------

    public void createUserTable() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS Users (ID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                        "                      email VARCHAR(255),\n" +
                        "                      password VARCHAR(255),\n" +
                        "                      firstName VARCHAR(20),\n" +
                        "                      lastName VARCHAR(40),\n" +
                        "                      additionalName VARCHAR(40),\n" +
                        "                      profilePicture VARCHAR(255),\n" +
                        "                      backgroundPicture VARCHAR(255),\n" +
                        "                      title VARCHAR(220),\n" +
                        "                      location VARCHAR(255),\n" +
                        "                      profession VARCHAR(255),\n" +
                        "                      seekingOpportunity VARCHAR(255));");
        statement.executeUpdate();
    }

    public void createCurrentJobPosition() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS CurrentJobPosition (   userID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                        "                                    jobTitle VARCHAR(40),\n" +
                        "                                    employmentType INT,\n" +
                        "                                    companyName VARCHAR(40),\n" +
                        "                                    workLocation VARCHAR(40),\n" +
                        "                                    workplaceType INT,\n" +
                        "                                    isActive BOOLEAN,\n" +
                        "                                    startDate DATE,\n" +
                        "                                    endDate DATE,\n" +
                        "                                    description VARCHAR(1000),\n" +
                        "                                    skills VARCHAR(40) ,\n" +
                        "                                    notifyChanges BOOLEAN,\n" +
                        "                                    FOREIGN KEY (userID) REFERENCES User(ID));");
        statement.executeUpdate();

    }


    //(SAVE)-------------------------------------------------------------------------------------------------------


    public void saveUser(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO users (email, password, firstName, lastName, additionalName, profilePicture, backgroundPicture, title, location, profession, seekingOpportunity) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        statement.setString(1, user.getEmail());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getFirstName());
        statement.setString(4, user.getLastName());
        statement.setString(5, user.getAdditionalName());
        statement.setString(6, user.getProfilePicture());
        statement.setString(7, user.getBackgroundPicture());
        statement.setString(8, user.getTitle());
        statement.setString(9, user.getLocation());
        statement.setString(10, user.getProfession());
        statement.setString(11, user.getSeekingOpportunity());

        statement.executeUpdate();
    }

    public void saveCurrentJobPosition(User.CurrentJobPosition currentJob) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO CurrentJob (userID, jobTitle, companyName, workLocation, employmentType, workplaceType, startDate, endDate, description, skills, notifyChanges) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        statement.setString(1, currentJob.getUserID());
        statement.setString(2, currentJob.getJobTitle());
        statement.setString(3, currentJob.getCompanyName());
        statement.setString(4, currentJob.getWorkLocation());
        statement.setInt(5, currentJob.getEmploymentType());
        statement.setInt(6, currentJob.getWorkplaceType());
        statement.setDate(7, java.sql.Date.valueOf(currentJob.getStartDate().toLocalDate()));
        statement.setDate(8, java.sql.Date.valueOf(currentJob.getEndDate().toLocalDate()));
        statement.setString(9, currentJob.getDescription());
        statement.setString(10, currentJob.getSkills());
        statement.setBoolean(11, currentJob.isNotifyChanges());

        statement.executeUpdate();
    }



    //(UPDATE)-------------------------------------------------------------------------------------------------------

    public void updateUser(User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE users SET email = ?, password = ?, firstName = ?, lastName = ?, additionalName = ?, profilePicture = ?, backgroundPicture = ?, title = ?, location = ?, profession = ?, seekingOpportunity = ? WHERE ID = ?");

        statement.setString(1, user.getEmail());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getFirstName());
        statement.setString(4, user.getLastName());
        statement.setString(5, user.getAdditionalName());
        statement.setString(6, user.getProfilePicture());
        statement.setString(7, user.getBackgroundPicture());
        statement.setString(8, user.getTitle());
        statement.setString(9, user.getLocation());
        statement.setString(10, user.getProfession());
        statement.setString(11, user.getSeekingOpportunity());
        statement.setString(12, user.getID());

        statement.executeUpdate();
    }

    public void updateCurrentJobPosition(CurrentJobPosition currentJobPosition) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE CurrentJobPosition SET jobTitle = ?, employmentType = ?, companyName = ?, workLocation = ?, workplaceType = ?, isActive = ?, startDate = ?, endDate = ?, description = ?, skills = ?, notifyChanges = ? WHERE userID = ?");

        statement.setString(1, currentJobPosition.getJobTitle());
        statement.setInt(2, currentJobPosition.getEmploymentType());
        statement.setString(3, currentJobPosition.getCompanyName());
        statement.setString(4, currentJobPosition.getWorkLocation());
        statement.setInt(5, currentJobPosition.getWorkplaceType());
        statement.setBoolean(6, currentJobPosition.isActive());
        statement.setDate(7, java.sql.Date.valueOf(currentJobPosition.getStartDate().toLocalDate()));
        statement.setDate(8, java.sql.Date.valueOf(currentJobPosition.getEndDate().toLocalDate()));
        statement.setString(9, currentJobPosition.getDescription());
        statement.setString(10, currentJobPosition.getSkills());
        statement.setBoolean(11, currentJobPosition.isNotifyChanges());
        statement.setString(12, currentJobPosition.getUserID());

        statement.executeUpdate();
    }
    //(GET)-------------------------------------------------------------------------------------------------------


    public User getUser(String ID) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE ID = ?");
        statement.setString(1, ID);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            User user = new User();
            user.setID(resultSet.getString("ID"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            user.setFirstName(resultSet.getString("firstName"));
            user.setLastName(resultSet.getString("lastName"));
            user.setAdditionalName(resultSet.getString("additionalName"));
            user.setProfilePicture(resultSet.getString("profilePicture"));
            user.setBackgroundPicture(resultSet.getString("backgroundPicture"));
            user.setTitle(resultSet.getString("title"));
            user.setLocation(resultSet.getString("location"));
            user.setProfession(resultSet.getString("profession"));
            user.setSeekingOpportunity(resultSet.getString("seekingOpportunity"));
            return user;
        }

        return null; // User not found
    }

    public User getUser(String userID, String userPass) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE ID = ? AND password = ?");
        statement.setString(1, userID);
        statement.setString(2, userPass);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            User user = new User();
            user.setID(resultSet.getString("ID"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            user.setFirstName(resultSet.getString("firstName"));
            user.setLastName(resultSet.getString("lastName"));
            user.setAdditionalName(resultSet.getString("additionalName"));
            user.setProfilePicture(resultSet.getString("profilePicture"));
            user.setBackgroundPicture(resultSet.getString("backgroundPicture"));
            user.setTitle(resultSet.getString("title"));
            user.setLocation(resultSet.getString("location"));
            user.setProfession(resultSet.getString("profession"));
            user.setSeekingOpportunity(resultSet.getString("seekingOpportunity"));

            return user;
        }

        return null; // User not found
    }

    public User.CurrentJobPosition getCurrentJobPosition(String userID) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM CurrentJobPosition WHERE userID = ?");
        statement.setString(1, userID);
        ResultSet resultSet = statement.executeQuery();


        if (resultSet.next()) {
            User.CurrentJobPosition jobPosition = new User.CurrentJobPosition();
            jobPosition.setUserID(resultSet.getString("userID"));
            jobPosition.setJobTitle(resultSet.getString("jobTitle"));
            jobPosition.setEmploymentType(resultSet.getInt("employmentType"));
            jobPosition.setCompanyName(resultSet.getString("companyName"));
            jobPosition.setWorkLocation(resultSet.getString("workLocation"));
            jobPosition.setWorkplaceType(resultSet.getInt("workplaceType"));
            jobPosition.setActive(resultSet.getBoolean("isActive"));
            jobPosition.setStartDate(Date.valueOf(resultSet.getDate("startDate").toLocalDate()));
            jobPosition.setEndDate(Date.valueOf(resultSet.getDate("endDate").toLocalDate()));
            jobPosition.setDescription(resultSet.getString("description"));
            jobPosition.setSkills(resultSet.getString("skills"));
            jobPosition.setNotifyChanges(resultSet.getBoolean("notifyChanges"));
            return jobPosition;
        }

        return null; // Job position not found
    }


    public ArrayList<User> getUsers() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            User user = new User();
            user.setID(resultSet.getString("ID"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            user.setFirstName(resultSet.getString("firstName"));
            user.setLastName(resultSet.getString("lastName"));
            user.setAdditionalName(resultSet.getString("additionalName"));
            user.setProfilePicture(resultSet.getString("profilePicture"));
            user.setBackgroundPicture(resultSet.getString("backgroundPicture"));
            user.setTitle(resultSet.getString("title"));
            user.setLocation(resultSet.getString("location"));
            user.setProfession(resultSet.getString("profession"));
            user.setSeekingOpportunity(resultSet.getString("seekingOpportunity"));
            users.add(user);
        }

        return users;
    }

    public void deleteUser(User user) {

    }
}
