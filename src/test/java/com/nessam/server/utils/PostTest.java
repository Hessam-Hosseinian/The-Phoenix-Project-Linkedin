package com.nessam.server.utils;

import com.nessam.server.controllers.PostController;
import com.nessam.server.handlers.modelHandlers.PostHandler;
import com.nessam.server.utils.BetterLogger;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PostTest {

    @InjectMocks
    private PostHandler postHandler;

    @Mock
    private PostController postController;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        postHandler = new PostHandler();
    }

    @Test
    public void testHandleGetRequest_AllPosts() throws Exception {
        String[] splittedPath = {"/api", "posts"};
        when(postController.getPosts()).thenReturn("all posts");

        String response = postHandler.handleGetRequest(splittedPath);

        assertEquals("all posts", response);
        verify(postController, times(1)).getPosts();
    }

    @Test
    public void testHandleGetRequest_PostsByAuthor() throws Exception {
        String[] splittedPath = {"/api", "posts", "user1"};
        when(postController.getPostByAuthor("user1")).thenReturn("posts by user1");

        String response = postHandler.handleGetRequest(splittedPath);

        assertEquals("posts by user1", response);
        verify(postController, times(1)).getPostByAuthor("user1");
    }

    @Test
    public void testHandleGetRequest_PostByAuthorAndTitle() throws Exception {
        String[] splittedPath = {"/api", "posts", "user1", "title1"};
        when(postController.getPostByAuthorAndTitle("user1", "title1")).thenReturn("post by user1 with title1");

        String response = postHandler.handleGetRequest(splittedPath);

        assertEquals("post by user1 with title1", response);
        verify(postController, times(1)).getPostByAuthorAndTitle("user1", "title1");
    }

    @Test
    public void testHandleGetRequest_WrongURLFormat() throws Exception {
        String[] splittedPath = {"/api", "posts", "invalid"};
        String response = postHandler.handleGetRequest(splittedPath);

        assertEquals("WRONG URL", response);
    }

    @Test
    public void testHandlePostRequest_CreateNewPost() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        String requestBody = "{\"title\":\"New Post\", \"content\":\"This is a new post.\", \"file_path\":\"/uploads/new_post.txt\"}";
        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream(requestBody.getBytes()));
        postHandler.setUserEmail("user1");
        when(postController.getPostByAuthorAndTitle("user1", "New Post")).thenReturn("No Post");

        String response = postHandler.handlePostRequest(exchange);

        assertEquals("Done!", response);
        verify(postController, times(1)).createPost("New Post", "This is a new post.", "user1", "/uploads/new_post.txt");
    }

    @Test
    public void testHandlePostRequest_DuplicatePostTitle() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        String requestBody = "{\"title\":\"Existing Post\", \"content\":\"This is an existing post.\", \"file_path\":\"/uploads/existing_post.txt\"}";
        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream(requestBody.getBytes()));
        postHandler.setUserEmail("user1");
        when(postController.getPostByAuthorAndTitle("user1", "Existing Post")).thenReturn("Existing Post");

        String response = postHandler.handlePostRequest(exchange);

        assertEquals("This post is already saved, use another title!", response);
        verify(postController, never()).createPost(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void testHandlePutRequest_UpdatePost() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        String requestBody = "{\"title\":\"Existing Post\", \"content\":\"Updated content.\"}";
        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream(requestBody.getBytes()));
        postHandler.setUserEmail("user1");
        when(postController.getPostByAuthorAndTitle("user1", "Existing Post")).thenReturn("Existing Post");

        String response = postHandler.handlePutRequest(exchange);

        assertEquals("Post updated!", response);
        verify(postController, times(1)).updatePost("user1", "Existing Post", "Updated content.");
    }

    @Test
    public void testHandlePutRequest_PostNotFound() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        String requestBody = "{\"title\":\"Non Existing Post\", \"content\":\"Updated content.\"}";
        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream(requestBody.getBytes()));
        postHandler.setUserEmail("user1");
        when(postController.getPostByAuthorAndTitle("user1", "Non Existing Post")).thenReturn("No Post");

        String response = postHandler.handlePutRequest(exchange);

        assertEquals("Post not found!", response);
        verify(postController, never()).updatePost(anyString(), anyString(), anyString());
    }

    @Test
    public void testHandleDeleteRequest_DeleteAllPosts() throws Exception {
        String[] splittedPath = {"/api", "posts"};
        String response = postHandler.handleDeleteRequest(splittedPath);

        assertEquals("All posts deleted!", response);
        verify(postController, times(1)).deletePosts();
    }

    @Test
    public void testHandleDeleteRequest_DeletePostByAuthorAndTitle() throws Exception {
        String[] splittedPath = {"/api", "posts", "user1", "Existing Post"};
        String response = postHandler.handleDeleteRequest(splittedPath);

        assertEquals("Post deleted!", response);
        verify(postController, times(1)).deletePostByAuthorAndTitle("user1", "Existing Post");
    }

    @Test
    public void testHandleDeleteRequest_InvalidFormat() throws Exception {
        String[] splittedPath = {"/api", "posts", "invalid"};
        String response = postHandler.handleDeleteRequest(splittedPath);

        assertEquals("Invalid request format", response);
        verify(postController, never()).deletePosts();
        verify(postController, never()).deletePostByAuthorAndTitle(anyString(), anyString());
    }

}
