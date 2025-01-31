package com.nessam.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nessam.server.dataAccess.FollowDAO;
import com.nessam.server.models.Follow;

import java.sql.SQLException;
import java.util.List;

public class FollowController {

    private final FollowDAO followDAO;

    public FollowController() throws SQLException {
        followDAO = new FollowDAO();
    }

    public void saveFollow(String follower, String followed) throws SQLException {
        Follow follow = new Follow(follower, followed);
        followDAO.saveFollow(follow);
    }

    public void deleteFollow(String follower, String followed) throws SQLException {
        Follow follow = new Follow(follower, followed);
        followDAO.deleteFollow(follow);
    }

    public void deleteAllFollows() throws SQLException {
        followDAO.deleteAllFollows();
    }

    public String getFollows(String userId) throws SQLException, JsonProcessingException {
        List<Follow> follows = followDAO.getFollows(userId);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(follows);
    }

    public String getFollowers(String userId) throws SQLException, JsonProcessingException {
        List<Follow> follows = followDAO.getFollowers(userId);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(follows);
    }

    public String getAllFollow() throws SQLException, JsonProcessingException {
        List<Follow> follows = followDAO.getAllFollow();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(follows);
    }

    public boolean isFollowing(String follower, String followed) throws SQLException {
        return followDAO.isFollowing(follower, followed);
    }
}
