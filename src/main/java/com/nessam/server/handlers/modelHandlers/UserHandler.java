package com.nessam.server.handlers.modelHandlers;

import com.nessam.server.controllers.UserController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;


public class UserHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        UserController userController = null;



        try {
            userController = new UserController();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] splittedPath = path.split("/");
        switch (method) {
            case "GET":

                if (splittedPath.length == 2) {
                    try {
                        response = userController.getUsers();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Extract the user ID from the path
                    String userId = splittedPath[splittedPath.length - 1];
                    try {
                        response = userController.getUserById(userId);
                        if (response == null) response = "No User";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "POST":
                // Read the request body
                InputStream requestBody = exchange.getRequestBody();
                BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
                StringBuilder body = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
                requestBody.close();

                // Process the user creation based on the request body
                String newUser = body.toString();
                JSONObject jsonObject = new JSONObject(newUser);
                try {
                    userController.createUser( jsonObject.getString("email"), jsonObject.getString("password"),
                            jsonObject.getString("firstName"), jsonObject.getString("lastName"),
                            jsonObject.getString("additionalName"), jsonObject.getString("profilePicture"),
                            jsonObject.getString("backgroundPicture"), jsonObject.getString("title"),
                            jsonObject.getString("location"), jsonObject.getString("profession"),
                            jsonObject.getString("seekingOpportunity"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Files.createDirectories(Paths.get("src/main/resources/assets/users/user" + jsonObject.getString("email")));
                response = "this is done!";
                break;
            case "PUT":
                response = "This is the response users Put";
                break;
            case "DELETE":
                if (splittedPath.length == 2) {
                    userController.deleteUsers();
                    response = "All users deleted";
                } else {
                    // Extract the user ID from the path
                    String userId = splittedPath[splittedPath.length - 1];
                    userController.deleteUser(userId);
                    response = "This is the response users Delete\nDone!";

                }
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
