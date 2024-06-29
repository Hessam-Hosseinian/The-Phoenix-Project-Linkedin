package com.nessam.server.handlers.httpHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nessam.server.controllers.UserController;
import com.nessam.server.utils.BetterLogger;
import com.nessam.server.utils.JWTManager;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler implements HttpHandler {

    private final UserController userController;
    private final JWTManager jwtManager;

    public RequestHandler() throws SQLException {
        this.userController = new UserController();
        this.jwtManager = new JWTManager();
    }

    @Override
    public void handle(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        String[] splittedPath = exchange.getRequestURI().getPath().split("/");
        if (method.equals("GET")) {
            handleGetRequest(splittedPath, exchange);

        } else if (method.equals("POST")) {
            handlePostRequest(splittedPath, exchange);

        } else {
            sendResponse(exchange, "Method not allowed", 405);
        }
    }

    private void handleGetRequest(String[] splittedPath, HttpExchange exchange) {
        if (splittedPath.length < 3) {
            sendResponse(exchange, "Invalid request path", 400);
            return;
        }

        String userEmail = splittedPath[splittedPath.length - 2];
        String userPass = splittedPath[splittedPath.length - 1];

        try {
            String userResponse = String.valueOf(userController.getUserByEmailAndPass(userEmail, userPass));

            if (userResponse.equals("No User")) {
                sendResponse(exchange, "Incorrect userID or password", 401);
            } else {
                Headers headers = exchange.getResponseHeaders();
                Map<String, Object> payload = new HashMap<>();
                payload.put("email", userEmail);
                String token = jwtManager.createToken(payload, 60); // Token valid for 60 minutes
                System.out.println("Generated Token: " + token);

                // Set the Authorization header with the token
                headers.set("Authorization", "Bearer " + token);

                System.out.println(exchange.getResponseHeaders());
                System.out.println(exchange.getResponseHeaders().getFirst("Authorization"));
                // Optionally decode the token for logging purposes
                Map<String, Object> decodedPayload = jwtManager.decodeToken(token);
                if (decodedPayload != null) {
                    System.out.println("Decoded Payload: " + decodedPayload);
                } else {
                    System.out.println("Token is invalid or expired.");
                }

                sendResponse(exchange, "Logged in successfully", 200);
            }
        } catch (SQLException | JsonProcessingException e) {
            BetterLogger.ERROR(e.getMessage());
            sendResponse(exchange, "Internal server error", 500);
        }
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) {
        try {
            exchange.sendResponseHeaders(statusCode, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } catch (IOException e) {
            BetterLogger.ERROR(e.getMessage());
        }
    }

    private void handlePostRequest(String[] splittedPath, HttpExchange exchange) {

        String response = "This is the response Posts";
        int statusCode = 200;

        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response = "Unauthorized";
            statusCode = 401;

            sendResponse(exchange, response, statusCode);
            BetterLogger.WARNING("Unauthorized access detected.");
        } else {
            String token = authHeader.substring(7);
            Map<String, Object> tokenData = jwtManager.decodeToken(token);


            if (tokenData == null) {
                response = "Invalid or expired token";
                statusCode = 401;
                BetterLogger.WARNING("Invalid or expired token received.");
                sendResponse(exchange, response, statusCode);

            }
        }
        sendResponse(exchange, response, statusCode);

    }
}