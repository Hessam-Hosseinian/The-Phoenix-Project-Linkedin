package com.nessam.server.handlers.httpHandlers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.nessam.server.controllers.UserController;
import com.nessam.server.utils.JwtAuth;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

public class AuthHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        UserController userController = null;
        try {
            userController = new UserController();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = null;
        String[] splittedPath = path.split("/");
        switch (method) {
            case "GET":
                String tmpUserEmail = splittedPath[splittedPath.length - 2];
                String tmpUserPass = splittedPath[splittedPath.length - 1];
                String res = null;
                try {
                    res = userController.getUserByEmailAndPass(tmpUserEmail, tmpUserPass);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                if (res == null) {
                    response = "incorrect userID or password";
                } else {
                    Headers headers = exchange.getResponseHeaders();
                    headers.add("JWT", tmpUserEmail + "!" + JwtAuth.jws(tmpUserEmail));
                    response = "logged in successfully";
                }
                break;
            default:
                break;
        }
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream outs = exchange.getResponseBody();
        outs.write(response.getBytes());
        outs.close();

    }


}