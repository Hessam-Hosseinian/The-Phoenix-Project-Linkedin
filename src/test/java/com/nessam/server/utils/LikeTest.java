package com.nessam.server.utils;

import com.nessam.server.controllers.LikeController;
import com.nessam.server.controllers.PostController;
import com.nessam.server.handlers.modelHandlers.LikeHandler;
import com.nessam.server.models.Post;
import com.nessam.server.utils.JWTManager;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LikeTest {

    @InjectMocks
    private LikeHandler likeHandler;

    @Mock
    private PostController postController;

    @Mock
    private LikeController likeController;

    @Mock
    private JWTManager jwtManager;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        likeHandler = new LikeHandler();
    }

    @Test
    public void testHandle_GET_AllLikes() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/likes/get"));

        JSONObject requestBody = new JSONObject();
        requestBody.put("postAuthor", "user1");
        requestBody.put("postTitle", "Title");

        when(likeHandler.getRequestBody(exchange)).thenReturn(requestBody);

        when(postController.getPostByAuthorAndTitleAbsolut("user1", "Title")).thenReturn(new Post("1", "user1", "Title", "Content"));

        when(likeController.getAllLikes(1L)).thenReturn("Likes for post with ID 1");

        likeHandler.handle(exchange);

        verify(exchange, times(1)).sendResponseHeaders(200, "Likes for post with ID 1".getBytes().length);
        verify(exchange.getResponseBody(), times(1)).write("Likes for post with ID 1".getBytes());
    }

    @Test
    public void testHandle_POST_SaveLike() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/likes/save"));

        JSONObject requestBody = new JSONObject();
        requestBody.put("postAuthor", "user1");
        requestBody.put("postTitle", "Title");

        when(likeHandler.getRequestBody(exchange)).thenReturn(requestBody);

        when(postController.getPostByAuthorAndTitleAbsolut("user1", "Title")).thenReturn(new Post("1", "user1", "Title", "Content"));

        doNothing().when(likeController).saveLike(anyString(), any(Post.class));

        likeHandler.handle(exchange);

        verify(exchange, times(1)).sendResponseHeaders(200, "Done!".getBytes().length);
        verify(exchange.getResponseBody(), times(1)).write("Done!".getBytes());
    }

    @Test
    public void testHandle_DELETE_DeleteLike() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("DELETE");
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/likes/delete"));

        JSONObject requestBody = new JSONObject();
        requestBody.put("postAuthor", "user1");
        requestBody.put("postTitle", "Title");

        when(likeHandler.getRequestBody(exchange)).thenReturn(requestBody);

        when(postController.getPostByAuthorAndTitleAbsolut("user1", "Title")).thenReturn(new Post("1", "user1", "Title", "Content"));

        doNothing().when(likeController).deleteLike(anyString(), Long.valueOf(anyString()));

        likeHandler.handle(exchange);

        verify(exchange, times(1)).sendResponseHeaders(200, "Successfully unliked the post.".getBytes().length);
        verify(exchange.getResponseBody(), times(1)).write("Successfully unliked the post.".getBytes());
    }
}
