package com.nessam.server.utils;

import com.nessam.server.dataAccess.DatabaseConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/download")
public class FileDownloadServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileId = request.getParameter("fileId");

        String filePath = null;
        try {
            filePath = getFilePath(fileId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (filePath != null) {
            File downloadFile = new File(filePath);
            FileInputStream inStream = new FileInputStream(downloadFile);

            // Get MIME type of the file
            String mimeType = getServletContext().getMimeType(filePath);
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            // Set content type
            response.setContentType(mimeType);
            response.setContentLength((int) downloadFile.length());

            // Set headers for file download
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
            response.setHeader(headerKey, headerValue);

            // Get output stream of the response
            OutputStream outStream = response.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // Write bytes to the output stream
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            inStream.close();
            outStream.close();
        } else {
            response.getWriter().println("File not found for the given id!");
        }
    }

    private String getFilePath(String fileId) throws SQLException {
        Connection connection = DatabaseConnectionManager.getConnection();
        String sql = "SELECT path FROM files WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, fileId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString("path");
        }
        return null;
    }
}
