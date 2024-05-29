package com.nessam.server.dataAccess;

import com.nessam.server.models.Comment;
import com.nessam.server.models.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {

    private final Connection connection;

    public PostDAO() throws SQLException {
        connection = DatabaseConnectionManager.getConnection();
        createPostTable();
        createCommentTable();
    }

    public void createPostTable() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS posts (" + "post_Id BIGINT AUTO_INCREMENT PRIMARY KEY, " + "title VARCHAR(255) NOT NULL, " + "content TEXT, " + "file_path TEXT, " + "dateCreated TEXT, " + "author VARCHAR(255), " + "likes INT, " + "dislikes INT)");
        statement.executeUpdate();
    }

    public void createCommentTable() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS comments (" + "comment_Id BIGINT AUTO_INCREMENT PRIMARY KEY, " + "content TEXT, " + "file_path TEXT, " + "dateCreated TEXT, " + "author VARCHAR(255), " + "fk_post_Id BIGINT NOT NULL, " + "FOREIGN KEY (fk_post_Id) REFERENCES posts(post_Id))");
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
                Post post = mapResultSetToPost(resultSet);
                post.setComments(getCommentsByPostId(post.getId()));
                posts.add(post);
            }
        }
        return posts;
    }

    public Post getPostById(long postId) throws SQLException {
        String query = "SELECT * FROM posts WHERE post_Id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Post post = mapResultSetToPost(rs);
                    post.setComments(getCommentsByPostId(postId));
                    return post;
                }
            }
        }
        return null; // Return null if no post with the given ID is found
    }

    public List<Post> getPostsByAuthor(String author) throws SQLException {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT * FROM posts WHERE author = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, author);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Post post = mapResultSetToPost(rs);
                    post.setComments(getCommentsByPostId(post.getId()));
                    posts.add(post);
                }
            }
        }
        return posts;
    }

    public Post getPostByAuthorAndTitle(String author, String title) throws SQLException {
        String query = "SELECT * FROM posts WHERE author = ? AND title = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, author);
            stmt.setString(2, title);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Post post = mapResultSetToPost(rs);
                    post.setComments(getCommentsByPostId(post.getId()));
                    return post;
                }
            }
        }
        return null;
    }

    public void updatePost(Post post) throws SQLException {
        String query = "UPDATE posts SET content = ? WHERE author = ? AND title = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, post.getContent());
            stmt.setString(2, post.getAuthor());
            stmt.setString(3, post.getTitle());
            stmt.executeUpdate();
        }
    }

    public void deleteAllPosts() throws SQLException {
        String query = "DELETE FROM posts";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
        }
    }

    public void deletePostByAuthorAndTitle(String author, String title) throws SQLException {
        String query = "DELETE FROM posts WHERE author = ? AND title = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, author);
            stmt.setString(2, title);
            stmt.executeUpdate();
        }
    }

    private List<Comment> getCommentsByPostId(long postId) throws SQLException {
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

    private Comment mapResultSetToComment(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();
        comment.setId(resultSet.getLong("comment_Id"));
        comment.setContent(resultSet.getString("content"));
        comment.setDateCreated(resultSet.getString("dateCreated"));
        comment.setAuthor(resultSet.getString("author"));
        comment.setFilePath(resultSet.getString("file_path"));
        // Assuming you set the post manually where needed
        return comment;
    }

    private Post mapResultSetToPost(ResultSet resultSet) throws SQLException {
        Post post = new Post();
        post.setId(resultSet.getLong("post_Id"));
        post.setTitle(resultSet.getString("title"));
        post.setContent(resultSet.getString("content"));
        post.setDateCreated(resultSet.getString("dateCreated"));
        post.setAuthor(resultSet.getString("author"));
        post.setLikes(resultSet.getInt("likes"));
        post.setDislikes(resultSet.getInt("dislikes"));
        return post;
    }
}
