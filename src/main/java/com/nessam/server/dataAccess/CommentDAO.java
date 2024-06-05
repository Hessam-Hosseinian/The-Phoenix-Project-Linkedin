package com.nessam.server.dataAccess;

import com.nessam.server.models.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    private final Connection connection;

    public CommentDAO() throws SQLException {
        connection = DatabaseConnectionManager.getConnection();
        createCommentTable();
    }

    public void createCommentTable() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS comments (" + "comment_Id BIGINT AUTO_INCREMENT PRIMARY KEY, " + "content TEXT, " + "file_path TEXT, " + "dateCreated TEXT, " + "author VARCHAR(255), " + "fk_post_Id BIGINT NOT NULL, " + "FOREIGN KEY (fk_post_Id) REFERENCES posts(post_Id)" + ")");
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
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    comments.add(mapResultSetToComment(rs));
                }
            }
        }
        return comments;
    }

    public Comment getCommentById(long commentId) throws SQLException {
        String query = "SELECT * FROM comments WHERE comment_Id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, commentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToComment(rs);
                }
            }
        }
        return null; // Return null if no comment with the given ID is found
    }

    public List<Comment> getCommentsByAuthor(String author) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT * FROM comments WHERE author = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, author);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    comments.add(mapResultSetToComment(rs));
                }
            }
        }
        return comments;
    }

    public void updateComment(Comment comment) throws SQLException {
        String query = "UPDATE comments SET content = ?, file_path = ? WHERE comment_Id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, comment.getContent());
            stmt.setString(2, comment.getFilePath());
            stmt.setLong(3, comment.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteCommentById(long commentId) throws SQLException {
        String query = "DELETE FROM comments WHERE comment_Id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, commentId);
            stmt.executeUpdate();
        }
    }

    public void deleteCommentsByPostId(long postId) throws SQLException {
        String query = "DELETE FROM comments WHERE fk_post_Id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, postId);
            stmt.executeUpdate();
        }
    }

    private Comment mapResultSetToComment(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();
        comment.setId(resultSet.getLong("comment_Id"));
        comment.setContent(resultSet.getString("content"));
        comment.setFilePath(resultSet.getString("file_path"));
        comment.setDateCreated(resultSet.getString("dateCreated"));
        comment.setAuthor(resultSet.getString("author"));
        // Assuming you have a method to get Post by Id in PostDAO
        PostDAO postDAO = new PostDAO();
        comment.setPost(postDAO.getPostById(resultSet.getLong("fk_post_Id")));

        return comment;
    }
}
