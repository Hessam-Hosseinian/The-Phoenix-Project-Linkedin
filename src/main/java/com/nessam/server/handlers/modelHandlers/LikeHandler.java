package com.nessam.server.handlers.modelHandlers;

import com.nessam.server.controllers.LikeController;
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
        String path = exchange.getRequestURI().getPath();
        String[] splittedPath = path.split("/");
        String response = "This is the response likes";
        int statusCode = 200;

        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            BetterLogger.WARNING("Unauthorized access detected.");
        } else {
            String token = authHeader.substring(7);
            Map<String, Object> tokenData = jwtManager.decodeToken(token);
            if (tokenData == null) {
                response = "Invalid or expired token";
                statusCode = 403;
                BetterLogger.WARNING("Invalid or expired token received.");
            } else {
                switch (method) {
                    case "GET":
                        try {
                            response = handleGetRequest(splittedPath);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "POST":
                        try {
                            response = handlePostRequest(splittedPath);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "DELETE":
                        response = handleDeleteRequest(splittedPath);
                        break;
                    default:
                        BetterLogger.ERROR("Unsupported HTTP method: " + method);
                        response = "Method not supported";
                        statusCode = 405;
                }
            }

            exchange.sendResponseHeaders(statusCode, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    // GET ip:port/like/All-Likes/postId
    public String handleGetRequest(String[] splittedPath) throws IOException, SQLException {
        if (splittedPath.length != 4) {
            return "you idiot!";
        }
        else if (postController.getPostById(splittedPath[3]) == null) {
            return "there is no post with this id";
        }
        else {
            BetterLogger.INFO("All Likes below: ");
            likeController.getAllLikes(Long.valueOf(splittedPath[3]));
        }
        return "successfully all likes shown";
    }

    // POST ip:port/like/set/postId
    public String handlePostRequest(String[] splittedPath) throws SQLException {
        if (splittedPath.length != 4) {
            return "you idiot!";
        } else {
            likeController.saveLike(Long.valueOf(splittedPath[3]), userEmail);
            return "success";
        }

    }

    // DELETE ip:port/like/dislike/postId
    public String handleDeleteRequest(String[] splittedPath) {
        if (splittedPath.length != 4) {
            return "you idiot!";
        } else {
            likeController.deleteLike(Long.valueOf(splittedPath[3]));
        }
        return "you disliked this post";
    }

}
