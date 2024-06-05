package com.nessam.server.controllers;


import com.nessam.server.dataAccess.HashtagDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HashtagController {
    private final HashtagDAO hashtagDAO;

    public HashtagController() throws SQLException {
        hashtagDAO = new HashtagDAO();
    }

    public void addHashtag(Long id, String post) throws SQLException {
        hashtagDAO.addHashtag(id, post);
    }

    public String deleteHashtag(Long postId, String hashtag) throws SQLException {
        hashtagDAO.deleteHashtag(postId, hashtag);
        return "tag deleted successfully";
    }

    public String searchHashtag(String hashtag) throws SQLException {
        List<String> posts = hashtagDAO.searchHashtags(hashtag);
        StringBuilder response = new StringBuilder();
        for (int i = 0; i < posts.size(); i++) {
            response.append('#');
            response.append(posts.get(i));
            response.append('\n');
        }
        return response.toString();
    }

    public boolean tagExists (Long postId, String tagName) throws SQLException {
        return hashtagDAO.tagExists(postId, tagName);
    }
}
