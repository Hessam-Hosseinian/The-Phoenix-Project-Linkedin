package com.nessam.server.dataAccess;

import com.nessam.server.models.Comment;
import com.nessam.server.models.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    private final Connection connection;

    public CommentDAO() throws SQLException {
        connection = DatabaseConnectionManager.getConnection();
//        createCommentTable();
    }

    private void createCommentTable() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS comments (" +
                        "comment_Id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "content TEXT NOT NULL, " +
                        "file_path TEXT, " +
                        "dateCreated TEXT , " +
                        "author VARCHAR(255) , " +
                        "fk_post_Id BIGINT NOT NULL, " +
                        "FOREIGN KEY (fk_post_Id) REFERENCES posts(post_Id)" +
                        ")"
        );
        statement.executeUpdate();
    }

    public void saveComment(Comment comment) throws SQLException {
        String query = "INSERT INTO comments (content, file_path, dateCreated, author, fk_post_Id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, comment.getContent());
            statement.setString(2, comment.getFilePath());
            statement.setString(3, comment.getDateCreated());
            statement.setString(4, comment.getAuthor());
            statement.setLong(5, comment.getPost().getId());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    comment.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public List<Comment> getCommentsByPostId(long postId) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT * FROM comments WHERE fk_post_Id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, postId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    comments.add(mapResultSetToComment(resultSet));
                }
            }
        }
        return comments;
    }

    public void deleteCommentsByPostId(long postId) throws SQLException {
        String query = "DELETE FROM comments WHERE fk_post_Id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, postId);
            statement.executeUpdate();
        }
    }

    private Comment mapResultSetToComment(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();
        comment.setId(resultSet.getLong("comment_Id"));
        comment.setContent(resultSet.getString("content"));
        comment.setFilePath(resultSet.getString("file_path"));
        comment.setDateCreated(resultSet.getString("dateCreated"));
        comment.setAuthor(resultSet.getString("author"));

        Post post = new Post();
        post.setId(resultSet.getLong("fk_post_Id"));
        comment.setPost(post);

        return comment;
    }
}
