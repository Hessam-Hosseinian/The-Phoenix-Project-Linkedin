package com.nessam.server.dataAccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class HashtagDAO {

    private final Connection connection;

    public HashtagDAO() throws SQLException {
        connection = DatabaseConnectionManager.getConnection();
        createHashtagTable();
    }

    public void Drop() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DROP TABLE tags");
        statement.executeUpdate();
    }

    public void createHashtagTable() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS tags (id VARCHAR(280) , post VARCHAR(36), PRIMARY KEY (id, post))");
        statement.executeUpdate();
    }

    public void saveHashtag(String id, String post) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO tags (id, post) VALUES (?, ?)");
        statement.setString(1, id);
        statement.setString(2, post);
        statement.executeUpdate();
    }

    public void deleteAll() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM tags");
        statement.executeUpdate();
    }

    public ArrayList<String> getHashtag(String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM tags WHERE id = ?");
        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<String> posts = new ArrayList<>();
        while (resultSet.next()) {
            posts.add(resultSet.getString("post"));
        }
        return posts;
    }

    public ArrayList<String> getTags(String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT id FROM tags WHERE post = ?");
        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<String> tags = new ArrayList<>();
        while (resultSet.next()) {
            tags.add(resultSet.getString("id"));
        }
        return tags;
    }

    public void deleteOne(String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM tags WHERE id = ?");
        statement.setString(1, id);
        statement.executeUpdate();
    }

}
