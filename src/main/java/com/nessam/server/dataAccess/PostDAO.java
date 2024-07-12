package com.nessam.server.dataAccess;

import com.nessam.server.models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {

    private final Connection connection;

    public PostDAO() throws SQLException {
        connection = DatabaseConnectionManager.getConnection();
        createTables();
    }

    private void createTables() throws SQLException {
        createTable("CREATE TABLE IF NOT EXISTS posts (" + "post_Id BIGINT AUTO_INCREMENT PRIMARY KEY, " + "title VARCHAR(255) NOT NULL, " + "content TEXT, " + "file_path TEXT, " + "dateCreated TEXT, " + "author VARCHAR(255))");

    }

    private void createTable(String query) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
        }
    }

    public void savePost(Post post) throws SQLException {
        String query = "INSERT INTO posts (title, content, file_path, dateCreated, author) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getContent());
            preparedStatement.setString(3, post.getFilePath());
            preparedStatement.setString(4, post.getDateCreated());
            preparedStatement.setString(5, post.getAuthor());
            preparedStatement.executeUpdate();
        }
    }

    public void updatePost(Post post) throws SQLException {
        String query = "UPDATE posts SET content = ? WHERE author = ? AND title = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, post.getContent());
            preparedStatement.setString(2, post.getAuthor());
            preparedStatement.setString(3, post.getTitle());
            preparedStatement.executeUpdate();
        }
    }

    public Post getPostByAuthorAndTitle(String author, String title) throws SQLException {
        String query = "SELECT * FROM posts WHERE author = ? AND title = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, author);
            preparedStatement.setString(2, title);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToPost(resultSet);
                } else {
                    return null;
                }
            }
        }
    }

    public List<Post> getAllPosts() throws SQLException {
        String query = "SELECT * FROM posts";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            List<Post> posts = new ArrayList<>();
            while (resultSet.next()) {
                Post post = mapResultSetToPost(resultSet);
                post.setLikes(getPostLikes(post.getId()));
                posts.add(post);

            }
            return posts;
        }
    }

    public List<Post> getPostsByAuthor(String author) throws SQLException {
        String query = "SELECT * FROM posts WHERE author = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, author);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Post> posts = new ArrayList<>();
                while (resultSet.next()) {
                    posts.add(mapResultSetToPost(resultSet));
                }
                return posts;
            }
        }
    }

    public List<Like> getPostLikes(long userId) throws SQLException {
        String educationSql = "SELECT * FROM likes WHERE fk_post_Id = ?";
        List<Like> likes = new ArrayList<>();

        try (PreparedStatement educationStmt = connection.prepareStatement(educationSql)) {
            educationStmt.setLong(1, userId);
            try (ResultSet resultSet = educationStmt.executeQuery()) {
                while (resultSet.next()) {
                    Like like = new Like();
                    like.setLiker(resultSet.getString("liker"));
                    likes.add(like);


                }
            }
        }
        return likes;
    }

    public Post getPostById(Long Id) throws SQLException {
        String query = "SELECT * FROM posts WHERE Id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, Id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Post post = mapResultSetToPost(resultSet);


                return post;
            }
        }
    }

    public void deletePostByAuthorAndTitle(String author, String title) throws SQLException {
        String query = "DELETE FROM posts WHERE author = ? AND title = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, author);
            preparedStatement.setString(2, title);
            preparedStatement.executeUpdate();
        }
    }

    public void deleteAllPosts() throws SQLException {
        String query = "DELETE FROM posts";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        }
    }

    private Post mapResultSetToPost(ResultSet resultSet) throws SQLException {
        Post post = new Post();
        post.setId(resultSet.getLong("post_Id"));
        post.setTitle(resultSet.getString("title"));
        post.setContent(resultSet.getString("content"));
        post.setFilePath(resultSet.getString("file_path"));
        post.setDateCreated(resultSet.getString("dateCreated"));
        post.setAuthor(resultSet.getString("author"));

        return post;
    }

    public List<Post> searchInPosts(String keyword) throws SQLException {
        List<Post> results = new ArrayList<>();
        String sql = "SELECT post_id, title, content, file_path, dateCreated, author FROM posts WHERE title LIKE ? ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + keyword + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Post post = mapResultSetToPost(resultSet);
                    results.add(post);
                }
            }
        }
        return results;
    }


    public List<Post> searchInPostsContent(String keyword) throws SQLException {
        List<Post> results = new ArrayList<>();
        String sql = "SELECT post_id, title, content, file_path, dateCreated, author FROM posts WHERE content LIKE ? ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + keyword + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Post post = mapResultSetToPost(resultSet);
                    results.add(post);
                }
            }
        }
        return results;
    }
}
