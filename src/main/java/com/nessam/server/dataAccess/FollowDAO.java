package com.nessam.server.dataAccess;

import com.nessam.server.models.Follow;
import com.nessam.server.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FollowDAO {
    private final Connection connection;

    public FollowDAO() throws SQLException {
        connection = DatabaseConnectionManager.getConnection();
        createFollowTable();
    }

    public void createFollowTable() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS follows (id  BIGINT AUTO_INCREMENT PRIMARY KEY,follower VARCHAR(36), followed VARCHAR(36))");
        preparedStatement.executeUpdate();
    }

    public void saveFollow(Follow follow) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO follows (follower, followed) VALUES (?, ?)");
        preparedStatement.setString(1, follow.getFollower());
        preparedStatement.setString(2, follow.getFollowed());
        preparedStatement.executeUpdate();
    }

    public void deleteFollow(Follow follow) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM follows WHERE follower = ? AND followed = ?");
        preparedStatement.setString(1, follow.getFollower());
        preparedStatement.setString(2, follow.getFollowed());
        preparedStatement.executeUpdate();
    }

    public void deleteAllFollows() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM follows");
        preparedStatement.executeUpdate();
    }


    public List<Follow> getFollows(String userId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM follows WHERE follower = ?");
        preparedStatement.setString(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Follow> follows = new ArrayList<>();
        while (resultSet.next()) {
            Follow follow = new Follow();
            follow.setFollower(resultSet.getString("follower"));
            follow.setFollowed(resultSet.getString("followed"));
            follows.add(follow);
        }
        return follows;
    }

    public List<Follow> getFollowers(String userId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM follows WHERE followed = ?");
        preparedStatement.setString(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Follow> follows = new ArrayList<>();
        while (resultSet.next()) {
            Follow follow = new Follow();
            follow.setFollower(resultSet.getString("follower"));
            follow.setFollowed(resultSet.getString("followed"));
            follows.add(follow);
        }
        return follows;
    }

    public List<Follow> getAllFollow() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM follows");
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Follow> follows = new ArrayList<>();
        while (resultSet.next()) {
            Follow follow = new Follow();
            follow.setFollower(resultSet.getString("follower"));
            follow.setFollowed(resultSet.getString("followed"));
            follows.add(follow);
        }
        return follows;
    }


    public boolean isFollowing(String follower, String followed) throws SQLException {
        String query = "SELECT COUNT(*) FROM follows WHERE follower = ? AND followed = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, follower);
            statement.setString(2, followed);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }


}
