package com.nessam.server.controllers;


import com.nessam.server.dataAccess.HashtagDAO;
import java.sql.SQLException;
import java.util.ArrayList;

public class HashtagController {
    private final HashtagDAO hashtagDAO;

    public HashtagController() throws SQLException {
        hashtagDAO = new HashtagDAO();
    }

    public void addHashtag(String id, String post) throws SQLException {
        hashtagDAO.saveHashtag(id, post);
    }

    public void deleteAll() throws SQLException {
        hashtagDAO.deleteAll();
    }

    public void deleteOne (String id) throws SQLException {
        hashtagDAO.deleteOne(id);
    }

    public String GetHashtag(String id) throws SQLException {
        ArrayList<String> posts = hashtagDAO.getHashtag(id);
        StringBuilder response = new StringBuilder();
        for (int i = 0; i < posts.size(); i++) {
            if (i > 0) response.append(',');
            response.append(posts.get(i));
        }
        return response.toString();
    }
}
