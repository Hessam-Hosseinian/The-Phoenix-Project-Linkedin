package com.nessam.server.dataAccess;

import com.nessam.server.models.Connect;
import com.nessam.server.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectDAO {
    private final Connection connection;
    public ConnectDAO() throws SQLException {
        connection = DatabaseConnectionManager.getConnection();
        createConnectTable();
    }
    public void createConnectTable() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS connects (id  BIGINT AUTO_INCREMENT PRIMARY KEY,person1 VARCHAR(36), person2 VARCHAR(36))");
        preparedStatement.executeUpdate();
    }

    public void saveConnect(Connect connect) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO connects (person1, person2) VALUES (?, ?)");
        preparedStatement.setString(1, connect.getPerson1());
        preparedStatement.setString(2, connect.getPerson2());
        preparedStatement.executeUpdate();
    }

    public void deleteConnect(Connect connect) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM connects WHERE person1 = ? AND person2 = ?");
        preparedStatement.setString(1, connect.getPerson1());
        preparedStatement.setString(2, connect.getPerson2());
        preparedStatement.executeUpdate();
    }

    public void deleteAllConnects() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM connects");
        preparedStatement.executeUpdate();
    }


    public List<Connect> getPerson2s(String userId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM connects WHERE person1 = ?");
        preparedStatement.setString(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Connect> connects = new ArrayList<>();
        while (resultSet.next()) {
            Connect connect = new Connect();
            connect.setPerson1(resultSet.getString("person1"));
            connect.setPerson2(resultSet.getString("person2"));
            connects.add(connect);
        }
        return connects;
    }

    public List<Connect> getPerson1s(String userId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM connects WHERE person2 = ?");
        preparedStatement.setString(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Connect> connects = new ArrayList<>();
        while (resultSet.next()) {
            Connect connect = new Connect();
            connect.setPerson1(resultSet.getString("person1"));
            connect.setPerson2(resultSet.getString("person2"));
            connects.add(connect);
        }
        return connects;
    }

    public List<Connect> getAllConnect() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM connects");
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Connect> connects = new ArrayList<>();
        while (resultSet.next()) {
            Connect connect = new Connect();
            connect.setPerson1(resultSet.getString("person1"));
            connect.setPerson2(resultSet.getString("person2"));
            connects.add(connect);
        }
        return connects;
    }

    public boolean isConnecting(String person1Id, String person2Id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM connects WHERE person1 = ? AND person2 = ?");
        preparedStatement.setString(1, person1Id);
        preparedStatement.setString(2, person2Id);
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean isConnecting = resultSet.next();
        return isConnecting;
    }



}
