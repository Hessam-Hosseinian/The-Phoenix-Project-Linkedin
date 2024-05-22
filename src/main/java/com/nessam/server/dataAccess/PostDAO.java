package com.nessam.server.dataAccess;

import com.nessam.server.models.Post;
import com.nessam.server.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {

    private final Connection connection;

    public PostDAO() throws SQLException {

        connection = DatabaseConnectionManager.getConnection();
        createPostTable();

    }


    public void createPostTable() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS posts (" +
                        "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                        "title VARCHAR(255) NOT NULL, " +
                        "content TEXT NOT NULL, " +
                        "dateCreated DATETIME NOT NULL, " +
                        "author VARCHAR(255) NOT NULL, " +
                        "likes INT NOT NULL DEFAULT 0, " +
                        "dislikes INT NOT NULL DEFAULT 0" +
                        ")"
        );
        statement.executeUpdate();
    }


    public void savePost(Post post) throws SQLException {
        String query = "INSERT INTO posts (title, content, dateCreated, author, likes, dislikes) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getContent());
            statement.setString(3, post.getDateCreated());
            statement.setString(4, post.getAuthor());
            statement.setInt(5, post.getLikes());
            statement.setInt(6, post.getDislikes());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public List<Post> getAllPosts() throws SQLException {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT * FROM posts";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                posts.add(mapResultSetToPost(resultSet));
            }
        }
        return posts;
    }

    public Post getPostByEmail(String email) throws SQLException {
        String query = "SELECT * FROM posts WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToPost(resultSet);
                } else {
                    return null;
                }
            }
        }
    }

    public void updatePost(Post post) throws SQLException {
        String query = "UPDATE posts SET title = ?, content = ?, likes = ?, dislikes = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getContent());
            statement.setInt(3, post.getLikes());
            statement.setInt(4, post.getDislikes());
            statement.setLong(5, post.getId());
            statement.executeUpdate();
        }
    }

    public void deleteAllPosts() throws SQLException {
        String query = "DELETE FROM posts";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        }
    }

    public void deletePostById(Long id) throws SQLException {
        String query = "DELETE FROM posts WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    private Post mapResultSetToPost(ResultSet resultSet) throws SQLException {
        Post post = new Post();
        post.setId(resultSet.getLong("id"));
        post.setTitle(resultSet.getString("title"));
        post.setContent(resultSet.getString("content"));
        post.setDateCreated(resultSet.getString("dateCreated"));
        post.setLikes(resultSet.getInt("likes"));
        post.setDislikes(resultSet.getInt("dislikes"));

        // Assuming you have a method to get a User by their ID
        long authorId = resultSet.getLong("author_id");
        UserDAO userDAO = new UserDAO();
//        User author = userDAO.getUserById(authorId);
//        post.setAuthor(author);

        return post;
    }

    public List<Post> getPostsForUserFeed(Long userId) {
        return null;
    }
}
