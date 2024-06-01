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

    public LikeController() throws SQLException, ClassNotFoundException {
        likeDAO = new LikeDAO();
        objectMapper = new ObjectMapper();
    }

    public void createLikeTable() throws SQLException {
        likeDAO.createLikeTable();

    }

    public void saveLike(Long postId, String liker) {
        Like like = new Like();
        like.getPost().setId(postId);
        like.setLiker(liker);
        likeDAO.insertLike(like);
    }

    public void deleteLike(String likerId, Long postId) {
        likeDAO.deleteLike(likerId, postId);
    }

    public String getAllLikes(Long postId) {
        try {
            List<Like> likes = likeDAO.getAllLikes(postId);  // Passing postId to DAO
            return objectMapper.writeValueAsString(likes);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to convert likes to JSON";
        }
    }
    //this is a test comment
}
