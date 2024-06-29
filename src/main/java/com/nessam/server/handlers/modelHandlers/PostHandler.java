package com.nessam.server.handlers.modelHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nessam.server.controllers.PostController;
import com.nessam.server.utils.BetterLogger;
import com.nessam.server.utils.JWTManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.sql.SQLException;
import java.util.Map;

public class PostHandler implements HttpHandler {

    private final PostController postController;
    private final JWTManager jwtManager;
    private String userEmail;

    public PostHandler() throws SQLException {
        this.postController = new PostController();
        this.jwtManager = new JWTManager();
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
                    response = handleGetRequest(splittedPath);
                    break;
                case "POST":
                    response = handlePostRequest(exchange);
                    break;
                case "PUT":
                    response = handlePutRequest(exchange);
                    break;
                case "DELETE":
                    response = handleDeleteRequest(splittedPath);
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

    private String handleGetRequest(String[] splittedPath) throws SQLException {
        if (splittedPath.length == 2) {
            return getAllPosts();
        } else if (splittedPath.length == 3) {
            String tmpUserEmail = splittedPath[2];
            return getPostsByAuthor(tmpUserEmail);
        } else if (splittedPath.length == 4) {
            String title = splittedPath[3];
            String tmpUserEmail = splittedPath[2];
            return getPostByAuthorAndTitle(tmpUserEmail, title);
        }

        BetterLogger.WARNING("Received request with wrong URL format.");
        return "WRONG URL";
    }

    private String getAllPosts() throws SQLException {
        try {
            String posts = postController.getPosts();
            BetterLogger.INFO("Successfully retrieved all posts");
            return posts;
        } catch (SQLException | JsonProcessingException e) {
            BetterLogger.ERROR("Error fetching posts: " + e.getMessage());
            return "Error fetching posts";
        }
    }

    private String getPostsByAuthor(String author) throws SQLException {
        try {
            String response = postController.getPostByAuthor(author);
            BetterLogger.INFO("Successfully retrieved posts for author: " + author);
            return response != null ? response : "No Post";
        } catch (SQLException | JsonProcessingException e) {
            BetterLogger.ERROR("Error fetching posts for author " + author + ": " + e.getMessage());
            return "Error fetching posts";
        }
    }

    private String getPostByAuthorAndTitle(String author, String title) throws SQLException {
        try {
            String response = postController.getPostByAuthorAndTitle(author, title);
            BetterLogger.INFO("Successfully retrieved post for author: " + author + ", title: " + title);
            return response != null ? response : "No Post";
        } catch (SQLException | JsonProcessingException e) {
            BetterLogger.ERROR("Error fetching post for author " + author + ", title " + title + ": " + e.getMessage());
            return "Error fetching post";
        }
    }

    private String handlePostRequest(HttpExchange exchange) throws SQLException, IOException {
        JSONObject jsonObject = getRequestBody(exchange);

        String title = jsonObject.optString("title", null);
        String content = jsonObject.optString("content", null);
        String filePath = jsonObject.optString("file_path", null);

        if (postController.getPostByAuthorAndTitle(userEmail, title).equals("No Post")) {
            postController.createPost(title, content, userEmail, filePath);
            BetterLogger.INFO("Successfully saved post: " + userEmail + " -> " + title);
            return "Done!";
        } else {
            BetterLogger.WARNING("Post not saved: " + userEmail + " -> " + title);
            return "This post is already saved use another title!";
        }
    }

    private String handlePutRequest(HttpExchange exchange) throws SQLException, IOException {
        JSONObject jsonObject = getRequestBody(exchange);

        String title = jsonObject.optString("title", null);
        String newContent = jsonObject.optString("content", null);

        if (postController.getPostByAuthorAndTitle(userEmail, title).equals("No Post")) {
            BetterLogger.WARNING("Post not found for author: " + userEmail + " -> " + title);
            return "Post not found!";
        }

        postController.updatePost(userEmail, title, newContent);
        BetterLogger.INFO("Successfully updated post: " + userEmail + " -> " + title);
        return "Post updated!";
    }

    private String handleDeleteRequest(String[] splittedPath) throws SQLException {
        if (splittedPath.length == 2) {
            postController.deletePosts();
            BetterLogger.INFO("Successfully deleted all posts");
            return "All posts deleted!";
        } else if (splittedPath.length == 4) {
            String author = splittedPath[2];
            String title = splittedPath[3];
            postController.deletePostByAuthorAndTitle(author, title);
            BetterLogger.INFO("Successfully deleted post: " + author + " -> " + title);
            return "Post deleted!";
        } else {
            BetterLogger.WARNING("Invalid request format for DELETE.");
            return "Invalid request format";
        }
    }

    private JSONObject getRequestBody(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        requestBody.close();
        return new JSONObject(body.toString());
    }

    private void respondWith(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
