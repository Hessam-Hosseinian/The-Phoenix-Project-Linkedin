package com.nessam.server.handlers.modelHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.nessam.server.controllers.EducationController;
import com.nessam.server.models.Education;
import com.nessam.server.utils.BetterLogger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

public class EducationHandler implements HttpHandler {

    private EducationController educationController;

    public EducationHandler() {
        try {
            educationController = new EducationController();
        } catch (SQLException e) {
            BetterLogger.ERROR("Error initializing EducationController: " + e.toString());
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
        try {
            if (splittedPath.length == 3 && splittedPath[2].equals("user")) {
                String userEmail = splittedPath[splittedPath.length - 1];
                BetterLogger.INFO("Education records for user received");
                return educationController.getEducationsByUser(userEmail);
            } else if (splittedPath.length == 3) {
                Long educationId = Long.parseLong(splittedPath[splittedPath.length - 1]);
                BetterLogger.INFO("Education record received");
                return educationController.getEducationById(educationId);
            } else {
                BetterLogger.INFO("All education records received");
                return educationController.getAllEducations();
            }
        } catch (SQLException | IOException e) {
            BetterLogger.ERROR(e.toString());
            return "Error fetching education records";
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
            educationController.createEducation(
                    jsonObject.getString("user_email"),
                    jsonObject.getString("institution"),
                    jsonObject.getString("degree"),
                    jsonObject.getString("field_of_study"),
                    jsonObject.getString("start_date"),
                    jsonObject.getString("end_date")


                    );


            Files.createDirectories(Paths.get("src/main/resources/assets/education/user" + jsonObject.getString("email")));


            BetterLogger.INFO("Education record created successfully");
            return "Education record created successfully";
        } catch (Exception e) {
            e.printStackTrace();
            BetterLogger.ERROR("Error creating education record");
            return "Error creating education record";
        }
    }

    private String handlePutRequest(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String[] splittedPath = path.split("/");
            if (splittedPath.length == 3) {
                Long educationId = Long.parseLong(splittedPath[splittedPath.length - 1]);

                InputStream requestBody = exchange.getRequestBody();
                BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
                StringBuilder body = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
                requestBody.close();

                ObjectMapper objectMapper = new ObjectMapper();
                Education updatedEducation = objectMapper.readValue(body.toString(), Education.class);

                educationController.updateEducation(educationId, updatedEducation);
                BetterLogger.INFO("Education record updated successfully");
                return "Education record updated successfully";
            } else {
                BetterLogger.ERROR("Invalid URL format for PUT request");
                return "Invalid URL format for PUT request";
            }
        } catch (Exception e) {
            e.printStackTrace();
            BetterLogger.ERROR("Error updating education record");
            return "Error updating education record";
        }
    }

    private String handleDeleteRequest(String[] splittedPath) {
        try {
            if (splittedPath.length == 3) {
                Long educationId = Long.parseLong(splittedPath[splittedPath.length - 1]);
                educationController.deleteEducation(educationId);
                BetterLogger.INFO("Education record deleted successfully");
                return "Education record deleted successfully";
            } else {
                BetterLogger.ERROR("Invalid URL format for DELETE request");
                return "Invalid URL format for DELETE request";
            }
        } catch (Exception e) {
            e.printStackTrace();
            BetterLogger.ERROR("Error deleting education record");
            return "Error deleting education record";
        }
    }
}
