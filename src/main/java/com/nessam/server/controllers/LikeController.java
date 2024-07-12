package com.nessam.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nessam.server.dataAccess.LikeDAO;
import com.nessam.server.models.Like;
import com.nessam.server.models.Post;
import com.nessam.server.utils.BetterLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class LikeController {

    private final LikeDAO likeDAO;
    private final ObjectMapper objectMapper;

    public LikeController() throws SQLException {
        likeDAO = new LikeDAO();
        objectMapper = new ObjectMapper();
    }

    public void saveLike(String likerEmail, Post post) throws SQLException {
        Like like = new Like();

        like.setPost(post);

        like.setLiker(likerEmail);


        likeDAO.insertLike(like);
    }

    public void deleteLike(String userEmail, Long postId) {
        likeDAO.deleteLike(userEmail, postId);
    }

    public String getAllLikes(Long postId) {
        try {
            List<Like> likes = likeDAO.getAllLikes(postId);

            return objectMapper.writeValueAsString(likes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean isUserLikedPost(String userEmail, Long id) {
        return likeDAO.isUserLikedPost(userEmail, id);
    }
}
