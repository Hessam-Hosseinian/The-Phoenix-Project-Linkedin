//package com.nessam.server.dataAccess;
//
//
//
//import com.nessam.server.models.Direct;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//
//public class DirectDAO {
//    private final Connection connection;
//
//    public DirectDAO() throws SQLException {
//        connection = DatabaseConnectionManager.getConnection();
//        createDirectTable();
//    }
//
//    public void createDirectTable() throws SQLException {
//        PreparedStatement preparedStatement = connection.prepareStatement(
//                "CREATE TABLE IF NOT EXISTS direct (" +
//                        "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
//                        "sender VARCHAR(36), " +
//                        "receiver VARCHAR(36), " +
//                        "lastMessage VARCHAR(300))"
//        );
//        preparedStatement.executeUpdate();
//        System.out.println("Direct table created");
//    }
//
//    public void saveDirect(Direct direct) throws SQLException {
//        PreparedStatement preparedStatement = connection.prepareStatement(
//                "INSERT INTO direct (sender, receiver, lastMessage) VALUES (?, ?, ?)"
//        );
//        preparedStatement.setString(1, direct.getSender());
//        preparedStatement.setString(2, direct.getReceiver());
//        preparedStatement.setString(3, direct.getLastMessage());
//        preparedStatement.executeUpdate();
//    }
//
//    public void deleteDirect(String id) throws SQLException {
//        PreparedStatement preparedStatement = connection.prepareStatement(
//                "DELETE FROM direct WHERE id = ?"
//        );
//        preparedStatement.setString(1, id);
//        preparedStatement.executeUpdate();
//    }
//
//    public void deleteAll() throws SQLException {
//        PreparedStatement preparedStatement = connection.prepareStatement(
//                "DELETE FROM direct"
//        );
//        preparedStatement.executeUpdate();
//    }
//
//    public ArrayList<Direct> getDirects(String u1, String u2) throws SQLException {
//        ArrayList<Direct> directs = new ArrayList<>();
//        PreparedStatement statement = connection.prepareStatement(
//                "SELECT * FROM direct WHERE (sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?)"
//        );
//        statement.setString(1, u1);
//        statement.setString(2, u2);
//        statement.setString(3, u2);
//        statement.setString(4, u1);
//        ResultSet resultSet = statement.executeQuery();
//        while (resultSet.next()) {
//            Direct direct = new Direct();
//            direct.setId(resultSet.getString("id"));
//            direct.setSender(resultSet.getString("sender"));
//            direct.setReceiver(resultSet.getString("receiver"));
//            direct.setLastMessage(resultSet.getString("lastMessage"));
//            directs.add(direct);
//        }
//        return directs;
//    }
//
//    public ArrayList<Direct> getNotify(String receiver) throws SQLException {
//        ArrayList<Direct> directs = new ArrayList<>();
//        PreparedStatement statement = connection.prepareStatement(
//                "SELECT * FROM direct WHERE receiver = ?"
//        );
//        statement.setString(1, receiver);
//        ResultSet resultSet = statement.executeQuery();
//        while (resultSet.next()) {
//            Direct direct = new Direct();
//            direct.setId(resultSet.getString("id"));
//            direct.setSender(resultSet.getString("sender"));
//            direct.setReceiver(resultSet.getString("receiver"));
//            direct.setLastMessage(resultSet.getString("lastMessage"));
//            directs.add(direct);
//        }
//        return directs;
//    }
//}
