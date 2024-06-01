package com.nessam.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nessam.server.dataAccess.CommentDAO;
import com.nessam.server.models.Comment;
import com.nessam.server.models.Post;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CommentController {

    private final CommentDAO commentDAO;
    private final ObjectMapper objectMapper;

    public CommentController() throws SQLException {
        this.commentDAO = new CommentDAO();
        this.objectMapper = new ObjectMapper();
    }

    public String getCommentsByPostId(long postId) throws SQLException, JsonProcessingException {
        List<Comment> comments = commentDAO.getCommentsByPostId(postId);
        return objectMapper.writeValueAsString(comments);
    }

    public void createComment(String content, String filePath, String author, Post post) throws SQLException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String dateCreated = formatter.format(new Date());
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setFilePath(filePath);
        comment.setAuthor(author);
        comment.setDateCreated(dateCreated);
        comment.setPost(post);
        commentDAO.saveComment(comment);
    }

    public void deleteCommentsByPostId(long postId) throws SQLException {
        commentDAO.deleteCommentsByPostId(postId);
    }
    //this is a test comment
}
