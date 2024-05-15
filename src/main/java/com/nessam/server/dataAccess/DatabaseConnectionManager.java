package com.nessam.server.dataAccess;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/hessardatabase";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "nessar";
    //
    private static Connection connection;

    private DatabaseConnectionManager() {
        // Private constructor to prevent instantiation
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
        return connection;
    }

}
