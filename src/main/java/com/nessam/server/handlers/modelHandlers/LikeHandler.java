package com.nessam.server.handlers.modelHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    private final LikeController likeController;
    private final UserController userController;
    private final PostController postController;
    private final JWTManager jwtManager;

    public LikeHandler(LikeController likeController, UserController userController, PostController postController, JWTManager jwtManager) {
        this.likeController = likeController;
        this.userController = userController;
        this.postController = postController;
        this.jwtManager = jwtManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] splittedPath = path.split("/");
        String response = "This is the response likes";
        int statusCode = 202;

        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            BetterLogger.WARNING("Unauthorized access detected.");
        } else {
            String token = authHeader.substring(7);
            Map<String, Object> tokenData = jwtManager.decodeToken(token);
            if (tokenData == null) {
                response = "Invalid or expired token";
                statusCode = 402;
                BetterLogger.WARNING("Invalid or expired token received.");
            } else {
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
                        statusCode = 406;
                }
            }

            exchange.sendResponseHeaders(statusCode, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    public String handleGetRequest(String[] splittedPath) throws IOException {
        if (splittedPath.length ==  2) {
            try {
                BetterLogger.INFO("Successfully retrieved all likes");
                return likeController.getAllLikes();
            } catch (SQLException | JsonProcessingException e) {
                BetterLogger.ERROR("Error: " + e.getMessage());
                return "an error occurred";
            }
        } else {
            if (splittedPath.length == 4 && splittedPath[2].equals("likers")) {
                try {
                    BetterLogger.INFO("Successfully retrieved all likers");
                    return likeController.getLikes(splittedPath[3]);
                } catch (SQLException e) {
                    BetterLogger.ERROR("Error: " + e.getMessage());
                    return "an error occurred";
                }
            } else if (splittedPath.length == 4 && splittedPath[2].equals("likes")) {
                try {
                    BetterLogger.INFO("Successfully retrieved all likes");
                    return likeController.getLikes(splittedPath[3]);
                } catch (SQLException e) {
                    BetterLogger.ERROR("Error: " + e.getMessage());
                    return "an error occurred";
                }
            }
            else {
                return "WRONG URL";
            }
        }
    }

    public String handlePostRequest(String[] splittedPath) {
        if (splittedPath.length != 4) {
            return "you idiot!";
        }
        else if (!userController.isUserExists(splittedPath[2])) {
            return "user doesn't exist";
        } else {
            try {
                likeController.saveLike(splittedPath[2], splittedPath[3]);
                BetterLogger.INFO("new like added");
            } catch (SQLException e) {
                BetterLogger.ERROR("Error: " + e.getMessage());
                return "an error occurred";
            }
            return "Success!";
        }
    }

    public String handleDeleteRequest(String[] splittedPath) {
        if (splittedPath.length != 4) {
            if (splittedPath.length == 2) {
                try {
                    likeController.deleteAllLikes();
                    BetterLogger.INFO("All likes deleted");
                } catch (SQLException e) {
                    BetterLogger.ERROR("Error: " + e.getMessage());
                    return "an error occurred";
                }
                return "Success!";
            } else {
                return "you idiot!";
            }
        } else {
            try {
                if (!userController.isUserExists(splittedPath[2]) || postController.getPostById(splittedPath[3]) == null) {
                    return "user doesn't exist";
                } else {
                    try {
                        likeController.deleteLike(splittedPath[2], splittedPath[3]);
                        BetterLogger.INFO("your specified like deleted successfully");
                    } catch (SQLException e) {
                        BetterLogger.ERROR("Error: " + e.getMessage());
                        return "an error occurred";
                    }
                    return "Success!";
                }
            } catch (SQLException | JsonProcessingException e) {
                BetterLogger.ERROR("Error: " + e.getMessage());
                return "an error occurred";
            }
        }
    }

}
