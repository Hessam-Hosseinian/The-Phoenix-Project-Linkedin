package com.nessam.server.dataAccess;

import com.nessam.server.models.Message;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MessageDAO {
    
    private final Connection connection;

    public MessageDAO() throws SQLException {
        connection = DatabaseConnectionManager.getConnection();
        createMessageTable();
    }

    public void createMessageTable() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS messages (id VARCHAR(60) PRIMARY KEY, sender VARCHAR(36), receiver VARCHAR(36), text VARCHAR(300), createdat bigint)");
        statement.executeUpdate();
    }

    public void saveMessage(Message message) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO messages (id, sender, receiver, text, createdat) VALUES (?, ?, ?, ?, ?)");
        statement.setString(1, message.getId());
        statement.setString(2, message.getSender());
        statement.setString(3, message.getReceiver());
        statement.setString(4, message.getText());
        statement.setLong(5, message.getCreatedAt());
        statement.executeUpdate();
    }

    public void deleteMessage(String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM messages WHERE id = ?");
        statement.setString(1, id);
        statement.executeUpdate();
    }

    public void deleteAll() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM messages");
        statement.executeUpdate();
    }

    public ArrayList<Message> getMessages(String u1, String u2) throws SQLException {
        ArrayList<Message> messages = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages WHERE (sender = ? AND receiver = ?) or (sender = ? AND receiver = ?)");
        statement.setString(1, u1);
        statement.setString(2, u2);
        statement.setString(3, u2);
        statement.setString(4, u1);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Message message = new Message();
            message.setId(resultSet.getString("id"));
            message.setSender(resultSet.getString("sender"));
            message.setReceiver(resultSet.getString("receiver"));
            message.setText(resultSet.getString("text"));
            message.setCreatedAt(resultSet.getLong("createdat"));
            messages.add(message);
        }
        return messages;
    }

    public ArrayList<Message> getMessagesInDirect(String receiver) throws SQLException {
        ArrayList<Message> messages = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM messages WHERE receiver = ?");
        statement.setString(1, receiver);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Message message = new Message();
            message.setId(resultSet.getString("id"));
            message.setSender(resultSet.getString("sender"));
            message.setReceiver(resultSet.getString("receiver"));
            message.setText(resultSet.getString("text"));
            message.setCreatedAt(resultSet.getLong("createdat"));
            messages.add(message);
        }
        return messages;
    }
    //this is a test comment
}
