package com.nessam.server.handlers.modelHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nessam.server.controllers.BlockController;
import com.nessam.server.controllers.ConnectController;
import com.nessam.server.controllers.UserController;
import com.nessam.server.utils.BetterLogger;
import com.nessam.server.utils.JWTManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Map;

public class ConnectHandler implements HttpHandler {

    private final ConnectController connectController;
    private final UserController userController;
    private BlockController blockController;
    private final JWTManager jwtManager;

    public ConnectHandler() throws SQLException {
        this.connectController = new ConnectController();
        this.userController = new UserController();
        this.jwtManager = new JWTManager();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] splittedPath = path.split("/");
        String response = "This is the response connects";
        int statusCode = 200;

        // Extract and verify token
        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response = "Unauthorized";
            statusCode = 401;
            BetterLogger.WARNING("Unauthorized access detected.");
        } else {
            String token = authHeader.substring(7);

            Map<String, Object> tokenData = jwtManager.decodeToken(token);
            if (tokenData == null) {
                response = "Invalid or expired token";
                statusCode = 401;
                BetterLogger.WARNING("Invalid or expired token received.");
            } else {
                try {
                    switch (method) {
                        case "GET":
                            response = handleGetRequest(splittedPath);
                            break;
                        case "POST":
                            response = handlePostRequest(splittedPath, exchange);
                            break;
                        case "DELETE":
                            response = handleDeleteRequest(splittedPath);
                            break;
                        default:
                            BetterLogger.ERROR("Unsupported HTTP method: " + method);
                            response = "Method not supported";
                            statusCode = 405;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    BetterLogger.ERROR("SQLException occurred: " + e.getMessage());
                    response = "Internal Server Error";
                    statusCode = 500;
                }
            }
        }

        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    // GET ip:port/connect/person2s/userEmail
    private String handleGetRequest(String[] splittedPath) throws SQLException, JsonProcessingException {
        if (splittedPath.length == 4) {
            if (splittedPath[2].equals("person2s")) {
                BetterLogger.INFO("Received request to retrieve connectings records.");
                return connectController.getPerson2s(splittedPath[3]);
            }
        }
        BetterLogger.WARNING("Received request with wrong URL format.");
        return "WRONG URL";
    }

    private String handlePostRequest(String[] splittedPath, HttpExchange exchange) throws SQLException {
        if (splittedPath.length != 4) {
            BetterLogger.WARNING("Invalid request format for POST.");
            return "Invalid request format";
        }
        String user = splittedPath[2];
        String targetUser = splittedPath[3];

        if (!userController.isUserExists(user)) {
            BetterLogger.WARNING("User not found: " + user);
            return "user-not-found";
        }

        connectController.saveConnect(user, targetUser);
        BetterLogger.INFO("Successfully saved connect relationship: " + user + " -> " + targetUser);
        return "Done!";
    }

    private String handleDeleteRequest(String[] splittedPath) throws SQLException {
        if (splittedPath.length == 4) {
            BetterLogger.WARNING("Invalid request format for DELETE.");
            return "Invalid request format for delete";
        } else if (splittedPath.length == 2) {
            connectController.deleteAllConnects();
            BetterLogger.INFO("All connects deleted.");
            return "Done!";
        }
        BetterLogger.WARNING("Invalid request format for DELETE.");
        return "Invalid request format for delete";
    }
}
