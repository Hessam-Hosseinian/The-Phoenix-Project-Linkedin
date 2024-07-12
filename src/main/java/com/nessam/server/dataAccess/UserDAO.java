package com.nessam.server.dataAccess;

import com.nessam.server.models.User;
import com.nessam.server.models.UserContactInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private final Connection connection;

    public UserDAO() throws SQLException {
        this.connection = DatabaseConnectionManager.getConnection();
        createUserContactInfoTable();
        createUserTable();
        createFilesTable();


    }

    private void createUserTable() throws SQLException {
        String userTableSql = """
                    CREATE TABLE IF NOT EXISTS users (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        email VARCHAR(50) NOT NULL UNIQUE,
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
                        contact_info_id BIGINT,
                        FOREIGN KEY (contact_info_id) REFERENCES user_contact_info(id) ON DELETE SET NULL
                    )
                """;

        try (PreparedStatement statement = connection.prepareStatement(userTableSql)) {
            statement.executeUpdate();
        }
    }

    private void createFilesTable() throws SQLException {
        String filesTableSql = """
                    CREATE TABLE IF NOT EXISTS files (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        path VARCHAR(255) NOT NULL,
                        email_cc VARCHAR(255)
                    )
                """;

        try (PreparedStatement statement = connection.prepareStatement(filesTableSql)) {
            statement.executeUpdate();
        }
    }


    private void createUserContactInfoTable() throws SQLException {
        String contactInfoTableSql = """
                    CREATE TABLE IF NOT EXISTS user_contact_info (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        profile_link VARCHAR(40),
                        email VARCHAR(40),
                        phone_number VARCHAR(40),
                        phone_type INT,
                        address VARCHAR(220),
                        birth_month INT,
                        birth_day INT,
                        birth_privacy_policy INT,
                        instant_contact_method VARCHAR(40)
                    )
                """;
        try (PreparedStatement statement = connection.prepareStatement(contactInfoTableSql)) {
            statement.executeUpdate();
        }
    }


    private void executeUpdate(String sql) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }

    public void saveUser(User user) throws SQLException {
        String userSql = """
                    INSERT INTO users (email, password, first_name, last_name, additional_name, profile_picture, background_picture, title, location, profession, seeking_opportunity, contact_info_id)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement userStmt = connection.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            if (user.getContactInfo() != null) {
                saveUserContactInfo(user.getContactInfo());
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
            userStmt.setObject(12, user.getContactInfo() != null ? user.getContactInfo().getId() : null);

            userStmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void saveUserContactInfo(UserContactInfo contactInfo) throws SQLException {
        String contactSql = """
                    INSERT INTO user_contact_info (profile_link, email, phone_number, phone_type, address, birth_month, birth_day, birth_privacy_policy, instant_contact_method)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement contactStmt = connection.prepareStatement(contactSql, Statement.RETURN_GENERATED_KEYS)) {
            contactStmt.setString(1, contactInfo.getProfileLink());
            contactStmt.setString(2, contactInfo.getEmail());
            contactStmt.setString(3, contactInfo.getPhoneNumber());
            contactStmt.setInt(4, contactInfo.getPhoneType().ordinal());
            contactStmt.setString(5, contactInfo.getAddress());
            contactStmt.setInt(6, contactInfo.getBirthMonth().ordinal());
            contactStmt.setInt(7, contactInfo.getBirthDay());
            contactStmt.setInt(8, contactInfo.getBirthDisplayPolicy().ordinal());
            contactStmt.setString(9, contactInfo.getInstantMessagingId());

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
        executeUpdate(sql);
    }

    public void updateUser(User user) throws SQLException {
        String sql = """
                    UPDATE users SET password = ?, first_name = ?, last_name = ?, additional_name = ?, profile_picture = ?, background_picture = ?, title = ?, location = ?, profession = ?, seeking_opportunity = ?, contact_info_id = ? WHERE email = ?
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
            statement.setObject(11, user.getContactInfo() != null ? user.getContactInfo().getId() : null);
            statement.setString(12, user.getEmail());

            statement.executeUpdate();
        }
    }

    public User getUserByEmail(String email) throws SQLException {
        String sql = """
                    SELECT id, email, password, first_name, last_name, additional_name, profile_picture, background_picture, title, location, profession, seeking_opportunity, contact_info_id
                    FROM users WHERE email = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = mapResultSetToUser(resultSet);
                    user.setContactInfo(getUserContactInfoById(resultSet.getLong("contact_info_id")));
                    return user;
                }
            }
        }

        return null;
    }

    public User getUser(String userId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE Id = ?");
        statement.setString(1, userId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
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
            return user;
        }

        return null; // User not found
    }

    public User getUserByEmailAndPassword(String email, String password) throws SQLException {
        String sql = """
                    SELECT id, email, password, first_name, last_name, additional_name, profile_picture, background_picture, title, location, profession, seeking_opportunity, contact_info_id
                    FROM users WHERE email = ? AND password = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = mapResultSetToUser(resultSet);
                    user.setContactInfo(getUserContactInfoById(resultSet.getLong("contact_info_id")));
                    return user;
                }
            }
        }

        return null;
    }

    public List<User> getAllUsers() throws SQLException {
        String sql = """
                    SELECT id, email, password, first_name, last_name, additional_name, profile_picture, background_picture, title, location, profession, seeking_opportunity, contact_info_id
                    FROM users
                """;

        List<User> users = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = mapResultSetToUser(resultSet);
                user.setContactInfo(getUserContactInfoById(resultSet.getLong("contact_info_id")));
//                user.setEducation(getUserEducations(resultSet.getLong("id")));
                users.add(user);
            }
        }

        return users;
    }


    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
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
        return user;
    }

    private UserContactInfo getUserContactInfoById(Long id) throws SQLException {
        if (id == null) {
            return null;
        }

        String sql = """
                    SELECT id, profile_link, email, phone_number, phone_type, address, birth_month, birth_day, birth_privacy_policy, instant_contact_method
                    FROM user_contact_info WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    UserContactInfo contactInfo = new UserContactInfo();
                    contactInfo.setId(resultSet.getLong("id"));
                    contactInfo.setProfileLink(resultSet.getString("profile_link"));
                    contactInfo.setEmail(resultSet.getString("email"));
                    contactInfo.setPhoneNumber(resultSet.getString("phone_number"));
                    contactInfo.setPhoneType(UserContactInfo.PhoneType.values()[resultSet.getInt("phone_type")]);
                    contactInfo.setAddress(resultSet.getString("address"));
                    contactInfo.setBirthMonth(UserContactInfo.Month.values()[resultSet.getInt("birth_month")]);
                    contactInfo.setBirthDay(resultSet.getInt("birth_day"));
                    contactInfo.setBirthDisplayPolicy(UserContactInfo.DisplayPolicy.values()[resultSet.getInt("birth_privacy_policy")]);
                    contactInfo.setInstantMessagingId(resultSet.getString("instant_contact_method"));
                    return contactInfo;
                }
            }
        }

        return null;
    }

    public UserContactInfo getUserContactInfoByEmail(String email) throws SQLException {
        if (email == null) {
            return null;
        }

        String sql = """
                    SELECT id, profile_link, email, phone_number, phone_type, address, birth_month, birth_day, birth_privacy_policy, instant_contact_method
                    FROM user_contact_info WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(3, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    UserContactInfo contactInfo = new UserContactInfo();
                    contactInfo.setId(resultSet.getLong("id"));
                    contactInfo.setProfileLink(resultSet.getString("profile_link"));
                    contactInfo.setEmail(resultSet.getString("email"));
                    contactInfo.setPhoneNumber(resultSet.getString("phone_number"));
                    contactInfo.setPhoneType(UserContactInfo.PhoneType.values()[resultSet.getInt("phone_type")]);
                    contactInfo.setAddress(resultSet.getString("address"));
                    contactInfo.setBirthMonth(UserContactInfo.Month.values()[resultSet.getInt("birth_month")]);
                    contactInfo.setBirthDay(resultSet.getInt("birth_day"));
                    contactInfo.setBirthDisplayPolicy(UserContactInfo.DisplayPolicy.values()[resultSet.getInt("birth_privacy_policy")]);
                    contactInfo.setInstantMessagingId(resultSet.getString("instant_contact_method"));
                    return contactInfo;
                }
            }
        }

        return null;
    }

    public List<User> searchByName(String keyword) throws SQLException {
        List<User> results = new ArrayList<>();
        String sql = "SELECT id, email, password, first_name, last_name, additional_name," + "profile_picture, background_picture, title, location, profession, seeking_opportunity, contact_info_id FROM users WHERE first_name LIKE ? OR last_name LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + keyword + "%");
            statement.setString(2, "%" + keyword + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = mapResultSetToUser(resultSet);
                    user.setContactInfo(getUserContactInfoById(resultSet.getLong("contact_info_id")));
                    results.add(user);
                }
            }
        }
        return results;
    }
}
