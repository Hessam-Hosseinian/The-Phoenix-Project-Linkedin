package com.nessam.server.handlers.modelHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nessam.server.controllers.HashtagController;
import com.nessam.server.controllers.PostController;
import com.nessam.server.controllers.UserController;
import com.nessam.server.utils.BetterLogger;
import com.nessam.server.utils.JWTManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Map;

public class HashtagHandler implements HttpHandler {

    private final HashtagController hashtagController;
    private final UserController userController;
    private final PostController postController;
    private final JWTManager jwtManager;

    public HashtagHandler() throws SQLException {
        this.hashtagController = new HashtagController();
        this.userController = new UserController();
        this.postController = new PostController();
        this.jwtManager = new JWTManager();
        BetterLogger.INFO("HashtagHandler initialized successfully.");
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] splittedPath = path.split("/");
        String response = "This is the response hashtags";
        int statusCode = 200;

        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            statusCode = 401;
            response = "Unauthorized access";
            BetterLogger.WARNING("Unauthorized access attempt detected.");
        } else {
            String token = authHeader.substring(7);
            Map<String, Object> tokenData = jwtManager.decodeToken(token);
            if (tokenData == null) {
                response = "Invalid or expired token";
                statusCode = 403;
                BetterLogger.WARNING("Token validation failed for token: " + token);
            } else {
                try {
                    switch (method) {
                        case "GET":
                            response = handleGetRequest(splittedPath);
                            break;
                        case "POST":
                            response = handlePostRequest(splittedPath);
                            break;
                        case "DELETE":
                            response = handleDeleteRequest(splittedPath);
                            break;
                        default:
                            response = "HTTP method not supported";
                            statusCode = 405;
                            BetterLogger.ERROR("Unsupported HTTP method: " + method);
                    }
                } catch (Exception e) {
                    response = "Internal Server Error";
                    statusCode = 500;
                    BetterLogger.ERROR("Exception occurred: " + e.getMessage());
                }
            }
        }

        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
            BetterLogger.INFO("Response sent: " + response);
        }
    }

    private String handleGetRequest(String[] splittedPath) {
        if (splittedPath.length != 3) {
            return "Incorrect URL format.";
        }
        try {
            String result = hashtagController.GetHashtag(splittedPath[2]);
            BetterLogger.INFO("Fetched posts for hashtag: " + splittedPath[2]);
            return result;
        } catch (SQLException e) {
            BetterLogger.ERROR("SQL error while fetching hashtag: " + splittedPath[2]);
            throw new RuntimeException(e);
        }
    }

    private String handlePostRequest(String[] splittedPath) {
        if (splittedPath.length != 4) {
            return "Incorrect URL format.";
        }
        try {
            if (postController.getPostById(splittedPath[3]) == null) {
                return "No post found with ID: " + splittedPath[3];
            } else {
                hashtagController.addHashtag(splittedPath[2], splittedPath[3]);
                BetterLogger.INFO("Hashtag added: " + splittedPath[2] + " to post " + splittedPath[3]);
                return "Hashtag added successfully.";
            }
        } catch (SQLException | JsonProcessingException e) {
            BetterLogger.ERROR("Error adding hashtag to post");
            throw new RuntimeException(e);
        }
    }

    private String handleDeleteRequest(String[] splittedPath) throws SQLException {
        if (splittedPath.length == 2 && "deleteAllTags".equals(splittedPath[1])) {
            hashtagController.deleteAll();
            BetterLogger.INFO("All tags deleted");
            return "All tags deleted successfully.";
        } else if (splittedPath.length == 3) {
            hashtagController.deleteOne(splittedPath[2]);
            BetterLogger.INFO("Tag deleted: " + splittedPath[2]);
            return "Tag deleted successfully.";
        } else {
            return "Incorrect URL format.";
        }
    }
}
