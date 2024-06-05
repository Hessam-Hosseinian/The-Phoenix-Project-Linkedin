package com.nessam.server.handlers.modelHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nessam.server.controllers.MessageController;
import com.nessam.server.utils.BetterLogger;
import com.nessam.server.utils.JWTManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import java.io.*;
import java.sql.SQLException;
import java.util.Map;

public class MessageHandler implements HttpHandler {

    private final MessageController messageController;
    private final JWTManager jwtManager;
    private String userEmail;

    public MessageHandler() throws SQLException {
        this.messageController = new MessageController();
        this.jwtManager = new JWTManager();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] splittedPath = path.split("/");
        String response = "This is the response messages";
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
                setUserEmail((String)tokenData.get("email"));
                try {
                    switch (method) {
                        case "GET":
                            response = handleGetRequest(splittedPath);
                            break;
                        case "POST": {
                            response = handlePostRequest(splittedPath, exchange);
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

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    // showing messages for the logged in person

    private String handleGetRequest(String[] splittedPath) {
        // GET ip:port/message/direct/person
        if (splittedPath.length == 4) {
            try {
                return messageController.getMessagesInDirect(splittedPath[3], 20 );
            } catch (SQLException | JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        // GET ip:port/message/direct/person2
        try {
            return messageController.getMessages(userEmail, splittedPath[3]);
        } catch (SQLException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // POST ip:port/message/body
    private String handlePostRequest(String[] splittedPath, HttpExchange exchange) throws IOException {
        InputStream jsonBodyFormat = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(jsonBodyFormat));
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        jsonBodyFormat.close();
        String newMessage = body.toString();
        JSONObject json = new JSONObject(newMessage);
        try {
            messageController.addMessage(json.getString("id"), userEmail, json.getString("receiver"), json.getString("text"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "Successfully Done!";
    }


    private String handleDeleteRequest(String[] splittedPath) throws SQLException {
        // DELETE ip:port/message/delete/messageId
        if (splittedPath.length == 4) {
            try {
                messageController.deleteMessage(splittedPath[3]);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        // DELETE ip:port/message/deleteAll
        else if (splittedPath.length == 3) {
            try {
                messageController.deleteAll();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return "Deleted successfully!";
    }
}
