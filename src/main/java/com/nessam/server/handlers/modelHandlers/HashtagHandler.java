package com.nessam.server.handlers.modelHandlers;

import com.nessam.server.controllers.HashtagController;
import com.nessam.server.controllers.PostController;
import com.nessam.server.controllers.UserController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

public class HashtagHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HashtagController hashtagController = null;
        try {
            hashtagController = new HashtagController();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        UserController userController = null;
        try {
            userController = new UserController();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        PostController postController = null;
        try {
            postController = new PostController();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] splittedPath = path.split("/");

        switch (method) {
            case "GET":
                if (splittedPath.length != 3) { // ip:port/hashtag/tagName
                    response = "wtf";
                    break;
                }
                try {
                    response = hashtagController.GetHashtag(splittedPath[2]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "POST": // ip:port/hashtag/tagName/postId
                if (splittedPath.length != 4) {
                    response = "wtf";
                    break;
                }
//                try {
//                    if (postController.getPostById((splittedPath[3])) == null) {
//                        response = "tweet-not-found";
//                    } else if (!userController.isUserExists(ExtractUserAuth.extract(exchange))) {
//                        response = "permission-denied";
//                    } else {
//                        try {
//                            hashtagController.addHashtag(splittedPath[2], splittedPath[3]);
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                            throw new RuntimeException(e);
//                        }
//                        response = "Done!";
//                    }
//                } catch (SQLException e) {
//                    throw new RuntimeException(e);
//                }
                break;
            case "DELETE":
                try {
                    hashtagController.deleteAll();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                response = "Done!";
                break;
            default:
                break;
        }

        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}

