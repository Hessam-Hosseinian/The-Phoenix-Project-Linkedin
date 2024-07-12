package com.nessam.server.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nessam.server.dataAccess.ConnectDAO;
import com.nessam.server.models.Connect;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.sql.ResultSet;

public class ConnectController {

    private final ConnectDAO connectDAO;
    public ConnectController() throws SQLException {
        connectDAO = new ConnectDAO();
    }




    public void saveConnect(String connecter, String connected) throws SQLException {
        Connect connect = new Connect(connecter, connected);
        connectDAO.saveConnect(connect);
    }


    public void deleteConnect(String connecter, String connected) throws SQLException {
        Connect connect = new Connect(connecter, connected);
        connectDAO.deleteConnect(connect);
    }

    public void deleteAllConnects() throws SQLException {
        connectDAO.deleteAllConnects();
    }

    public String getPerson2s(String userId) throws SQLException, JsonProcessingException {
        List<Connect> connects = connectDAO.getPerson2s(userId);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(connects);
    }

    public String getPerson1s(String userId) throws SQLException, JsonProcessingException {
        List<Connect> connects = connectDAO.getPerson1s(userId);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(connects);
    }

    public String getAllConnect() throws SQLException, JsonProcessingException {
        List<Connect> connects = connectDAO.getAllConnect();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(connects);
    }
}
