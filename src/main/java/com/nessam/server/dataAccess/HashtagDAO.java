//package com.nessam.server.dataAccess;
//
//import com.nessam.server.models.Post;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class HashtagDAO {
//
//    private final Connection connection;
//
//    public HashtagDAO() throws SQLException {
//        connection = DatabaseConnectionManager.getConnection();
//        createHashtagTable();
//    }
//
//    public void Drop() throws SQLException {
//        PreparedStatement statement = connection.prepareStatement("DROP TABLE tags");
//        statement.executeUpdate();
//    }
//
//    public void createHashtagTable() throws SQLException {
//        String sql = "CREATE TABLE IF NOT EXISTS hashtags (" +
//                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
//                "post_id BIGINT NOT NULL," +
//                "hashtag VARCHAR(255) NOT NULL," +
//                "FOREIGN KEY (post_id) REFERENCES posts(post_Id)" +
//                ")";
//        PreparedStatement statement = connection.prepareStatement(sql);
//        statement.executeUpdate();
//    }
//
//
//    public boolean tagExists (Long postId, String tagName) throws SQLException {
//        String query = "SELECT * FROM hashtags WHERE post_Id = ? AND hashtag = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setLong(1, postId);
//            stmt.setString(2, tagName);
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//    public void addHashtag(Long postId, String hashtag) throws SQLException {
//        String sql = "INSERT INTO hashtags (post_id, hashtag) VALUES (?, ?)";
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setLong(1, postId);
//            statement.setString(2, hashtag);
//            statement.executeUpdate();
//        }
//    }
//
//    public String deleteHashtag(Long postId, String hashtag) throws SQLException {
//        String sql = "DELETE FROM hashtags WHERE hashtag = ? AND post_id = ?";
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setString(1, hashtag);
//            statement.setLong(2, postId);
//            statement.executeUpdate();
//        }
//        return "hashtag deleted";
//    }
//
//
//    public List<String> searchHashtags(String keyword) throws SQLException {
//        List<String> hashtags = new ArrayList<>();
//        String sql = "SELECT hashtag FROM hashtags WHERE hashtag LIKE ?";
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setString(1, "%" + keyword + "%");
//            try (ResultSet resultSet = statement.executeQuery()) {
//                while (resultSet.next()) {
//                    hashtags.add(resultSet.getString("hashtag"));
//                }
//            }
//        }
//        return hashtags;
//    }
//
//
//}
