package com.nessam.server.utils;

import com.nessam.server.controllers.FollowController;
import com.nessam.server.controllers.UserController;
import com.nessam.server.handlers.modelHandlers.FollowHandler;
import com.nessam.server.utils.JWTManager;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FollowTest {

    @InjectMocks
    private FollowHandler followHandler;

    @Mock
    private FollowController followController;

    @Mock
    private UserController userController;

    @Mock
    private JWTManager jwtManager;

    @Mock
    private HttpExchange httpExchange;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        followHandler = new FollowHandler();
    }

    @Test
    public void testHandleGetRequest_Followers() throws Exception {
        String[] splittedPath = {"/api", "follow", "followers", "test@example.com"};
        when(followController.getFollowers("test@example.com")).thenReturn("followers list");

        String response = followHandler.handleGetRequest(splittedPath);

        assertEquals("followers list", response);
        verify(followController, times(1)).getFollowers("test@example.com");
    }

    @Test
    public void testHandleGetRequest_Followings() throws Exception {
        String[] splittedPath = {"/api", "follow", "followings", "test@example.com"};
        when(followController.getFollows("test@example.com")).thenReturn("followings list");

        String response = followHandler.handleGetRequest(splittedPath);

        assertEquals("followings list", response);
        verify(followController, times(1)).getFollows("test@example.com");
    }

    @Test
    public void testHandleGetRequest_WrongURL() throws Exception {
        String[] splittedPath = {"/api", "follow", "invalid"};
        String response = followHandler.handleGetRequest(splittedPath);

        assertEquals("WRONG URL", response);
    }

    @Test
    public void testHandlePostRequest_Valid() throws Exception {
        String[] splittedPath = {"/api", "follow", "user1", "user2"};
        when(userController.isUserExists("user1")).thenReturn(true);

        String response = followHandler.handlePostRequest(splittedPath, httpExchange);

        assertEquals("Done!", response);
        verify(followController, times(1)).saveFollow("user1", "user2");
    }

    @Test
    public void testHandlePostRequest_UserNotFound() throws Exception {
        String[] splittedPath = {"/api", "follow", "user1", "user2"};
        when(userController.isUserExists("user1")).thenReturn(false);

        String response = followHandler.handlePostRequest(splittedPath, httpExchange);

        assertEquals("user-not-found", response);
        verify(followController, never()).saveFollow("user1", "user2");
    }

    @Test
    public void testHandlePostRequest_InvalidRequestFormat() throws Exception {
        String[] splittedPath = {"/api", "follow", "user1"};
        String response = followHandler.handlePostRequest(splittedPath, httpExchange);

        assertEquals("Invalid request format", response);
        verify(followController, never()).saveFollow(anyString(), anyString());
    }

    @Test
    public void testHandleDeleteRequest_DeleteAll() throws Exception {
        String[] splittedPath = {"/api", "follow"};
        String response = followHandler.handleDeleteRequest(splittedPath);

        assertEquals("Done!", response);
        verify(followController, times(1)).deleteAllFollows();
    }

    @Test
    public void testHandleDeleteRequest_InvalidRequestFormat() throws Exception {
        String[] splittedPath = {"/api", "follow", "extra", "segment"};
        String response = followHandler.handleDeleteRequest(splittedPath);

        assertEquals("Invalid request format for delete", response);
        verify(followController, never()).deleteAllFollows();
    }

    @Test
    public void testHandleDeleteRequest_InvalidRequestFormatLength2() throws Exception {
        String[] splittedPath = {"/api", "follow", "extra"};
        String response = followHandler.handleDeleteRequest(splittedPath);

        assertEquals("Invalid request format for delete", response);
        verify(followController, never()).deleteAllFollows();
    }
}
