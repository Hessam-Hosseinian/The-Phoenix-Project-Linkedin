package com.nessam.server.handlers.modelHandlers;

import com.nessam.server.controllers.MessageController;
import com.nessam.server.utils.BetterLogger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.sql.SQLException;

public class MessageHandler implements HttpHandler {

    MessageController messageController;

    public MessageHandler() throws SQLException {
        this.messageController = new MessageController();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] splittedPath = path.split("/");
        switch (method) {
            case "GET":
                if (splittedPath.length == 3) { //port:id/direct/person
                    try {
                        response = messageController.getNotify(splittedPath[2], 20);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if ("last".equals(splittedPath[3]) && splittedPath.length == 4) { //port:id/direct/person/last
                    try {
                        response = messageController.getLastMessagesWithUsers(splittedPath[2]);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (splittedPath.length == 4) { //port:id/direct/person1/person2
                    try {
                        response = messageController.getMessages(splittedPath[2], splittedPath[3]);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "POST": // ip:port/direct + body
                InputStream requestBody = exchange.getRequestBody();
                BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
                StringBuilder body = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
                requestBody.close();
                String newMessage = body.toString();

                JSONObject jsonObject = new JSONObject(newMessage);


                response = "Done!";
                try {
                    messageController.addMessage(jsonObject.getString("sender"), jsonObject.getString("receiver"), jsonObject.getString("text"));
                } catch (SQLException e) {
                    BetterLogger.ERROR("Failed to add message");
                }
                break;
        }
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
