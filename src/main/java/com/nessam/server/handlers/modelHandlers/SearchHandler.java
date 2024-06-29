package com.nessam.server.handlers.modelHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
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

public class SearchHandler implements HttpHandler {
    private final UserController userController;
    private final PostController postController;
    private final JWTManager jwtManager;

    public SearchHandler() throws SQLException {
        this.userController = new UserController();
        this.postController = new PostController();
        this.jwtManager = new JWTManager();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] splittedPath = path.split("/");
        String response = "This is the response likes";
        int statusCode = 203;

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

                    default:
                        BetterLogger.ERROR("Unsupported HTTP method: " + method);
                        response = "Method not supported";
                        statusCode = 407;
                }
            }

            exchange.sendResponseHeaders(statusCode, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }


    // GET ip:port/search/searchUser/keyword
    // GET ip:port/search/searchPost/keyword
    private String handleGetRequest(String[] splittedPath) throws SQLException, JsonProcessingException {
        if (splittedPath.length != 4) {
            BetterLogger.ERROR("Invalid URL format for search.");
            return "Invalid URL format.";
        }
        else if (splittedPath[2].equals("searchUser")) {
            BetterLogger.INFO("Searching user with keyword: " + splittedPath[3]);
            return userController.searchUser(splittedPath[3]);
        }
//        else if (splittedPath[2].equals("searchPost")) {
//            BetterLogger.INFO("Searching post with keyword: " + splittedPath[3]);
//            return postController.searchPost(splittedPath[3]);
//        }
        return "No search operation executed.";
    }
}
