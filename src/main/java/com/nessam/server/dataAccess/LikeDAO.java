package com.nessam.server.dataAccess;

import com.nessam.server.models.Like;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LikeDAO {
    private final Connection connection;

    public LikeDAO() throws SQLException {
        connection = DatabaseConnectionManager.getConnection();
        createLikeTable();
    }

    public void createLikeTable() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS likes (liker VARCHAR(36), likes VARCHAR(36))");
        statement.executeUpdate();

    }
    public void insertLike(Like like) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO likes (liker, liked) VALUES (?, ?)");
        statement.setString(1, like.getLiker());
        statement.setString(2, like.getLiked());
        statement.executeUpdate();
    }

    public void deleteLike(Like like) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM likes WHERE liker = ? AND liked = ?");
        statement.setString(1, like.getLiker());
        statement.setString(2, like.getLiked());
        statement.executeUpdate();
    }

    public void deleteAllLikes() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM likes");
        statement.executeUpdate();
    }

    public List<Like> getLikes(String liker) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM likes WHERE liker = ?");
        statement.setString(1, liker);
        ResultSet resultSet = statement.executeQuery();
        List<Like> likes = new ArrayList<>();
        while (resultSet.next()) {
            Like like = new Like(resultSet.getString("liker"), resultSet.getString("liked"));
            likes.add(like);
        }
        return likes;
    }

    public List<Like> getLikers (String liker) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM likes WHERE liker = ?");
        statement.setString(1, liker);
        ResultSet resultSet = statement.executeQuery();
        List<Like> likes = new ArrayList<>();
        while (resultSet.next()) {
            Like like = new Like(resultSet.getString("liker"), resultSet.getString("liked"));
            likes.add(like);
        }
        return likes;
    }

    public List<Like> getAllLikes() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM likes");
        ResultSet resultSet = statement.executeQuery();
        List<Like> likes = new ArrayList<>();
        while (resultSet.next()) {
            Like like = new Like(resultSet.getString("liker"), resultSet.getString("liked"));
            likes.add(like);
        }
        return likes;
    }

    public boolean isLiked(String liker, String liked) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM likes WHERE liker = ? AND liked = ?");
        statement.setString(1, liker);
        statement.setString(2, liked);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }
}
