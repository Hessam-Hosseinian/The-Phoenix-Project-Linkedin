package com.nessam.server.dataAccess;

import com.nessam.server.models.Like;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LikeDAO {
    private Connection connection;

    public LikeDAO() throws SQLException, ClassNotFoundException {
        connection = DatabaseConnectionManager.getConnection();
        createLikeTable();
    }

    public void createLikeTable() {
        String sql = "CREATE TABLE IF NOT EXISTS likes (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "post_id BIGINT NOT NULL," +
                "liker VARCHAR(255) NOT NULL," +
                "FOREIGN KEY (post_id) REFERENCES posts(id)" +
                ")";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Table 'likes' created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to create table 'likes'.");
        }
    }

    public List<Like> getAllLikes(Long postId) {
        List<Like> likes = new ArrayList<>();
        String sql = "SELECT id, post_id, liker FROM likes WHERE post_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, postId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Like like = new Like();
                like.setId(resultSet.getLong("id"));
                like.getPost().setId(resultSet.getLong("post_id"));
                like.setLiker(resultSet.getString("liker"));
                likes.add(like);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return likes;
    }

    public void insertLike(Like like) {
        String sql = "INSERT INTO likes (post_id, liker) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, like.getPost().getId());
            statement.setString(2, like.getLiker());
            int affectedRows = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLike(String likerEmail, Long postId) {
        String sql = "DELETE FROM likes WHERE liker_email = ? AND post_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, likerEmail); // Set the email of the user who liked
            statement.setLong(2, postId); // Set the post ID of the liked post
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Like deleted successfully for user with email: " + likerEmail + " and post ID: " + postId);
            } else {
                System.out.println("No like found for user with email: " + likerEmail + " and post ID: " + postId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete like for user with email: " + likerEmail + " and post ID: " + postId);
        }
    }




}
