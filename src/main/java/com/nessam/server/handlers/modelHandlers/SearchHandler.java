package com.nessam.server.handlers.modelHandlers;

import com.nessam.server.controllers.UserController;
import com.nessam.server.utils.BetterLogger;
import com.nessam.server.utils.JWTManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SearchHandler implements HttpHandler {
    private final UserController userController;
    private final JWTManager jwtManager;

    public SearchHandler() throws SQLException {
        this.userController = new UserController();
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

    // GET ip:port/searcherEmail/search/keyword
    public String handleGetRequest(String[] splittedPath) throws IOException, SQLException {
       if (splittedPath.length != 4) {
           return "you idiot!";
       }
       else if (!userController.isUserExists(splittedPath[1])) {
           return "user doesn't exist";
       }
       else {
            userController.search(splittedPath[3]);
       }
       return "the search results shown";
    }
}