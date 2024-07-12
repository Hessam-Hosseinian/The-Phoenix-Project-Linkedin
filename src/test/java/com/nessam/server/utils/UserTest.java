package com.nessam.server.utils;

import com.nessam.server.controllers.UserController;
import com.nessam.server.handlers.modelHandlers.UserHandler;
import com.nessam.server.utils.JWTManager;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.testng.annotations.Test;
import org.mockito.*;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserTest {

    @InjectMocks
    private UserHandler userHandler;

    @Mock
    private UserController userController;

    @Mock
    private JWTManager jwtManager;

    @Mock
    private HttpExchange httpExchange;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userHandler = new UserHandler();
    }

    @Test
    public void testHandleGetRequest() throws Exception {
        String[] splittedPath = {"/api", "users", "contactInfo", "123"};
        when(userController.getUserContactInfoByUserId("123")).thenReturn("UserContactInfo");

        String response = userHandler.handleGetRequest(splittedPath);

        assertEquals("UserContactInfo", response);
        verify(userController, times(1)).getUserContactInfoByUserId("123");
    }

    @Test
    public void testHandleGet() throws Exception {
        URI uri = new URI("/api/users/contactInfo/123");
        when(httpExchange.getRequestMethod()).thenReturn("GET");
        when(httpExchange.getRequestURI()).thenReturn(uri);

        OutputStream os = new ByteArrayOutputStream();
        when(httpExchange.getResponseBody()).thenReturn(os);

        userHandler.handle(httpExchange);

        verify(httpExchange, times(1)).sendResponseHeaders(eq(200), anyLong());
        assertEquals("UserContactInfo", os.toString());
    }

    @Test
    public void testHandlePostRequest_CreateUser() throws Exception {
        String[] splittedPath = {"/api", "users"};
        String requestBody = "{\"email\":\"test@example.com\",\"password\":\"Pass1234!\"}";
        InputStream requestBodyStream = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));

        when(httpExchange.getRequestBody()).thenReturn(requestBodyStream);
        doNothing().when(userController).createUser(any(JSONObject.class));

        String response = userHandler.handlePostRequest(httpExchange, splittedPath);

        assertEquals("User created successfully", response);
        verify(userController, times(1)).createUser(any(JSONObject.class));
    }

    @Test
    public void testHandlePostRequest_CreateUserContactInfo() throws Exception {
        String[] splittedPath = {"/api", "users", "contactInfo"};
        String requestBody = "{\"userId\":\"123\",\"phone\":\"1234567890\"}";
        InputStream requestBodyStream = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));

        when(httpExchange.getRequestBody()).thenReturn(requestBodyStream);
        doNothing().when(userController).createUserContactInfo(any(JSONObject.class));

        String response = userHandler.handlePostRequest(httpExchange, splittedPath);

        assertEquals("User contact info created successfully", response);
        verify(userController, times(1)).createUserContactInfo(any(JSONObject.class));
    }

    @Test
    public void testHandlePostRequest_CreateUserEducation() throws Exception {
        String[] splittedPath = {"/api", "users", "education"};
        String requestBody = "{\"userId\":\"123\",\"degree\":\"Bachelor\"}";
        InputStream requestBodyStream = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));

        when(httpExchange.getRequestBody()).thenReturn(requestBodyStream);
        doNothing().when(userController).creteUserEducation(any(JSONObject.class));

        String response = userHandler.handlePostRequest(httpExchange, splittedPath);

        assertEquals("User contact info created successfully", response);
        verify(userController, times(1)).creteUserEducation(any(JSONObject.class));
    }

    @Test
    public void testHandlePostRequest_ErrorCreatingUser() throws Exception {
        String[] splittedPath = {"/api", "users"};
        String requestBody = "{\"email\":\"invalidemail\",\"password\":\"short\"}";
        InputStream requestBodyStream = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));

        when(httpExchange.getRequestBody()).thenReturn(requestBodyStream);
        doThrow(new IllegalArgumentException("Incorrect email format")).when(userController).createUser(any(JSONObject.class));

        String response = userHandler.handlePostRequest(httpExchange, splittedPath);

        assertEquals("Error creating user", response);
        verify(userController, times(1)).createUser(any(JSONObject.class));
    }

    @Test
    public void testHandlePostRequest_ErrorCreatingUserContactInfo() throws Exception {
        String[] splittedPath = {"/api", "users", "contactInfo"};
        String requestBody = "{\"userId\":\"123\",\"phone\":\"invalidphone\"}";
        InputStream requestBodyStream = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));

        when(httpExchange.getRequestBody()).thenReturn(requestBodyStream);
        doThrow(new IllegalArgumentException("Invalid phone number")).when(userController).createUserContactInfo(any(JSONObject.class));

        String response = userHandler.handlePostRequest(httpExchange, splittedPath);

        assertEquals("Error creating user contact info", response);
        verify(userController, times(1)).createUserContactInfo(any(JSONObject.class));
    }

    @Test
    public void testHandleDeleteRequest_DeleteAllUsers() throws Exception {
        String[] splittedPath = {"/api", "users"};

        doNothing().when(userController).deleteUsers();

        String response = userHandler.handleDeleteRequest(splittedPath);

        assertEquals("All users deleted", response);
        verify(userController, times(1)).deleteUsers();
    }

    @Test
    public void testHandleDeleteRequest_DeleteSpecificUser() throws Exception {
        String[] splittedPath = {"/api", "users", "test@example.com"};

        doNothing().when(userController).deleteUser("test@example.com");

        String response = userHandler.handleDeleteRequest(splittedPath);

        assertEquals("User deleted successfully", response);
        verify(userController, times(1)).deleteUser("test@example.com");
    }

    @Test
    public void testHandleDeleteRequest_ErrorDeletingUser() throws Exception {
        String[] splittedPath = {"/api", "users", "test@example.com"};

        doThrow(new IllegalArgumentException("User not found")).when(userController).deleteUser("test@example.com");

        String response = userHandler.handleDeleteRequest(splittedPath);

        assertEquals("Error deleting user", response);
        verify(userController, times(1)).deleteUser("test@example.com");
    }

    @Test
    public void testHandleDelete() throws Exception {
        URI uri = new URI("/api/users/test@example.com");
        when(httpExchange.getRequestMethod()).thenReturn("DELETE");
        when(httpExchange.getRequestURI()).thenReturn(uri);

        OutputStream os = new ByteArrayOutputStream();
        when(httpExchange.getResponseBody()).thenReturn(os);

        userHandler.handle(httpExchange);

        verify(httpExchange, times(1)).sendResponseHeaders(eq(200), anyLong());
        assertEquals("User deleted successfully", os.toString());
    }
}
