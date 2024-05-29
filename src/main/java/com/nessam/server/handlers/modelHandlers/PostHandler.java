package com.nessam.server.handlers.modelHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nessam.server.controllers.CommentController;
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
    private final CommentController commentController;
    private final JWTManager jwtManager;
    String userEmail;

    public PostHandler() throws SQLException {
        this.postController = new PostController();
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
                setUserEmail((String) tokenData.get("email"));
                try {
                    switch (method) {
                        case "GET":
                            response = handleGetRequest(splittedPath);
                            break;
                        case "POST":
                            if (splittedPath.length == 6 && "comment".equals(splittedPath[5])) {
                                response = handleCommentPostRequest(splittedPath, exchange);
                            } else {
                                response = handlePostRequest(splittedPath, exchange);
                            }
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

    private String handleGetRequest(String[] splittedPath) throws SQLException {
        if (splittedPath.length == 1) {
            try {
                String posts = postController.getPosts();
                BetterLogger.INFO("Successfully retrieved all posts");
                return posts;
            } catch (SQLException | JsonProcessingException e) {
                BetterLogger.ERROR("Error fetching posts: " + e.getMessage());
                return "Error fetching posts";
            }
        } else if (splittedPath.length == 2) {

            try {
                String response = postController.getPostByAuthor(userEmail);
                BetterLogger.INFO("Successfully retrieved posts for author: " + userEmail);
                return response != null ? response : "No Post";
            } catch (SQLException | JsonProcessingException e) {
                BetterLogger.ERROR("Error fetching posts for author " + userEmail + ": " + e.getMessage());
                return "Error fetching posts";
            }
        } else if (splittedPath.length == 3) {

            String title = splittedPath[splittedPath.length - 1];
            try {
                String response = postController.getPostByAuthorAndTitle(userEmail, title);
                BetterLogger.INFO("Successfully retrieved post for author: " + userEmail + ", title: " + title);
                return response != null ? response : "No Post";
            } catch (SQLException | JsonProcessingException e) {
                BetterLogger.ERROR("Error fetching post for author " + userEmail + ", title " + title + ": " + e.getMessage());
                return "Error fetching post";
            }
        }

        BetterLogger.WARNING("Received request with wrong URL format.");
        return "WRONG URL";
    }

    private String handlePostRequest(String[] splittedPath, HttpExchange exchange) throws SQLException, IOException {
        InputStream requestBody = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        requestBody.close();

        JSONObject jsonObject = new JSONObject(body.toString());


        String author = userEmail;
        String title = jsonObject.optString("title", null);
        String content = jsonObject.optString("content", null);

        postController.createPost(title, content, author);
        BetterLogger.INFO("Successfully saved post: " + author + " -> " + title);
        return "Done!";
    }

    private String handleCommentPostRequest(String[] splittedPath, HttpExchange exchange) throws SQLException, IOException {
        if (splittedPath.length != 5) {
            BetterLogger.WARNING("Invalid request format for adding comment.");
            return "Invalid request format";
        }


        String postIdStr = splittedPath[2];
        String author = userEmail;
        String content = splittedPath[3];

        try {
            long postId = Long.parseLong(postIdStr);


            commentController.createComment(content, "", author, postId);


            BetterLogger.INFO("Successfully added comment by: " + author + " -> Post ID: " + postId);


            return "Comment added!";
        } catch (NumberFormatException e) {
            BetterLogger.WARNING("Invalid post ID format.");
            return "Invalid post ID format";
        }
    }

    private String handlePutRequest(String[] splittedPath, HttpExchange exchange) throws SQLException, IOException {
        if (splittedPath.length != 4) {
            BetterLogger.WARNING("Invalid request format for PUT.");
            return "Invalid request format";
        }

        String author = userEmail;
        String title = splittedPath[2];
        String newContent = splittedPath[3];

        postController.updatePost(author, title, newContent);
        BetterLogger.INFO("Successfully updated post: " + author + " -> " + title);
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
