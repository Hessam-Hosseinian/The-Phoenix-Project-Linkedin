package com.nessam.server.dataAccess;

import com.nessam.server.models.Comment;
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
        String sql = "CREATE TABLE IF NOT EXISTS likes (" + "id BIGINT AUTO_INCREMENT PRIMARY KEY," + "liker VARCHAR(255) NOT NULL," + "fk_post_Id BIGINT NOT NULL, " + "FOREIGN KEY (fk_post_Id) REFERENCES posts(post_Id)" + ")";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.executeUpdate();
    }

    public List<Like> getAllLikes(Long postId) {
        List<Like> likes = new ArrayList<>();
        String query = "SELECT liker FROM likes WHERE fk_post_Id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, postId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    likes.add(mapResultSetToLike(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return likes;
    }

    private Like mapResultSetToLike(ResultSet resultSet) throws SQLException {
        Like like = new Like();
//        like.setId(resultSet.getLong("id"));
        like.setLiker(resultSet.getString("liker"));
        PostDAO postDAO = new PostDAO();
//        like.setPost(postDAO.getPostById(resultSet.getLong("fk_post_Id")));

        return like;
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

    public void deleteLike(String userEmail, Long postId) {
        String query = "DELETE FROM likes WHERE fk_post_Id = ? AND liker =?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, postId);
            statement.setString(2, userEmail);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isUserLikedPost(String userEmail, Long postId) {
        String query = "SELECT COUNT(*) FROM likes WHERE liker = ? AND fk_post_Id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userEmail);
            stmt.setLong(2, postId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0; // If count > 0, user has liked the post
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false; // Return false if no records found
    }

}
