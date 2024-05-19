package com.nessam.server.handlers.modelHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nessam.server.controllers.UserController;
import com.nessam.server.utils.BetterLogger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

public class UserHandler implements HttpHandler {

    private UserController userController;

    public UserHandler() {
        try {
            userController = new UserController();
        } catch (SQLException e) {
            BetterLogger.ERROR("Error initializing UserController: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {


        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";

        String[] splittedPath = path.split("/");
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
                break;
        }

        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String handleGetRequest(String[] splittedPath) {
        if (splittedPath.length == 2) {
            try {
                return userController.getUsers();
            } catch (SQLException | JsonProcessingException e) {
                BetterLogger.ERROR(e.toString());
                e.printStackTrace();
                return "Error fetching users";
            }
        } else {
            String userId = splittedPath[splittedPath.length - 1];
            try {
                String response = userController.getUserById(userId);
                return response != null ? response : "No User";
            } catch (SQLException | JsonProcessingException e) {
                BetterLogger.ERROR(e.toString());
                e.printStackTrace();
                return "Error fetching users";
            }
        }
    }

    private String handlePostRequest(HttpExchange exchange) throws IOException {
        try {
            InputStream requestBody = exchange.getRequestBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            requestBody.close();

            JSONObject jsonObject = new JSONObject(body.toString());
            userController.createUser(
                    jsonObject.getString("email"),
                    jsonObject.getString("password"),
                    jsonObject.getString("firstName"),
                    jsonObject.getString("lastName"),
                    jsonObject.getString("additionalName"),
                    jsonObject.getString("profilePicture"),
                    jsonObject.getString("backgroundPicture"),
                    jsonObject.getString("title"),
                    jsonObject.getString("location"),
                    jsonObject.getString("profession"),
                    jsonObject.getString("seekingOpportunity")
            );


            Files.createDirectories(Paths.get("src/main/resources/assets/users/user" + jsonObject.getString("email")));
          BetterLogger.INFO("User created successfully");
            return "User created successfully";
        } catch (Exception e) {
            e.printStackTrace();
            BetterLogger.ERROR("Can not create user");
            return "Error creating user";
        }
    }

    private String handlePutRequest(HttpExchange exchange) {
        return "This is the response for PUT request (UserHandler)";
    }

    private String handleDeleteRequest(String[] splittedPath) {
        if (splittedPath.length == 2) {
            userController.deleteUsers();
            BetterLogger.INFO("All user deleted successfully");
            return "All users deleted";
        } else {
            String userId = splittedPath[splittedPath.length - 1];
            userController.deleteUser(userId);
            BetterLogger.INFO("One user deleted successfully");
            return "User deleted successfully";
        }
    }
}
