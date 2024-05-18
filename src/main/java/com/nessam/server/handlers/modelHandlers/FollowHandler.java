package com.nessam.server.handlers.modelHandlers;


import com.nessam.server.controllers.BlockController;
import com.nessam.server.controllers.FollowController;
import com.nessam.server.controllers.UserController;
import com.nessam.server.controllers.UserController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.nessam.server.utils.ExtractUserAuth;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

public class FollowHandler implements HttpHandler {

    @Override
    public void handle (HttpExchange exchange) throws IOException {
        FollowController followController = null;
        try {
            followController = new FollowController();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        UserController userController = null;
        try {
            userController = new UserController();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        BlockController blockController = null;
        try {
            blockController = new BlockController();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "This is the response follows";
        String[] splittedPath = path.split("/");
        switch (method) {
            case "GET":
                if (splittedPath.length == 2) {
                    try {
                        response = followController.getFollowers(splittedPath[3]);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
                else {
                    if (splittedPath.length == 4 && splittedPath[2].equals("followrs")) {
                        try {
                            response = followController.getFollowers(splittedPath[3]);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
                    else if (splittedPath.length == 4 && splittedPath[2].equals("followings")) {
                        try {
                            response = followController.getFollows(splittedPath[3]);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
                    else {
                        response = "WRONG URL";
                    }
                }
                break;
            case "POST" :
                if (splittedPath.length != 4) {
                    response = "you idiot!!!";
                } else if (!userController.isUserExists(splittedPath[2])) {
                    response = "user-not-found";
                } else if (!splittedPath[2].equals(ExtractUserAuth.extract(exchange))) {
                    response = "permission-denied";
                } else {
                    response = "Done!";
                    try {
                        if (!blockController.isBlocking(splittedPath[2], splittedPath[3]) && !blockController.isBlocking(splittedPath[3], splittedPath[2]))
                            followController.saveFollow(splittedPath[2], splittedPath[3]);
                        else
                            response = "Blocked";
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
                break;
            case "DELETE" :
                if (splittedPath.length != 4) {
                    if (splittedPath.length == 2) {
                        try {
                            followController.deleteAll();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                        response = "Done!";
                    }
                    break;
                }
        }
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }


}
