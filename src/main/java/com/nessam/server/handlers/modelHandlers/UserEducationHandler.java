package com.nessam.server.handlers.modelHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nessam.server.controllers.UserEducationController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.sql.SQLException;

public class UserEducationHandler implements HttpHandler {

    private final UserEducationController educationController;
    private final ObjectMapper objectMapper;

    public UserEducationHandler() throws SQLException {
        this.educationController = new UserEducationController();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] splittedPath = path.split("/");
        try {
            switch (method) {
                case "GET":
                    handleGet(splittedPath,exchange);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "PUT":
                    handlePut(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange);
                    break;
                default:
                    sendErrorResponse(exchange, 405, "Method Not Allowed");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(exchange, 500, "Internal Server Error: " + e.getMessage());
        }
    }

    private void handleGet(String[]strings ,HttpExchange exchange) throws IOException, SQLException {
     String userId = strings[2];

        String educations = educationController.getUserEducations(userId);
        System.out.println(educations);
        sendResponse(exchange, 200, educations);
    }

    private void handlePost(HttpExchange exchange) throws IOException, SQLException {
        JSONObject jsonObject = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
        educationController.createUserEducation(jsonObject);
        sendResponse(exchange, 200, "User education created successfully");
    }

    private void handlePut(HttpExchange exchange) throws IOException, SQLException {
        JSONObject jsonObject = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
        educationController.updateUserEducation(jsonObject);
        sendResponse(exchange, 200, "User education updated successfully");
    }

    private void handleDelete(HttpExchange exchange) throws IOException, SQLException {
        String query = exchange.getRequestURI().getQuery();
//        long educationId = parseEducationIdFromQuery(query);
//        educationController.deleteUserEducation(educationId);
        sendResponse(exchange, 200, "User education deleted successfully");
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        exchange.sendResponseHeaders(statusCode, message.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(message.getBytes());
        os.close();
    }

    private JSONObject getRequestBody(HttpExchange exchange) throws IOException {
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

}
