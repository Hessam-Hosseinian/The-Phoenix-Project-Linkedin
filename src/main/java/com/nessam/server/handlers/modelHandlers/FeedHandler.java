package com.nessam.server.handlers.modelHandlers;

import com.nessam.server.dataAccess.PostDAO;
import com.nessam.server.models.Post;

import java.util.List;

public class FeedHandler {
    private PostDAO postDAO;

    public FeedHandler(PostDAO postDAO) {
        this.postDAO = postDAO;
    }

//    public List<Post> getFeedForUser(Long userId) {
//        return postDAO.getPostsForUserFeed(userId);
//    }
}
//this is a test comment


