package com.nessam.server.utils;

import com.nessam.server.dataAccess.DatabaseConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/upload")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {
    private static final String UPLOAD_DIR = "uploads";


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String applicationPath = request.getServletContext().getRealPath("");
        String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;

        File uploadDir = new File(uploadFilePath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        for (Part part : request.getParts()) {
            String fileName = getFileName(part);
            String filePath = uploadFilePath + File.separator + fileName;
            part.write(filePath);

            try {
                saveFilePath(filePath);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        response.getWriter().println("File uploaded successfully!");
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] elements = contentDisposition.split(";");
        for (String element : elements) {
            if (element.trim().startsWith("filename")) {
                return element.substring(element.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }


    private void saveFilePath(String filePath) throws SQLException {
        Connection connection = DatabaseConnectionManager.getConnection();
        String sql = "INSERT INTO files (path) VALUES (?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, filePath);
        statement.executeUpdate();
    }

}
