package com.nessam.server.handlers.modelHandlers;

import com.nessam.server.dataAccess.PostDAO;
import com.nessam.server.models.Post;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PostHandler implements HttpHandler {

    private PostDAO postDAO;

    public PostHandler() {
        this.postDAO = new PostDAO();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if ("GET".equals(method)) {
            handleGetPosts(exchange);
        } else if ("POST".equals(method)) {
            handleCreatePost(exchange);
        } else if ("PUT".equals(method)) {
            handleUpdatePost(exchange);
        } else if ("DELETE".equals(method)) {
            handleDeletePost(exchange);
        } else {
            String response = "Unsupported HTTP method: " + method;
            exchange.sendResponseHeaders(405, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    private void handleCreatePost(HttpExchange exchange) throws IOException {
        String postContent = extractQueryParam(exchange, "content");
        String userId = extractQueryParam(exchange, "userId");

        Post newPost = new Post();
        newPost.setText(postContent);
        newPost.setUserId(userId);

        postDAO.savePost(newPost);

        String response = "Post created successfully";
        exchange.sendResponseHeaders(201, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void handleGetPosts(HttpExchange exchange) throws IOException {
        List<Post> posts = postDAO.getPosts();
        String response = posts.toString(); // Needs proper JSON formatting
        exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }

    private void handleUpdatePost(HttpExchange exchange) throws IOException {
        // Similar to handleCreatePost
    }

    private void handleDeletePost(HttpExchange exchange) throws IOException {
        // Similar to handleCreatePost
    }

    private String extractQueryParam(HttpExchange exchange, String paramName) {
        // Parse query parameters from the URI
        return ""; // Implementation needed
    }
}
