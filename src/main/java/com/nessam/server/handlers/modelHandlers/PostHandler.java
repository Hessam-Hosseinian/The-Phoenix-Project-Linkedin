package com.nessam.server.handlers.modelHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nessam.server.controllers.PostController;
import com.nessam.server.utils.BetterLogger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class PostHandler implements HttpHandler {

    private PostController postController;

    public PostHandler() throws SQLException {
        this.postController = new PostController();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] splittedPath = path.split("/");
        String response = "This is the response for posts";
        int statusCode = 200;

        try {
            switch (method) {
                case "GET":
                    response = handleGetRequest(splittedPath);
                    break;
                case "POST":
                    response = handlePostRequest(splittedPath, exchange);
                    break;
                case "PUT":
                    response = handlePutRequest(splittedPath, exchange);
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

        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private String handleGetRequest(String[] splittedPath) throws SQLException, JsonProcessingException {

        if (splittedPath.length == 2) {
            try {
                BetterLogger.INFO("Posts records received");
                return postController.getPosts();
            } catch (SQLException | JsonProcessingException e) {
                BetterLogger.ERROR(e.toString());
                return "Error fetching posts";
            }
        } else {
            String userEmail = splittedPath[splittedPath.length - 1];
            try {
                String response = postController.getPostByEmail(userEmail);
                BetterLogger.INFO("User received");
                return response != null ? response : "No Post";
            } catch (SQLException | JsonProcessingException e) {
                BetterLogger.ERROR(e.toString());
                return "Error fetching posts";
            }
        }


    }

    private String handlePostRequest(String[] splittedPath, HttpExchange exchange) throws SQLException, IOException {
        if (splittedPath.length != 5) {
            BetterLogger.WARNING("Invalid request format for POST.");
            return "Invalid request format";
        }
        String author = splittedPath[2];
        String title = splittedPath[3];
        String content = splittedPath[4];

        postController.createPost(title, content, author);
        BetterLogger.INFO("Successfully saved post : " + author + " -> " + title);
        return "Done!";

    }

    private String handlePutRequest(String[] splittedPath, HttpExchange exchange) throws SQLException, IOException {
        // Implement your logic for handling PUT requests for posts
        return "PUT method not implemented for posts";
    }

    private String handleDeleteRequest(String[] splittedPath) throws SQLException {
        // Implement your logic for handling DELETE requests for posts
        return "DELETE method not implemented for posts";
    }


}
