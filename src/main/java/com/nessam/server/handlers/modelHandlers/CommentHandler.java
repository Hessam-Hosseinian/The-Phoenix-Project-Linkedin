package com.nessam.server.handlers.modelHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nessam.server.controllers.CommentController;
import com.nessam.server.utils.BetterLogger;
import com.nessam.server.utils.JWTManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Map;

public class CommentHandler implements HttpHandler {

    private final CommentController commentController;
    private final JWTManager jwtManager;

    public CommentHandler() throws SQLException {
        this.commentController = new CommentController();
        this.jwtManager = new JWTManager();
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

    private String handleGetRequest(String[] splittedPath) throws SQLException, JsonProcessingException {
        if (splittedPath.length == 3) {
            try {
                long postId = Long.parseLong(splittedPath[2]);
                String comments = commentController.getCommentsByPostId(postId);
                BetterLogger.INFO("Successfully retrieved comments for post ID: " + postId);
                return comments;
            } catch (SQLException | JsonProcessingException | NumberFormatException e) {
                BetterLogger.ERROR("Error fetching comments for post ID " + splittedPath[2] + ": " + e.getMessage());
                return "Error fetching comments";
            }
        }

        BetterLogger.WARNING("Received request with wrong URL format.");
        return "WRONG URL";
    }

    private String handlePostRequest(String[] splittedPath, HttpExchange exchange) throws SQLException, IOException {
        if (splittedPath.length != 5) {
            BetterLogger.WARNING("Invalid request format for POST.");
            return "Invalid request format";
        }
        String postIdStr = splittedPath[2];
        String author = splittedPath[3];
        String content = splittedPath[4];
        String filePath = ""; // Assuming no file path provided

        try {
            long postId = Long.parseLong(postIdStr);
            commentController.createComment(content, filePath, author, postId);
            BetterLogger.INFO("Successfully saved comment: " + author + " -> Post ID: " + postId);
            return "Done!";
        } catch (NumberFormatException e) {
            BetterLogger.WARNING("Invalid post ID format.");
            return "Invalid post ID format";
        }
    }

    private String handleDeleteRequest(String[] splittedPath) throws SQLException {
        if (splittedPath.length == 3) {
            try {
                long postId = Long.parseLong(splittedPath[2]);
                commentController.deleteCommentsByPostId(postId);
                BetterLogger.INFO("Successfully deleted comments for post ID: " + postId);
                return "Comments deleted!";
            } catch (NumberFormatException e) {
                BetterLogger.WARNING("Invalid post ID format.");
                return "Invalid post ID format";
            }
        } else {
            BetterLogger.WARNING("Invalid request format for DELETE.");
            return "Invalid request format";
        }
    }
}
