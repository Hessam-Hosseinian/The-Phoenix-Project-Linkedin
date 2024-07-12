package com.nessam.server.dataAccess;

import com.nessam.server.models.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessageDAO {
    private final Connection connection;

    public MessageDAO() throws SQLException {
        connection = DatabaseConnectionManager.getConnection();
        createMessageTable();
    }

    public void createMessageTable() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS messages (id BIGINT AUTO_INCREMENT PRIMARY KEY, sender VARCHAR(36), receiver VARCHAR(36), text VARCHAR(300), createdat bigint)");
        preparedStatement.executeUpdate();

    }

    public void saveMessage(Message message) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO messages (sender, receiver, text, createdat) VALUES (?, ?, ?, ?)");
        preparedStatement.setString(1, message.getSender());
        preparedStatement.setString(2, message.getReceiver());
        preparedStatement.setString(3, message.getText());
        preparedStatement.setLong(4, message.getCreatedAt());
        preparedStatement.executeUpdate();
    }

    public void deleteMessage(String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM messages WHERE id = ?");
        preparedStatement.setString(1, id);
        preparedStatement.executeUpdate();
    }

    public void deleteAll() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM messages");
        preparedStatement.executeUpdate();
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

    public ArrayList<Message> getNotify(String receiver) throws SQLException {
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

    public Map<String, Message> getLastMessagesWithUsers(String user) throws SQLException {
        Map<String, Message> lastMessages = new HashMap<>();

        // Get distinct users that the user has chatted with
        PreparedStatement distinctUsersStatement = connection.prepareStatement(
                "SELECT DISTINCT CASE WHEN sender = ? THEN receiver ELSE sender END AS chatUser " +
                        "FROM messages WHERE sender = ? OR receiver = ?"
        );
        distinctUsersStatement.setString(1, user);
        distinctUsersStatement.setString(2, user);
        distinctUsersStatement.setString(3, user);
        ResultSet distinctUsersResult = distinctUsersStatement.executeQuery();

        while (distinctUsersResult.next()) {
            String chatUser = distinctUsersResult.getString("chatUser");

            // Get the last message exchanged with this user
            PreparedStatement lastMessageStatement = connection.prepareStatement(
                    "SELECT * FROM messages WHERE (sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?) " +
                            "ORDER BY createdat DESC LIMIT 1"
            );
            lastMessageStatement.setString(1, user);
            lastMessageStatement.setString(2, chatUser);
            lastMessageStatement.setString(3, chatUser);
            lastMessageStatement.setString(4, user);
            ResultSet lastMessageResult = lastMessageStatement.executeQuery();

            if (lastMessageResult.next()) {
                Message message = new Message();
                message.setId(lastMessageResult.getString("id"));
                message.setSender(lastMessageResult.getString("sender"));
                message.setReceiver(lastMessageResult.getString("receiver"));
                message.setText(lastMessageResult.getString("text"));
                message.setCreatedAt(lastMessageResult.getLong("createdat"));
                lastMessages.put(chatUser, message);
            }
        }
        return lastMessages;
    }
}
