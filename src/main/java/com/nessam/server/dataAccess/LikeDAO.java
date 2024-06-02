package com.nessam.server.dataAccess;

import com.nessam.server.models.Like;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LikeDAO {

    private final Connection connection;

    public LikeDAO() throws SQLException {
        this.connection = DatabaseConnectionManager.getConnection();
        createLikeTable();
    }

    public void createLikeTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS likes (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "liker VARCHAR(255) NOT NULL," +
                "fk_post_Id BIGINT NOT NULL, " +
                "FOREIGN KEY (fk_post_Id) REFERENCES posts(post_Id)" +
        ")";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.executeUpdate();
    }

    public List<Like> getAllLikes(Long postId) {
        List<Like> likes = new ArrayList<>();
        String sql = "SELECT liker FROM likes WHERE post_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, postId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Like like = new Like();
                like.setLiker(resultSet.getString("liker"));
                likes.add(like);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return likes;
    }

    public void insertLike(Like like) throws SQLException {
        String query = "INSERT INTO likes (fk_post_id, liker) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, like.getPost().getId());
            statement.setString(2, like.getLiker());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    like.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public void deleteLike(Long postId) {
        String query = "DELETE FROM likes WHERE fk_post_Id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, postId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }





}
