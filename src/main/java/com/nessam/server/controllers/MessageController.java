package com.nessam.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nessam.server.dataAccess.MessageDAO;
import com.nessam.server.models.Message;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class MessageController {
    private final MessageDAO messageDAO;

    public MessageController() throws SQLException {
        messageDAO = new MessageDAO();

    }

    public void addMessage( String sender, String receiver, String text) throws SQLException {

        messageDAO.saveMessage(new Message( sender, receiver, text));

    }

    public String getMessages(String u1, String u2) throws SQLException, JsonProcessingException {
        ArrayList<Message> messages = messageDAO.getMessages(u1, u2);
        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(messages);
        return response;
    }

    public void deleteMessage(String id) throws SQLException {
        messageDAO.deleteMessage(id);
    }

    public void deleteAll() throws SQLException {
        messageDAO.deleteAll();
    }

    public String getNotify(String receiver, int cnt) throws SQLException, JsonProcessingException {
        ArrayList<Message> messages = messageDAO.getNotify(receiver);
//		Collections.sort(messages);
        cnt = Integer.min(cnt, messages.size());
        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(messages.subList(messages.size() - cnt, messages.size()));
        return response;
    }
    public String getLastMessagesWithUsers(String user) throws SQLException {
        JSONArray lastMessagesArray = new JSONArray();
        Map<String, Message> lastMessages = messageDAO.getLastMessagesWithUsers(user);
        for (Map.Entry<String, Message> entry : lastMessages.entrySet()) {
            JSONObject messageJSON = new JSONObject();
            messageJSON.put("chatUser", entry.getKey());
            Message message = entry.getValue();
            messageJSON.put("id", message.getId());
            messageJSON.put("sender", message.getSender());
            messageJSON.put("receiver", message.getReceiver());
            messageJSON.put("text", message.getText());
            messageJSON.put("createdAt", message.getCreatedAt());
            lastMessagesArray.put(messageJSON);
        }
        return lastMessagesArray.toString();
    }
}