package com.nessam.server.handlers;

import com.nessam.server.dataAccess.CommentDAO;
import com.nessam.server.models.Comment;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CommentHandler implements HttpHandler {

    private CommentDAO commentDAO;

    public CommentHandler() {
        this.commentDAO = new CommentDAO();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if ("GET".equals(method)) {
            handleGetComments(exchange);
        } else if ("POST".equals(method)) {
            handleCreateComment(exchange);
        } else if ("PUT".equals(method)) {
            handleUpdateComment(exchange);
        } else if ("DELETE".equals(method)) {
            handleDeleteComment(exchange);
        } else {
            String response = "Unsupported HTTP method: " + method;
            exchange.sendResponseHeaders(405, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    private void handleCreateComment(HttpExchange exchange) throws IOException {
        String commentContent = extractQueryParam(exchange, "content");
        String userId = extractQueryParam(exchange, "userId");
        String postId = extractQueryParam(exchange, "postId");

        Comment newComment = new Comment();
        newComment.setText(commentContent);
        newComment.setUserId(Long.parseLong(userId));
        newComment.setPostId(Long.parseLong(postId));

        commentDAO.saveComment(newComment);

        String response = "Comment created successfully";
        exchange.sendResponseHeaders(201, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void handleGetComments(HttpExchange exchange) throws IOException {
        String postId = extractQueryParam(exchange, "postId");
        List<Comment> comments = commentDAO.getCommentsByPostId(Long.parseLong(postId));
        String response = comments.toString(); // Needs proper JSON formatting
        exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }

    private void handleUpdateComment(HttpExchange exchange) throws IOException {
        // Similar to handleCreateComment
    }

    private void handleDeleteComment(HttpExchange exchange) throws IOException {
        // Similar to handleCreateComment
    }

    private String extractQueryParam(HttpExchange exchange, String paramName) {
        // Parse query parameters from the URI
        return ""; // Implementation needed
    }
}
