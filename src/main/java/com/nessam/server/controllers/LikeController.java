package com.nessam.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nessam.server.dataAccess.LikeDAO;
import com.nessam.server.models.Like;
import org.hibernate.annotations.processing.SQL;

import java.sql.SQLException;
import java.util.List;

public class LikeController {

    private final LikeDAO likeDAO;
    private final ObjectMapper objectMapper;

    public LikeController() throws SQLException {
        likeDAO = new LikeDAO();
        objectMapper = new ObjectMapper();
    }

    public void createLikeTable() throws SQLException {
        likeDAO.createLikeTable();
    }

    public void saveLike(String liker, String liked) throws  SQLException {
        Like like = new Like(liker, liked);
        likeDAO.insertLike(like);
    }

    public void deleteLike(String liker, String liked) throws SQLException {
        Like like = new Like(liker, liked);
        likeDAO.deleteLike(like);
    }

    public void deleteAllLikes() throws SQLException {
        likeDAO.deleteAllLikes();
    }

    public String getLikes(String liker) throws SQLException, JsonProcessingException {
        List<Like> likes = likeDAO.getLikes(liker);
        return objectMapper.writeValueAsString(likes);
    }

    public String getLikers(String liker) throws SQLException, JsonProcessingException {
        List<Like> likes = likeDAO.getAllLikes();
        return objectMapper.writeValueAsString(likes);
    }

    public String getAllLikes() throws SQLException, JsonProcessingException {
        List<Like> likes = likeDAO.getAllLikes();
        return objectMapper.writeValueAsString(likes);
    }
}
