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
import java.sql.ResultSet;
import java.sql.SQLException;

public class FileDownloadHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            String email_cc = exchange.getRequestURI().getQuery().split("=")[1];
            String filePath = null;
            try {
                filePath = getFilePath(email_cc);
            } catch (SQLException e) {
                BetterLogger.ERROR(e.getMessage());
            }

            if (filePath != null) {
                File downloadFile = new File(filePath);
                FileInputStream inStream = new FileInputStream(downloadFile);

                String mimeType = Files.probeContentType(Paths.get(filePath));
                if (mimeType == null) {
                    mimeType = "application/octet-stream";
                }

                exchange.getResponseHeaders().add("Content-Type", mimeType);
                exchange.getResponseHeaders().add("Content-Disposition", "attachment; filename=\"" + downloadFile.getName() + "\"");
                exchange.sendResponseHeaders(200, downloadFile.length());

                OutputStream os = exchange.getResponseBody();
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inStream.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.close();
                inStream.close();
            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }

    private String getFilePath(String email) throws SQLException {

        Connection connection = DatabaseConnectionManager.getConnection();
        String sql = "SELECT path FROM files WHERE email_cc= ?";
        PreparedStatement statement = connection.prepareStatement(sql);
//        statement.setString(1, fileId);
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString("path");
        }

        return null;
    }
}
