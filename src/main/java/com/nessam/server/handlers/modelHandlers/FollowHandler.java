package com.nessam.server.handlers.modelHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nessam.server.controllers.BlockController;
import com.nessam.server.controllers.FollowController;
import com.nessam.server.controllers.UserController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

public class FollowHandler implements HttpHandler {

    private FollowController followController;
    private UserController userController;
    private BlockController blockController;

    public FollowHandler() throws SQLException {
        this.followController = new FollowController();
        this.userController = new UserController();
//        this.blockController = new BlockController();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] splittedPath = path.split("/");
        String response = "This is the response follows";

        try {
            switch (method) {
                case "GET":
                    response = handleGetRequest(splittedPath);
                    break;
                case "POST":
                    response = handlePostRequest(splittedPath, exchange);
                    break;
                case "DELETE":
                    response = handleDeleteRequest(splittedPath);
                    break;
                default:
                    response = "Method not supported";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response = "Internal Server Error";
            exchange.sendResponseHeaders(500, response.getBytes().length);
        }

        exchange.sendResponseHeaders(200, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private String handleGetRequest(String[] splittedPath) throws SQLException, JsonProcessingException {
        System.out.println(splittedPath[1]);
        System.out.println(splittedPath[2]);
        System.out.println(splittedPath[3]);
         if (splittedPath.length == 4) {
            if (splittedPath[2].equals("followers")) {
                return followController.getFollowers(splittedPath[3]);
            } else if (splittedPath[2].equals("followings")) {
                return followController.getFollows(splittedPath[3]);
            }
        }
        return "WRONG URL";
    }

    private String handlePostRequest(String[] splittedPath, HttpExchange exchange) throws SQLException {
        if (splittedPath.length != 4) {
            return "Invalid request format";
        }
        String user = splittedPath[2];
        String targetUser = splittedPath[3];

        if (!userController.isUserExists(user)) {
            return "user-not-found";
        }

//         if (!user.equals(ExtractUserAuth.extract(exchange))) {
//            return "permission-denied";
//         }
//        if (blockController.isBlocking(user, targetUser) || blockController.isBlocking(targetUser, user)) {
//            return "Blocked";
//        }

        followController.saveFollow(user, targetUser);
        return "Done!";
    }

    private String handleDeleteRequest(String[] splittedPath) throws SQLException {
        if (splittedPath.length == 4) {
            // Implement specific delete logic if needed
            return "Invalid request format for delete";
        } else if (splittedPath.length == 2) {
            followController.deleteAllFollows();
            return "Done!";
        }
        return "Invalid request format for delete";
    }
}
