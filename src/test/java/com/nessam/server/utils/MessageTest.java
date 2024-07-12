package com.nessam.server.test;

import com.nessam.server.controllers.MessageController;
import com.nessam.server.handlers.modelHandlers.MessageHandler;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MessageTest {

    @InjectMocks
    private MessageHandler messageHandler;

    @Mock
    private MessageController messageController;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        messageHandler = new MessageHandler();
    }

    @Test
    public void testHandle_GET_Notify() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/direct/1/person"));

        String[] splittedPath = {"/direct", "1", "person"};
        when(messageController.getNotify("1", 20)).thenReturn("Notification");

        messageHandler.handle(exchange);

        verify(exchange, times(1)).sendResponseHeaders(200, "Notification".getBytes().length);
        verify(exchange.getResponseBody(), times(1)).write("Notification".getBytes());
    }

    @Test
    public void testHandle_GET_LastMessagesWithUsers() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/direct/1/person/last"));

        String[] splittedPath = {"/direct", "1", "person", "last"};
        when(messageController.getLastMessagesWithUsers("1")).thenReturn("Last Messages");

        messageHandler.handle(exchange);

        verify(exchange, times(1)).sendResponseHeaders(200, "Last Messages".getBytes().length);
        verify(exchange.getResponseBody(), times(1)).write("Last Messages".getBytes());
    }

    @Test
    public void testHandle_GET_Messages() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/direct/1/person1/person2"));

        String[] splittedPath = {"/direct", "1", "person1", "person2"};
        when(messageController.getMessages("1", "person2")).thenReturn("Messages");

        messageHandler.handle(exchange);

        verify(exchange, times(1)).sendResponseHeaders(200, "Messages".getBytes().length);
        verify(exchange.getResponseBody(), times(1)).write("Messages".getBytes());
    }

    @Test
    public void testHandle_POST_AddMessage() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("POST");
        String requestBody = "{\"sender\":\"user1\", \"receiver\":\"user2\", \"text\":\"Hello!\"}";
        InputStream inputStream = new ByteArrayInputStream(requestBody.getBytes());
        when(exchange.getRequestBody()).thenReturn(inputStream);

        JSONObject jsonObject = new JSONObject(requestBody);
        messageHandler.handle(exchange);

        verify(exchange, times(1)).sendResponseHeaders(200, "Done!".getBytes().length);
        verify(exchange.getResponseBody(), times(1)).write("Done!".getBytes());
    }
}
