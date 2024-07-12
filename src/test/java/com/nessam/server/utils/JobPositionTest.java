package com.nessam.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nessam.server.controllers.JobPositionController;
import com.nessam.server.handlers.modelHandlers.JobPositionHandler;
import com.nessam.server.models.JobPosition;
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

public class JobPositionTest {

    @InjectMocks
    private JobPositionHandler jobPositionHandler;

    @Mock
    private JobPositionController jobPositionController;

    @Mock
    private JWTManager jwtManager;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        jobPositionHandler = new JobPositionHandler();
    }

    @Test
    public void testHandle_GET_AllJobPositions() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/jobpositions"));

        when(jobPositionController.getJobPositions()).thenReturn("List of all job positions");

        jobPositionHandler.handle(exchange);

        verify(exchange, times(1)).sendResponseHeaders(200, "List of all job positions".getBytes().length);
        verify(exchange.getResponseBody(), times(1)).write("List of all job positions".getBytes());
    }

    @Test
    public void testHandle_GET_JobPositionsByUser() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/jobpositions/user1"));

        when(jobPositionController.getJobPositionsByUserEmail("user1")).thenReturn("List of job positions for user1");

        jobPositionHandler.handle(exchange);

        verify(exchange, times(1)).sendResponseHeaders(200, "List of job positions for user1".getBytes().length);
        verify(exchange.getResponseBody(), times(1)).write("List of job positions for user1".getBytes());
    }

    @Test
    public void testHandle_POST_CreateJobPosition() throws Exception {
        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getRequestURI()).thenReturn(new java.net.URI("/jobpositions"));

        String requestBody = "{\"title\": \"Software Engineer\", \"description\": \"Developing software applications\"}";
        InputStream inputStream = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));
        when(exchange.getRequestBody()).thenReturn(inputStream);

//        JobPosition newJobPosition = new JobPosition("Software Engineer", "Developing software applications");

//        doNothing().when(jobPositionController).createJobPosition(newJobPosition);

        jobPositionHandler.handle(exchange);

        verify(exchange, times(1)).sendResponseHeaders(200, "Job position created successfully.".getBytes().length);
        verify(exchange.getResponseBody(), times(1)).write("Job position created successfully.".getBytes());
    }
}
