package com.nessam.server.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nessam.server.controllers.ConnectController;
import com.nessam.server.controllers.UserController;
import com.nessam.server.handlers.modelHandlers.ConnectHandler;
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

public class ConnectTest {

    @InjectMocks
    private ConnectHandler connectHandler;

    @Mock
    private ConnectController connectController;

    @Mock
    private UserController userController;

    @Mock
    private JWTManager jwtManager;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        connectHandler = new ConnectHandler();
    }

    @Test
    public void testHandle_GET_Person2s_Success() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/connect/person2s/user1"));
        when(connectController.getPerson2s("user1")).thenReturn("List of person2s for user1");

        connectHandler.handle(exchange);

        verify(exchange, times(1)).sendResponseHeaders(200, "List of person2s for user1".getBytes().length);
        verify(exchange.getResponseBody(), times(1)).write("List of person2s for user1".getBytes());
    }

    @Test
    public void testHandle_GET_WrongURLFormat() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/connect/wrongformat"));

        connectHandler.handle(exchange);

        verify(exchange, times(1)).sendResponseHeaders(200, "WRONG URL".getBytes().length);
        verify(exchange.getResponseBody(), times(1)).write("WRONG URL".getBytes());
    }

    @Test
    public void testHandle_POST_Success() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/connect/user1/user2"));

        when(userController.isUserExists("user1")).thenReturn(true);

        String requestBody = "";
        InputStream inputStream = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));
        when(exchange.getRequestBody()).thenReturn(inputStream);

        doNothing().when(connectController).saveConnect("user1", "user2");

        connectHandler.handle(exchange);

        verify(exchange, times(1)).sendResponseHeaders(200, "Done!".getBytes().length);
        verify(exchange.getResponseBody(), times(1)).write("Done!".getBytes());
    }

    @Test
    public void testHandle_POST_UserNotFound() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/connect/unknownuser/user2"));

        when(userController.isUserExists("unknownuser")).thenReturn(false);

        String requestBody = "";
        InputStream inputStream = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));
        when(exchange.getRequestBody()).thenReturn(inputStream);

        connectHandler.handle(exchange);

        verify(exchange, times(1)).sendResponseHeaders(200, "user-not-found".getBytes().length);
        verify(exchange.getResponseBody(), times(1)).write("user-not-found".getBytes());
    }

    @Test
    public void testHandle_DELETE_Success() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("DELETE");
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/connect"));

        connectHandler.handle(exchange);

        verify(connectController, times(1)).deleteAllConnects();
        verify(exchange, times(1)).sendResponseHeaders(200, "Done!".getBytes().length);
        verify(exchange.getResponseBody(), times(1)).write("Done!".getBytes());
    }

    @Test
    public void testHandle_DELETE_InvalidRequestFormat() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("DELETE");
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/connect/invalidformat"));

        connectHandler.handle(exchange);

        verify(exchange, times(1)).sendResponseHeaders(200, "Invalid request format for delete".getBytes().length);
        verify(exchange.getResponseBody(), times(1)).write("Invalid request format for delete".getBytes());
    }
}
