package com.nessam.server.dataAccess;

import com.nessam.server.models.UserEducation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserEducationDAO {

    private final Connection connection;

    public UserEducationDAO() throws SQLException {
        this.connection = DatabaseConnectionManager.getConnection();
        createUserEducationTable();
    }

    private void createUserEducationTable() throws SQLException {
        String userEducationTableSql = """
                    CREATE TABLE IF NOT EXISTS user_education (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        school_name VARCHAR(40) NOT NULL,
                        field_of_study VARCHAR(40) NOT NULL,
                        start_date VARCHAR(20) NOT NULL,
                        end_date VARCHAR(20),
                        grade VARCHAR(40),
                        activities VARCHAR(500),
                        description VARCHAR(1000),
                        skill VARCHAR(500),
                        notify_network BOOLEAN,
                        user_email VARCHAR(40) NOT NULL
                 
                    )
                """;

        try (PreparedStatement statement = connection.prepareStatement(userEducationTableSql)) {
            statement.executeUpdate();
        }
    }

    public void saveUserEducation(UserEducation userEducation) throws SQLException {
        String userEducationSql = """
                    INSERT INTO user_education ( school_name, field_of_study, start_date, end_date, grade, activities, description,skill, notify_network,user_email)
                    VALUES ( ?, ?, ?, ?, ?, ?, ?, ?,?,?)
                """;
        try (PreparedStatement userEducationStmt = connection.prepareStatement(userEducationSql, Statement.RETURN_GENERATED_KEYS)) {
//
            userEducationStmt.setString(1, userEducation.getSchoolName());
            userEducationStmt.setString(2, userEducation.getFieldOfStudy());
            userEducationStmt.setString(3, userEducation.getStartDate());
            userEducationStmt.setString(4, userEducation.getEndDate());
            userEducationStmt.setString(5, userEducation.getGrade());
            userEducationStmt.setString(6, userEducation.getActivities());
            userEducationStmt.setString(7, userEducation.getDescription());
            userEducationStmt.setString(8, userEducation.getSkills());
            userEducationStmt.setBoolean(9, userEducation.getNotifyNetwork());
            userEducationStmt.setString(10,userEducation.getUser_email());

            userEducationStmt.executeUpdate();

            try (ResultSet generatedKeys = userEducationStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    userEducation.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public void updateUserEducation(UserEducation userEducation) throws SQLException {
//        String sql = """
//                    UPDATE user_education SET school_name = ?, field_of_study = ?, start_date = ?, end_date = ?, grade = ?, activities = ?, description = ?, notify_network = ?
//                    WHERE id = ?
//                """;
//
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setString(1, userEducation.getSchoolName());
//            statement.setString(2, userEducation.getFieldOfStudy());
//            statement.setString(3, userEducation.getStartDate());
//            statement.setString(4, userEducation.getEndDate());
//            statement.setString(5, userEducation.getGrade());
//            statement.setString(6, userEducation.getActivities());
//            statement.setString(7, userEducation.getDescription());
//            statement.setBoolean(8, userEducation.getNotifyNetwork());
//            statement.setLong(9, userEducation.getId());
//
//            statement.executeUpdate();
//        }
    }

    public void deleteUserEducation(long userEducationId) throws SQLException {
        String sql = "DELETE FROM user_education WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userEducationId);
            statement.executeUpdate();
        }
    }

    public List<UserEducation> getUserEducationsByUserEmail(String user_email) throws SQLException {
        String sql = "SELECT * FROM user_education WHERE user_email = ?";
        List<UserEducation> userEducations = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user_email);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    UserEducation userEducation = mapResultSetToUserEducation(resultSet);
                    userEducations.add(userEducation);
                }
            }
        }

        return userEducations;
    }

    private UserEducation mapResultSetToUserEducation(ResultSet resultSet) throws SQLException {
        UserEducation userEducation = new UserEducation();
        userEducation.setId(resultSet.getLong("id"));
        userEducation.setSchoolName(resultSet.getString("school_name"));
        userEducation.setFieldOfStudy(resultSet.getString("field_of_study"));
        userEducation.setStartDate(resultSet.getString("start_date"));
        userEducation.setEndDate(resultSet.getString("end_date"));
        userEducation.setGrade(resultSet.getString("grade"));
        userEducation.setActivities(resultSet.getString("activities"));
        userEducation.setSkills(resultSet.getString("skill"));
        userEducation.setDescription(resultSet.getString("description"));
        userEducation.setNotifyNetwork(resultSet.getBoolean("notify_network"));
        userEducation.setUser_email(resultSet.getString("user_email"));

        return userEducation;
    }


}
