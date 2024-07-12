package com.nessam.server.utils;

import com.nessam.server.controllers.PostController;
import com.nessam.server.controllers.UserController;
import com.nessam.server.handlers.modelHandlers.SearchHandler;
import com.nessam.server.utils.BetterLogger;
import com.nessam.server.utils.JWTManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SearchTest {

    @InjectMocks
    private SearchHandler searchHandler;

    @Mock
    private UserController userController;

    @Mock
    private PostController postController;

    @Mock
    private JWTManager jwtManager;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        searchHandler = new SearchHandler();
    }

    @Test
    public void testHandleGetRequest_SearchHashtag() throws Exception {
        String[] splittedPath = {"/api", "search", "searchHashtag", "#example"};
        when(postController.searchPostHashtag("#example")).thenReturn("hashtag search result");

        String response = searchHandler.handleGetRequest(splittedPath);

        assertEquals("hashtag search result", response);
        verify(postController, times(1)).searchPostHashtag("#example");
    }

    @Test
    public void testHandleGetRequest_SearchPost() throws Exception {
        String[] splittedPath = {"/api", "search", "searchPost", "example"};
        when(postController.searchPost("example")).thenReturn("post search result");

        String response = searchHandler.handleGetRequest(splittedPath);

        assertEquals("post search result", response);
        verify(postController, times(1)).searchPost("example");
    }

    @Test
    public void testHandleGetRequest_SearchUser() throws Exception {
        String[] splittedPath = {"/api", "search", "searchUser", "example"};
        when(userController.searchUser("example")).thenReturn("user search result");

        String response = searchHandler.handleGetRequest(splittedPath);

        assertEquals("user search result", response);
        verify(userController, times(1)).searchUser("example");
    }

    @Test
    public void testHandleGetRequest_InvalidURLFormat() throws Exception {
        String[] splittedPath = {"/api", "search", "invalid"};
        String response = searchHandler.handleGetRequest(splittedPath);

        assertEquals("Invalid URL format.", response);
    }

    @Test
    public void testHandleGetRequest_NoSearchOperationExecuted() throws Exception {
        String[] splittedPath = {"/api", "search", "unknown", "example"};
        String response = searchHandler.handleGetRequest(splittedPath);

        assertEquals("No search operation executed.", response);
    }
}
