package com.nessam.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nessam.server.dataAccess.PostDAO;
import com.nessam.server.models.Post;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PostController {

    private final PostDAO postDAO;
    private final ObjectMapper objectMapper;

    public PostController() throws SQLException {
        this.postDAO = new PostDAO();
        this.objectMapper = new ObjectMapper();
    }

    public void createPost(String title, String content, String author, String filePath) throws SQLException {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String dateCreated = formatter.format(new Date());
        post.setDateCreated(dateCreated);
        post.setFilePath(filePath);
        postDAO.savePost(post);
    }

    public void createPost(String title, String content, String author) throws SQLException {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String dateCreated = formatter.format(new Date());
        post.setDateCreated(dateCreated);

        postDAO.savePost(post);
    }

    public String getPosts() throws SQLException, JsonProcessingException {
        List<Post> posts = postDAO.getAllPosts();

        return objectMapper.writeValueAsString(posts);
    }

    public String getPostByAuthor(String email) throws SQLException, JsonProcessingException {
        List<Post> posts = postDAO.getPostsByAuthor(email);
        return posts != null ? objectMapper.writeValueAsString(posts) : "No Post";
    }

    public String getPostByAuthorAndTitle(String email, String title) throws SQLException, JsonProcessingException {
        Post posts = postDAO.getPostByAuthorAndTitle(email, title);
        return posts != null ? objectMapper.writeValueAsString(posts) : "No Post";
    }

    public Post getPostByAuthorAndTitleAbsolut(String email, String title) throws SQLException, JsonProcessingException {

        Post post = postDAO.getPostByAuthorAndTitle(email, title);

        return post;
    }


    public void updatePost(String email, String title, String content) throws SQLException {
        Post post = postDAO.getPostByAuthorAndTitle(email, title);
        if (post != null) {

            post.setContent(content);
            postDAO.updatePost(post);
        } else {
            throw new SQLException("Post not found");
        }
    }

    public void deletePosts() {
        try {
            postDAO.deleteAllPosts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void deletePostByAuthorAndTitle(String author, String title) {
        try {
            postDAO.deletePostByAuthorAndTitle(author, title);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Post getPost(String id) throws SQLException {
        return postDAO.getPostById(Long.parseLong(id));
    }

    public String getPostById(String id) throws SQLException, JsonProcessingException {
        Post post = getPost(id);
        if (post == null) return null;
        return objectMapper.writeValueAsString(post);
    }

    public String searchPost(String keyword) throws SQLException, JsonProcessingException {
        List<Post> posts = postDAO.searchInPosts(keyword);
        return objectMapper.writeValueAsString(posts);
    }

    public String searchPostHashtag(String keyword) throws SQLException, JsonProcessingException {
        List<Post> posts = postDAO.searchInPostsContent(keyword);
        return objectMapper.writeValueAsString(posts);
    }

}
