package com.nessam.server.handlers.modelHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nessam.server.controllers.UserController;
import com.nessam.server.models.UserContactInfo;
import com.nessam.server.utils.BetterLogger;
import com.nessam.server.utils.JWTManager;
import com.nessam.server.utils.Validation;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Map;

public class UserHandler implements HttpHandler {

    private final JWTManager jwtManager;
    private UserController userController;

    public UserHandler() {
        jwtManager = new JWTManager();
        try {
            userController = new UserController();
        } catch (SQLException e) {
            BetterLogger.ERROR("Error initializing UserController: " + e);
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] splittedPath = path.split("/");
        String response = "This is the response follows";
        int statusCode = 200;

        try {
            switch (method) {
                case "GET":
                    response = handleGetRequest(splittedPath);
                    break;
                case "POST":
                    response = handlePostRequest(exchange, splittedPath);
                    break;
                case "PUT":
                    response = handlePutRequest(exchange);
                    break;
                case "DELETE":
                    response = handleDeleteRequest(splittedPath);
                    break;
                default:
                    response = "Method not allowed";
                    statusCode = 405;
                    break;
            }
        } catch (Exception e) {
            response = "Internal Server Error";
            statusCode = 500;
            BetterLogger.ERROR("Exception: " + e.getMessage());
        }

        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public String handleGetRequest(String[] splittedPath) throws SQLException, JsonProcessingException {
        if (splittedPath.length == 2) {
            BetterLogger.INFO("Users received");
            return userController.getUsers();
        } else if (splittedPath.length == 4 && splittedPath[2].equals("contactInfo")) {
            String userId = splittedPath[3];
            BetterLogger.INFO("User contact info received");
            return userController.getUserContactInfoByUserId(userId);

        } else if (splittedPath.length == 4 && splittedPath[2].equals("education")) {
            String userId = splittedPath[3];
            BetterLogger.INFO("User contact info received");
            return userController.getUserContactInfoByUserId(userId);
        } else if (splittedPath.length == 4 && splittedPath[2].equals("jwt")) {
            String token = "Bearer " + splittedPath[3];


            Map<String, Object> tokenData = jwtManager.decodeBearerToken(token);
            String userId = (String) tokenData.get("email");

            BetterLogger.INFO("User contact info received");
            return userController.getUserById(userId);
        } else {
            String tmpUserEmail = splittedPath[splittedPath.length - 1];
            String response = userController.getUserById(tmpUserEmail);
            BetterLogger.INFO("User received");
            return response != null ? response : "No User";
        }
    }

    public String handlePostRequest(HttpExchange exchange, String[] splittedPath) throws IOException {

        if (splittedPath.length == 2) {
            try {
                JSONObject jsonObject = parseRequestBody(exchange);
                validateUserDetails(jsonObject);

                userController.createUser(jsonObject);
                Files.createDirectories(Paths.get("src/main/resources/assets/users/user" + jsonObject.getString("email")));

                BetterLogger.INFO("User created successfully");
                return "User created successfully";
            } catch (Exception e) {
                BetterLogger.ERROR("Cannot create user: " + e.getMessage());
                return "Error creating user";
            }
        } else if (splittedPath.length == 3 && splittedPath[2].equals("contactInfo")) {
            try {
                JSONObject jsonObject = parseRequestBody(exchange);
//                validateContactInfoDetails(jsonObject);

                userController.createUserContactInfo(jsonObject);

                BetterLogger.INFO("User contact info created successfully");
                return "User contact info created successfully";
            } catch (Exception e) {
                BetterLogger.ERROR("Cannot create user contact info: " + e.getMessage());
                return "Error creating user contact info";
            }
        } else if (splittedPath.length == 3 && splittedPath[2].equals("education")) {
            try {
                JSONObject jsonObject = parseRequestBody(exchange);
//                validateContactInfoDetails(jsonObject);

//                userController.creteUserEducation(jsonObject);

                BetterLogger.INFO("User education created successfully");
                return "User contact info created successfully";
            } catch (Exception e) {
                BetterLogger.ERROR("Cannot create user contact info: " + e.getMessage());
                return "Error creating user contact info";
            }
        }
        return "Error creating user";
    }

    private String handlePutRequest(HttpExchange exchange) throws IOException {
        try {
            JSONObject jsonObject = parseRequestBody(exchange);
            validateUserDetails(jsonObject);

            userController.updateUser(jsonObject);
            BetterLogger.INFO("User updated successfully");
            return "User updated successfully";
        } catch (Exception e) {
            BetterLogger.ERROR("Cannot update user: " + e.getMessage());
            return "Error updating user";
        }
    }

    public String handleDeleteRequest(String[] splittedPath) {
        if (splittedPath.length == 2) {
            userController.deleteUsers();
            BetterLogger.INFO("All users deleted successfully");
            return "All users deleted";
        } else {
            String userEmail = splittedPath[splittedPath.length - 1];
            userController.deleteUser(userEmail);
            BetterLogger.INFO("User deleted successfully");
            return "User deleted successfully";
        }
    }

    private JSONObject parseRequestBody(HttpExchange exchange) throws IOException {
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

    private void validateUserDetails(JSONObject jsonObject) {
        boolean emailMatch = Validation.isValidEmail(jsonObject.optString("email"));
        if (!emailMatch) {
            BetterLogger.INFO("Incorrect email format");
            throw new IllegalArgumentException("Incorrect email format");
        }

        boolean passwordValidation = Validation.validatePassword(jsonObject.optString("password"));
        if (!passwordValidation) {
            BetterLogger.INFO("Incorrect password format");
            throw new IllegalArgumentException("Incorrect password format");
        }
    }
}
