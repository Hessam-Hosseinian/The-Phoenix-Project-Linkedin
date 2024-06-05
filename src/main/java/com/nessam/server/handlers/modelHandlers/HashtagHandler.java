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
    private final PostController postController;
    private final JWTManager jwtManager;
    private String userEmail;

    public HashtagHandler() throws SQLException {
        this.hashtagController = new HashtagController();
        this.postController = new PostController();
        this.jwtManager = new JWTManager();
        BetterLogger.INFO("HashtagHandler initialized successfully.");
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] splittedPath = path.split("/");
        String response = "This is the response follows";
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
                userEmail = (String) tokenData.get("email");
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
                            BetterLogger.ERROR("Unsupported HTTP method: " + method);
                            response = "Method not supported";
                            statusCode = 405;
                    }
                } catch (SQLException e) {
                    BetterLogger.ERROR("SQLException occurred: " + e.getMessage());
                    response = "Internal Server Error";
                    statusCode = 500;
                }
            }

            exchange.sendResponseHeaders(statusCode, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    // GET ip:port/hashtag/#tagWord
    private String handleGetRequest(String[] splittedPath) {
        if (splittedPath.length != 3) {
            return "Incorrect URL format.";
        }
        try {
            String result = hashtagController.searchHashtag(splittedPath[2]);
            BetterLogger.INFO("Fetched posts for hashtag: " + splittedPath[2]);
            return result;
        } catch (SQLException e) {
            BetterLogger.ERROR("SQL error while fetching hashtag: " + splittedPath[2]);
            throw new RuntimeException(e);
        }
    }

    // POST ip:port/hashtag/postId/#tagWord
    private String handlePostRequest(String[] splittedPath) throws SQLException, JsonProcessingException {
        if (splittedPath.length != 4) {
            System.out.println(splittedPath.length);
            return "Incorrect URL format.";
        }
        else {
            if (postController.getPostById(splittedPath[2]) == null) {
                return "No post found with ID: " + splittedPath[2];
            } else {
                hashtagController.addHashtag(Long.valueOf(splittedPath[2]), splittedPath[3]);
                BetterLogger.INFO("Hashtag added: " + splittedPath[3] + " to post " + splittedPath[2]);
                return "Hashtag added successfully.";
            }
        }
    }

    // DELETE ip:port/hashtag/postId/#tagName
    private String handleDeleteRequest(String[] splittedPath) throws SQLException {
        if (splittedPath.length == 4) {
            if (hashtagController.tagExists(Long.valueOf(splittedPath[2]), splittedPath[3])) {
                return hashtagController.deleteHashtag(Long.valueOf(splittedPath[2]), splittedPath[3]);
            }
            else {
                return "No tag exists matching this postId";
            }
        } else  {
            return "Wrong URL format";
        }
    }
}
