package com.nessam.server.handlers.modelHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nessam.server.controllers.JobPositionController;
import com.nessam.server.models.JobPosition;
import com.nessam.server.utils.BetterLogger;
import com.nessam.server.utils.JWTManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Map;

public class JobPositionHandler implements HttpHandler {

    private final JobPositionController jobPositionController;
    private final JWTManager jwtManager;
    private String userEmail;
    private final ObjectMapper objectMapper;

    public JobPositionHandler() throws SQLException {
        this.jobPositionController = new JobPositionController();
        this.jwtManager = new JWTManager();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] splittedPath = path.split("/");
        String response = "This is the response Posts";
        int statusCode = 200;
        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            respondWith(exchange, 401, "Unauthorized");
            BetterLogger.WARNING("Unauthorized access detected.");
            return;
        }

        String token = authHeader.substring(7);

        Map<String, Object> tokenData = jwtManager.decodeToken(token);

        if (tokenData == null) {
            respondWith(exchange, 401, "Invalid or expired token");
            BetterLogger.WARNING("Invalid or expired token received.");
            return;
        }

        setUserEmail((String) tokenData.get("email"));

        try {
            switch (method) {
                case "GET":
                    System.out.println("dsd");
                    response = handleGetRequest(splittedPath);
                    break;
                case "POST":
                    response = handlePostRequest(exchange);
                    break;
                case "PUT":
                    // response = handlePutRequest(exchange);
                    break;
                case "DELETE":
                    // response = handleDeleteRequest(splittedPath);
                    break;
                default:
                    BetterLogger.ERROR("Unsupported HTTP method: " + method);
                    respondWith(exchange, 405, "Method not supported");
                    return;
            }
        } catch (SQLException e) {
            BetterLogger.ERROR("SQLException occurred: " + e.getMessage());
            respondWith(exchange, 500, "Internal Server Error");
            return;
        }

        respondWith(exchange, statusCode, response);
    }

    private String handleGetRequest(String[] splittedPath) throws SQLException, JsonProcessingException {
        if (splittedPath.length != 3) {

            return jobPositionController.getJobPositions();
        }

        return jobPositionController.getJobPositionsByUserEmail(splittedPath[2]);
    }

    private String handlePostRequest(HttpExchange exchange) throws IOException, SQLException {
        InputStream requestBody = exchange.getRequestBody();
        JobPosition newJobPosition = objectMapper.readValue(requestBody, JobPosition.class);
        jobPositionController.createJobPosition(newJobPosition);
        return "Job position created successfully.";
    }

    private void respondWith(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    public JobPositionController getJobPositionController() {
        return jobPositionController;
    }

    public JWTManager getJwtManager() {
        return jwtManager;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
