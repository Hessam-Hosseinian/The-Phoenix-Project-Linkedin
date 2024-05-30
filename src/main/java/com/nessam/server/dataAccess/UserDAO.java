package com.nessam.server.dataAccess;

import com.nessam.server.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private final Connection connection;

    public UserDAO() throws SQLException {
        connection = DatabaseConnectionManager.getConnection();
        createUserTable();
    }

    public void createUserTable() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS users (id  BIGINT AUTO_INCREMENT PRIMARY KEY, email VARCHAR(50)  NOT NULL, password VARCHAR(255) NOT NULL, first_name  VARCHAR(20), last_name  VARCHAR(40),  additional_name     VARCHAR(40),  profile_picture     VARCHAR(255),  background_picture  VARCHAR(255),   title    VARCHAR(220),   location    VARCHAR(255),   profession          VARCHAR(255), seeking_opportunity VARCHAR(255))");
        statement.executeUpdate();
    }

    public void saveUser(User user) throws SQLException {
        String sql = "INSERT INTO users (email, password, first_name, last_name, additional_name, profile_picture, background_picture, title, location, profession, seeking_opportunity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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
        } catch (SQLException e) {
            // Handle the exception properly, maybe log it or rethrow
            throw e;
        }
    }

    public void deleteUserByEmail(String email) throws SQLException {
        String sql = "DELETE FROM users WHERE email = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.executeUpdate();
        } catch (SQLException e) {
            // Handle the exception properly, maybe log it or rethrow
            throw e;
        }
    }


    public void deleteAllUsers() throws SQLException {
        String sql = "DELETE FROM users";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int rowsDeleted = statement.executeUpdate();
            System.out.println(rowsDeleted + " rows deleted.");
        } catch (SQLException e) {
            // Handle the exception properly, maybe log it or rethrow
            throw e;
        }
    }

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET password = ?, first_name = ?, last_name = ?, additional_name = ?, profile_picture = ?, background_picture = ?, title = ?, location = ?, profession = ?, seeking_opportunity = ? WHERE email = ?";

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
        } catch (SQLException e) {
            // Handle the exception properly, maybe log it or rethrow
            throw e;
        }
    }

    public User getUserByEmail(String email) throws SQLException {
        String sql = "SELECT email, password, first_name, last_name, additional_name, profile_picture, background_picture, title, location, profession, seeking_opportunity FROM users WHERE email = ?";
        User user = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User();
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
                }
            }
        } catch (SQLException e) {
            // Handle the exception properly, maybe log it or rethrow
            throw e;
        }

        return user;
    }

    public User getUserByEmail(String email, String password) throws SQLException {
        String sql = "SELECT Id, email, password, first_name, last_name, additional_name, profile_picture, background_picture, title, location, profession, seeking_opportunity FROM users WHERE email = ? AND password =?";
        User user = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User();
                    user.setId(resultSet.getLong("Id"));
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
                }
            }
        } catch (SQLException e) {
            // Handle the exception properly, maybe log it or rethrow
            throw e;
        }

        return user;
    }


    public List<User> getAllUsers() throws SQLException {
        String sql = "SELECT Id, email, password, first_name, last_name, additional_name, profile_picture, background_picture, title, location, profession, seeking_opportunity FROM users";
        List<User> users = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("Id"));
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

                users.add(user);
            }
        } catch (SQLException e) {
            // Handle the exception properly, maybe log it or rethrow
            throw e;
        }

        return users;
    }

    public List<String> searchByName(String keyword) throws SQLException {
        List<String> results = new ArrayList<>();
        String sql = "SELECT first_name, last_name FROM users WHERE first_name LIKE ? OR last_name LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + keyword + "%");
            statement.setString(2, "%" + keyword + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String name = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                    results.add(name);
                }
            }
        }
        return results;
    }
}
