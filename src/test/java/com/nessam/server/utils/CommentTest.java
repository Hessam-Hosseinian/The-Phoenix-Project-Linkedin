package com.nessam.server.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nessam.server.controllers.CommentController;
import com.nessam.server.controllers.PostController;
import com.nessam.server.handlers.modelHandlers.CommentHandler;
import com.nessam.server.models.Post;
import com.nessam.server.utils.JWTManager;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CommentTest {

    @InjectMocks
    private CommentHandler commentHandler;

    @Mock
    private CommentController commentController;

    @Mock
    private PostController postController;

    @Mock
    private JWTManager jwtManager;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        commentHandler = new CommentHandler();
    }

    @Test
    public void testHandle_GET_Success() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/comment/emailOfPost/PostTitle"));
        when(commentController.getCommentsByPostTitle("PostTitle", "emailOfPost")).thenReturn("List of comments");

        commentHandler.handle(exchange);

        verify(exchange, times(1)).sendResponseHeaders(200, "List of comments".getBytes().length);
        verify(exchange.getResponseBody(), times(1)).write("List of comments".getBytes());
    }

    @Test
    public void testHandle_GET_WrongURLFormat() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/comment/wrongformat"));

        commentHandler.handle(exchange);

        verify(exchange, times(1)).sendResponseHeaders(200, "WRONG URL".getBytes().length);
        verify(exchange.getResponseBody(), times(1)).write("WRONG URL".getBytes());
    }

    @Test
    public void testHandle_POST_Success() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/comment/emailOfPost/PostTitle/commentContent"));

        String requestBody = "";
        InputStream inputStream = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));
        when(exchange.getRequestBody()).thenReturn(inputStream);

        when(postController.getPostByAuthorAndTitleAbsolut("emailOfPost", "PostTitle")).thenReturn(new Post());

        doNothing().when(commentController).createComment(anyString(), anyString(), anyString(), any(Post.class));

        commentHandler.handle(exchange);

        verify(exchange, times(1)).sendResponseHeaders(200, "Done!".getBytes().length);
        verify(exchange.getResponseBody(), times(1)).write("Done!".getBytes());
    }

    @Test
    public void testHandle_POST_InvalidRequestFormat() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/comment/emailOfPost"));

        commentHandler.handle(exchange);

        verify(exchange, times(1)).sendResponseHeaders(200, "Invalid request format".getBytes().length);
        verify(exchange.getResponseBody(), times(1)).write("Invalid request format".getBytes());
    }

    @Test
    public void testHandle_DELETE_Success() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("DELETE");
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/comment/123"));
        commentHandler.handle(exchange);

        verify(exchange, times(1)).sendResponseHeaders(200, "Comments deleted!".getBytes().length);
        verify(exchange.getResponseBody(), times(1)).write("Comments deleted!".getBytes());
    }

    @Test
    public void testHandle_DELETE_InvalidPostIdFormat() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("DELETE");
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/comment/invalidformat"));

        commentHandler.handle(exchange);

        verify(exchange, times(1)).sendResponseHeaders(200, "Invalid post ID format".getBytes().length);
        verify(exchange.getResponseBody(), times(1)).write("Invalid post ID format".getBytes());
    }
}
