package com.nessam.server.handlers.modelHandlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nessam.server.controllers.UserController;
import com.nessam.server.utils.BetterLogger;
import com.nessam.server.utils.JWTManager;
import com.nessam.server.utils.Validation;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

public class UserHandler implements HttpHandler {

    private final JWTManager jwtManager;
    private final Gson gson;
    private UserController userController;

    public UserHandler() {
        jwtManager = new JWTManager();
        gson = new Gson();
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
                    response = handlePostRequest(exchange);
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
            BetterLogger.ERROR("Exception handling request: " + e);
            response = "Internal server error";
            statusCode = 500;
        }

        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String handleGetRequest(String[] splittedPath) {
        try {
            if (splittedPath.length == 2) {
                BetterLogger.INFO("Fetching all users");
                return userController.getUsers();
            } else {
                String userEmail = splittedPath[splittedPath.length - 1];
                BetterLogger.INFO("Fetching user: " + userEmail);
                String response = userController.getUserById(userEmail);
                return response != null ? response : "No User";
            }
        } catch (SQLException | IOException e) {
            BetterLogger.ERROR("Error fetching users: " + e);
            return "Error fetching users";
        }
    }

    private String handlePostRequest(HttpExchange exchange) throws IOException {
        try {
            InputStream requestBody = exchange.getRequestBody();
            String jsonString = new BufferedReader(new InputStreamReader(requestBody))
                    .lines()
                    .reduce("", (accumulator, actual) -> accumulator + actual);

            JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);

            String email = jsonObject.get("email").getAsString();
            String password = jsonObject.get("password").getAsString();

            if (!Validation.isValidEmail(email)) {
                BetterLogger.INFO("Invalid email format");
                return "Invalid email format";
            }

            if (!Validation.validatePassword(password)) {
                BetterLogger.INFO("Invalid password format");
                return "Invalid password format";
            }

            userController.createUser(
                    email,
                    password,
                    jsonObject.get("firstName").getAsString(),
                    jsonObject.get("lastName").getAsString(),
                    jsonObject.has("additionalName") ? jsonObject.get("additionalName").getAsString() : null,
                    jsonObject.has("profilePicture") ? jsonObject.get("profilePicture").getAsString() : null,
                    jsonObject.has("backgroundPicture") ? jsonObject.get("backgroundPicture").getAsString() : null,
                    jsonObject.has("title") ? jsonObject.get("title").getAsString() : null,
                    jsonObject.has("location") ? jsonObject.get("location").getAsString() : null,
                    jsonObject.has("profession") ? jsonObject.get("profession").getAsString() : null,
                    jsonObject.has("seekingOpportunity") ? jsonObject.get("seekingOpportunity").getAsString() : null
            );

            Files.createDirectories(Paths.get("src/main/resources/assets/users/user" + email));
            BetterLogger.INFO("User created successfully");
            return "User created successfully";
        } catch (Exception e) {
            BetterLogger.ERROR("Error creating user: " + e);
            return "Error creating user";
        }
    }

    private String handlePutRequest(HttpExchange exchange) {
        try {
            InputStream requestBody = exchange.getRequestBody();
            String jsonString = new BufferedReader(new InputStreamReader(requestBody))
                    .lines()
                    .reduce("", (accumulator, actual) -> accumulator + actual);

            JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);

            String email = jsonObject.get("email").getAsString();
            String newPassword = jsonObject.get("newPassword").getAsString();

            if (!Validation.validatePassword(newPassword)) {
                BetterLogger.INFO("Invalid password format");
                return "Invalid password format";
            }

            if (!Validation.matchingPasswords(newPassword, jsonObject.get("repeatedPass").getAsString())) {
                BetterLogger.INFO("Passwords do not match");
                return "Passwords do not match";
            }

            userController.createUser(
                    email,
                    newPassword,
                    jsonObject.has("firstName") ? jsonObject.get("firstName").getAsString() : null,
                    jsonObject.has("lastName") ? jsonObject.get("lastName").getAsString() : null,
                    jsonObject.has("additionalName") ? jsonObject.get("additionalName").getAsString() : null,
                    jsonObject.has("profilePicture") ? jsonObject.get("profilePicture").getAsString() : null,
                    jsonObject.has("backgroundPicture") ? jsonObject.get("backgroundPicture").getAsString() : null,
                    jsonObject.has("title") ? jsonObject.get("title").getAsString() : null,
                    jsonObject.has("location") ? jsonObject.get("location").getAsString() : null,
                    jsonObject.has("profession") ? jsonObject.get("profession").getAsString() : null,
                    jsonObject.has("seekingOpportunity") ? jsonObject.get("seekingOpportunity").getAsString() : null
            );

            BetterLogger.INFO("User updated successfully");
            return "User updated successfully";
        } catch (Exception e) {
            BetterLogger.ERROR("Error updating user: " + e);
            return "Error updating user";
        }
    }

    private String handleDeleteRequest(String[] splittedPath) {
        if (splittedPath.length == 2) {
            userController.deleteUsers();
            BetterLogger.INFO("All users deleted successfully");
            return "All users deleted";
        } else {
            String userEmail = splittedPath[splittedPath.length - 1];
            userController.deleteUser(userEmail);

            if (!userController.isUserExists(userEmail)) {
                BetterLogger.INFO("User deleted successfully");
                return "User deleted successfully";
            } else {
                BetterLogger.ERROR("No user with this email");
                return "User deletion failed";
            }
        }
    }
}
