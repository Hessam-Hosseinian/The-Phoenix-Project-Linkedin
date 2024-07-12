package com.nessam.server.handlers.httpHandlers;

import com.nessam.server.dataAccess.DatabaseConnectionManager;
import com.nessam.server.utils.BetterLogger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class FileUploadHandler implements HttpHandler {
    private static final String UPLOAD_DIR = "src/main/java/com/nessam/server/assets/uploads/";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] splittedPath = path.split("/");
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            File uploadDir = new File(UPLOAD_DIR + "/" + splittedPath[2]);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String boundary = parseBoundary(exchange.getRequestHeaders().getFirst("Content-Type"));
            InputStream inputStream = exchange.getRequestBody();

            // Improved file name handling to preserve original extension
            String fileName = splittedPath[3];
            File uploadedFile = new File(UPLOAD_DIR + splittedPath[2] +"/"+ fileName);

            try (FileOutputStream outputStream = new FileOutputStream(uploadedFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                BetterLogger.ERROR("File upload error: " + e.getMessage());
                exchange.sendResponseHeaders(500, -1);
                return;
            }

            try {
                saveFilePath(uploadedFile.getAbsolutePath(), splittedPath[2]+":"+fileName);
            } catch (SQLException e) {
                BetterLogger.ERROR(e.getMessage());
                exchange.sendResponseHeaders(500, -1);
                return;
            }

            String response = "File uploaded successfully!";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }

    private String parseBoundary(String contentType) {
        for (String param : contentType.split(";")) {
            if (param.trim().startsWith("boundary")) {
                return param.split("=")[1];
            }
        }
        return null;
    }

    private void saveFilePath(String filePath, String filename) throws SQLException {
        Connection connection = DatabaseConnectionManager.getConnection();
        String sql = "INSERT INTO files (path,email_cc) VALUES (?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, filePath);
        statement.setString(2, filename);
        statement.executeUpdate();

    }
}
