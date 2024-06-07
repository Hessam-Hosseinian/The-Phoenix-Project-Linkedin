package com.nessam.server.dataAccess;

import com.nessam.server.models.Information;
import com.nessam.server.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private final Connection connection;

    public UserDAO() throws SQLException {
        this.connection = DatabaseConnectionManager.getConnection();
        createUserTable();
        createInformationTable();
    }

    private void createUserTable() throws SQLException {
        String userTableSql = """
            CREATE TABLE IF NOT EXISTS users (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                email VARCHAR(50) NOT NULL,
                password VARCHAR(255) NOT NULL,
                first_name VARCHAR(20),
                last_name VARCHAR(40),
                additional_name VARCHAR(40),
                profile_picture VARCHAR(255),
                background_picture VARCHAR(255),
                title VARCHAR(220),
                location VARCHAR(255),
                profession VARCHAR(255),
                seeking_opportunity VARCHAR(255),
                information_id BIGINT
            )
        """;
        try (PreparedStatement statement = connection.prepareStatement(userTableSql)) {
            statement.executeUpdate();
        }
    }

    private void createInformationTable() throws SQLException {
        String informationTableSql = """
            CREATE TABLE IF NOT EXISTS information (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                profile_link VARCHAR(40),
                email VARCHAR(40),
                phone_number VARCHAR(40),
                phone_type INT,
                address VARCHAR(220),
                birth_month DATE,
                birth_day DATE,
                birth_privacy_policy INT,
                instant_contact_method VARCHAR(40),
                user_id BIGINT,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            )
        """;
        try (PreparedStatement statement = connection.prepareStatement(informationTableSql)) {
            statement.executeUpdate();
        }
    }

    public void saveUser(User user) throws SQLException {
        String userSql = """
            INSERT INTO users (email, password, first_name, last_name, additional_name, profile_picture, background_picture, title, location, profession, seeking_opportunity, information_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement userStmt = connection.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            if (user.getInformation() != null) {
                saveContactInformation(user.getInformation());
            }
            userStmt.setString(1, user.getEmail());
            userStmt.setString(2, user.getPassword());
            userStmt.setString(3, user.getFirstName());
            userStmt.setString(4, user.getLastName());
            userStmt.setString(5, user.getAdditionalName());
            userStmt.setString(6, user.getProfilePicture());
            userStmt.setString(7, user.getBackgroundPicture());
            userStmt.setString(8, user.getTitle());
            userStmt.setString(9, user.getLocation());
            userStmt.setString(10, user.getProfession());
            userStmt.setString(11, user.getSeekingOpportunity());
            userStmt.setObject(12, user.getInformation() != null ? user.getInformation().getId() : null);
            userStmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private void saveContactInformation(Information contactInfo) throws SQLException {
        String contactSql = """
            INSERT INTO information (profile_link, email, phone_number, phone_type, address, birth_month, birth_day, birth_privacy_policy, instant_contact_method)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement contactStmt = connection.prepareStatement(contactSql, Statement.RETURN_GENERATED_KEYS)) {
            contactStmt.setString(1, contactInfo.getProfileLink());
            contactStmt.setString(2, contactInfo.getEmail());
            contactStmt.setString(3, contactInfo.getPhoneNumber());
            contactStmt.setInt(4, contactInfo.getPhoneType());
            contactStmt.setString(5, contactInfo.getAddress());
            contactStmt.setDate(6, contactInfo.getBirthMonth());
            contactStmt.setDate(7, contactInfo.getBirthDay());
            contactStmt.setInt(8, contactInfo.getBirthPrivacyPolicy());
            contactStmt.setString(9, contactInfo.getInstantContactMethod());

            contactStmt.executeUpdate();
            try (ResultSet generatedKeys = contactStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contactInfo.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public void deleteUserByEmail(String email) throws SQLException {
        String sql = "DELETE FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.executeUpdate();
        }
    }

    public void deleteAllUsers() throws SQLException {
        String sql = "DELETE FROM users";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }

    public User getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? buildUser(resultSet) : null;
            }
        }
    }

    public User getUserByEmailAndPassword(String email, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? buildUser(resultSet) : null;
            }
        }
    }

    public List<User> getAllUsers() throws SQLException {
        String sql = "SELECT * FROM users";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(buildUser(resultSet));
            }
            return users;
        }
    }

    public void updateUser(User user) throws SQLException {
        String sql = """
            UPDATE users SET
                password = ?, first_name = ?, last_name = ?, additional_name = ?, profile_picture = ?, background_picture = ?, title = ?, location = ?, profession = ?, seeking_opportunity = ?
            WHERE email = ?
        """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getPassword());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getAdditionalName());
            statement.setString(5, user.getProfilePicture());
            statement.setString(6, user.getBackgroundPicture());
            statement.setString(7, user.getTitle());
            statement.setString(8, user.getLocation());
            statement.setString(9, user.getProfession());
            statement.setString(10, user.getSeekingOpportunity());
            statement.setString(11, user.getEmail());
            statement.executeUpdate();
        }
    }

    public List<User> searchByName(String keyword) throws SQLException {
        String sql = "SELECT * FROM users WHERE first_name LIKE ? OR last_name LIKE ? OR additional_name LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            String searchTerm = "%" + keyword + "%";
            statement.setString(1, searchTerm);
            statement.setString(2, searchTerm);
            statement.setString(3, searchTerm);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<User> users = new ArrayList<>();
                while (resultSet.next()) {
                    users.add(buildUser(resultSet));
                }
                return users;
            }
        }
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setAdditionalName(resultSet.getString("additional_name"));
        user.setProfilePicture(resultSet.getString("profile_picture"));
        user.setBackgroundPicture(resultSet.getString("background_picture"));
        user.setTitle(resultSet.getString("title"));
        user.setLocation(resultSet.getString("location"));
        user.setProfession(resultSet.getString("profession"));
        user.setSeekingOpportunity(resultSet.getString("seeking_opportunity"));

        Information contactInfo = getContactInformationByUserId(resultSet.getLong("id"));
        user.setInformation(contactInfo);

        return user;
    }

    private Information getContactInformationByUserId(Long userId) throws SQLException {
        String sql = "SELECT * FROM information WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? buildContactInformation(resultSet) : null;
            }
        }
    }

    private Information buildContactInformation(ResultSet resultSet) throws SQLException {
        Information contactInfo = new Information();
        contactInfo.setId(resultSet.getLong("id"));
        contactInfo.setProfileLink(resultSet.getString("profile_link"));
        contactInfo.setEmail(resultSet.getString("email"));
        contactInfo.setPhoneNumber(resultSet.getString("phone_number"));
        contactInfo.setPhoneType(resultSet.getInt("phone_type"));
        contactInfo.setAddress(resultSet.getString("address"));
        contactInfo.setBirthMonth(resultSet.getDate("birth_month"));
        contactInfo.setBirthDay(resultSet.getDate("birth_day"));
        contactInfo.setBirthPrivacyPolicy(resultSet.getInt("birth_privacy_policy"));
        contactInfo.setInstantContactMethod(resultSet.getString("instant_contact_method"));
        return contactInfo;
    }
}
