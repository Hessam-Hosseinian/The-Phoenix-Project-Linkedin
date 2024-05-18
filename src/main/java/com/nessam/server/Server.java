package com.nessam.server;

import com.nessam.server.config.Configuration;
import com.nessam.server.config.ConfigurationManager;
import com.nessam.server.handlers.httpHandlers.AuthHandler;
import com.nessam.server.handlers.modelHandlers.UserHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;


public class Server {
    private final static Logger LOGGER = Logger.getLogger(String.valueOf(Server.class));

    public static void main(String[] args) {
        LOGGER.info("Server starting...");





        ConfigurationManager.getInstance().loadConfigurationFile("C:\\Users\\as\\Desktop\\The Phoenix Project 11.0\\src\\main\\resources\\http.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();

        LOGGER.info("Using Port: " + conf.getPort());
        LOGGER.info("Using WebRoot: " + conf.getWebroot());
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(conf.getPort()), 0);

            Files.createDirectories(Paths.get("src/main/java/com/nessam/server/assets"));
            server.createContext("/users", new UserHandler());
            server.createContext("/auth", new AuthHandler());
            server.setExecutor(null);
            server.start();


        } catch (
                IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}

