package com.nessam.server.handlers.modelHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nessam.server.controllers.LikeController;
import com.nessam.server.controllers.PostController;
import com.nessam.server.controllers.UserController;
import com.nessam.server.models.Post;
import com.nessam.server.utils.BetterLogger;
import com.nessam.server.utils.JWTManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.sql.SQLException;
import java.util.Map;

public class LikeHandler implements HttpHandler {

    private final UserController userController;
    private final PostController postController;
    private final LikeController likeController;
    private final JWTManager jwtManager;
    private String userEmail;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LikeHandler() throws SQLException {
        this.userController = new UserController();
        this.postController = new PostController();
        this.likeController = new LikeController();
        this.jwtManager = new JWTManager();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        System.out.println(method);
        String path = exchange.getRequestURI().getPath();
        String[] splittedPath = path.split("/");
        String response = "This is the response likes";
        int statusCode = 200;

        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            BetterLogger.WARNING("Unauthorized access detected.");
            statusCode = 401;
            response = "Unauthorized";
        } else {
            String token = authHeader.substring(7);
            Map<String, Object> tokenData = jwtManager.decodeToken(token);
            if (tokenData == null) {
                response = "Invalid or expired token";
                statusCode = 403;
                BetterLogger.WARNING("Invalid or expired token received.");
            } else {
                setUserEmail(tokenData.get("email").toString());
                try {
                    switch (method) {
                        case "GET":
                            System.out.println("get");
                            response = handleGetRequest(splittedPath, exchange);
                            System.out.println("get");

                            break;
                        case "POST":
                            if (splittedPath.length == 3 && "check".equals(splittedPath[2])) {
                                System.out.println("oooo");
                                response = handleCheckIfLikedRequest(splittedPath, exchange);
                            }
                           else if (splittedPath.length == 3 && "get".equals(splittedPath[2])) {
                                System.out.println("sdasd");
                                response = handleGetRequest(splittedPath, exchange);
                            } else {
                                response = handlePostRequest(splittedPath, exchange);
                            }
                            break;
                        case "DELETE":
                            response = handleDeleteRequest(splittedPath, exchange);
                            break;
                        default:
                            BetterLogger.ERROR("Unsupported HTTP method: " + method);
                            response = "Method not supported";
                            statusCode = 405;
                    }
                } catch (SQLException e) {
                    BetterLogger.ERROR("SQL Exception: " + e.getMessage());
                    response = "Internal Server Error";
                    statusCode = 500;
                } catch (Exception e) {
                    BetterLogger.ERROR("Unexpected Exception 1 : " + e.getMessage());
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


    public String handleCheckIfLikedRequest(String[] splittedPath, HttpExchange exchange) throws SQLException, IOException {
        JSONObject jsonObject = getRequestBody(exchange);
        String postAuthor = jsonObject.optString("postAuthor", null);
        String postTitle = jsonObject.optString("postTitle", null);

        if (postAuthor == null || postTitle == null) {
            BetterLogger.ERROR("Post author or title is missing in the request");
            return "Bad Request";
        }

        try {
            Post post = postController.getPostByAuthorAndTitleAbsolut(postAuthor, postTitle);
            if (post == null) {
                BetterLogger.ERROR("Post not found for author: " + postAuthor + " and title: " + postTitle);
                return "Post Not Found";
            }

            boolean isLiked = likeController.isUserLikedPost(getUserEmail(), post.getId());
            return Boolean.toString(isLiked);
        } catch (SQLException e) {
            BetterLogger.ERROR("SQL Exception: " + e.getMessage());
            return "Internal Server Error";
        } catch (Exception e) {
            BetterLogger.ERROR("Unexpected Exception 2 : " + e.getMessage());
            return "Internal Server Error";
        }
    }

    public String handleGetRequest(String[] splittedPath, HttpExchange exchange) throws IOException, SQLException {
        JSONObject jsonObject = getRequestBody(exchange);
        String postAuthor = jsonObject.optString("postAuthor", null);
        String postTitle = jsonObject.optString("postTitle", null);

        Post post = postController.getPostByAuthorAndTitleAbsolut(postAuthor, postTitle);
        if (post == null) {
            return "There is no post with this ID.";
        } else {
            BetterLogger.INFO("All Likes below: ");
            return likeController.getAllLikes(post.getId());
        }
    }

    public String handlePostRequest(String[] splittedPath, HttpExchange exchange) throws SQLException, IOException {
        JSONObject jsonObject = getRequestBody(exchange);
        String postAuthor = jsonObject.optString("postAuthor", null);
        String postTitle = jsonObject.optString("postTitle", null);

        try {
            Post post = postController.getPostByAuthorAndTitleAbsolut(postAuthor, postTitle);
            likeController.saveLike(getUserEmail(), post);
            BetterLogger.INFO("Successfully saved like: " + getUserEmail() + " -> Post ID: " + post.getId());
            return "Done!";
        } catch (NumberFormatException | JsonProcessingException e) {
            BetterLogger.WARNING("Invalid post ID format.");
            return "Invalid post ID format.";
        }
    }

    public String handleDeleteRequest(String[] splittedPath, HttpExchange exchange) throws IOException {
        JSONObject jsonObject = getRequestBody(exchange);
        String postAuthor = jsonObject.optString("postAuthor", null);
        String postTitle = jsonObject.optString("postTitle", null);

        try {
            Post post = postController.getPostByAuthorAndTitleAbsolut(postAuthor, postTitle);
            likeController.deleteLike(getUserEmail(), post.getId());
            BetterLogger.INFO("Successfully deleted like: " + getUserEmail() + " -> Post ID: " + post.getId());
            return "Successfully unliked the post.";
        } catch (SQLException e) {
            BetterLogger.ERROR("SQL Exception: " + e.getMessage());
            return "Internal Server Error";
        } catch (Exception e) {
            BetterLogger.ERROR("Unexpected Exception 3 : " + e.getMessage());
            return "Internal Server Error";
        }
    }

    public JSONObject getRequestBody(HttpExchange exchange) throws IOException {
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
}
