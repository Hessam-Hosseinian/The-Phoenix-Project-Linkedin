package com.nessam.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nessam.server.dataAccess.MessageDAO;
import com.nessam.server.models.Message;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class MessageController {

    private final MessageDAO messageDAO;
    private final ObjectMapper objectMapper;

    public MessageController() throws SQLException {
        messageDAO = new MessageDAO();
        objectMapper = new ObjectMapper();
    }

    public void addMessage(String id, String sender, String receiver, String text) throws SQLException {
        messageDAO.saveMessage(new Message(id, sender, receiver, text));
    }

    public String getMessages(String u1, String u2) throws SQLException, JsonProcessingException {
        ArrayList<Message> messages = messageDAO.getMessages(u1, u2);
        return objectMapper.writeValueAsString(messages);
    }

    public void deleteMessage(String text) throws SQLException {
        messageDAO.deleteMessage(text);
    }

    public void deleteAll() throws SQLException {
        messageDAO.deleteAll();
    }

    public String getMessagesInDirect(String receiver, int cnt) throws SQLException, JsonProcessingException {
        ArrayList<Message> messages = messageDAO.getMessagesInDirect(receiver);
//		Collections.sort(messages);
        cnt = Integer.min(cnt, messages.size());
        String response = objectMapper.writeValueAsString(messages.subList(messages.size() - cnt, messages.size()));
        return response;
    }
    //this is a test comment
}
