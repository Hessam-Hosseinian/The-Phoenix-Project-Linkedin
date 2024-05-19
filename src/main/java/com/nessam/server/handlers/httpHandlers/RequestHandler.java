package com.nessam.server.handlers.httpHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nessam.server.controllers.UserController;
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

    private UserController userController;
    private JWTManager jwtManager;

    public RequestHandler() throws SQLException {
        this.userController = new UserController();
        this.jwtManager = new JWTManager();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] splittedPath = exchange.getRequestURI().getPath().split("/");
        switch (method) {
            case "GET":
                handleGetRequest(splittedPath, exchange);
                break;
            default:
                sendResponse(exchange, "Method not allowed", 405);
                break;
        }
    }

    private void handleGetRequest(String[] splittedPath, HttpExchange exchange) {
        if (splittedPath.length < 2) {
            sendResponse(exchange, "Invalid request path", 400);
            return;
        }

        String userEmail = splittedPath[splittedPath.length - 2];
        String userPass = splittedPath[splittedPath.length - 1];

        String userResponse;
        try {
            userResponse = userController.getUserByEmailAndPass(userEmail, userPass);
        } catch (SQLException | JsonProcessingException e) {
            sendResponse(exchange, "Internal server error", 500);
            return;
        }

        if (userResponse == null) {
            sendResponse(exchange, "Incorrect userID or password", 401);
        } else {
            Headers headers = exchange.getResponseHeaders();
            Map<String, Object> payload = new HashMap<>();
            payload.put("email", userEmail);
            String token = jwtManager.createToken(payload, 60);
            headers.add("Authorization", "Bearer " + token);
            sendResponse(exchange, "Logged in successfully", 200);
        }
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) {
        try {
            exchange.sendResponseHeaders(statusCode, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
