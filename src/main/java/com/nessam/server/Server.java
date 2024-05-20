package com.nessam.server;

import com.nessam.server.config.Configuration;
import com.nessam.server.config.ConfigurationManager;

import com.nessam.server.handlers.httpHandlers.RequestHandler;
import com.nessam.server.handlers.modelHandlers.EducationHandler;
import com.nessam.server.handlers.modelHandlers.FollowHandler;
import com.nessam.server.handlers.modelHandlers.UserHandler;
import com.nessam.server.utils.BetterLogger;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Server {



    public static void main(String[] args) throws IOException {


        var log = Logger.getLogger("org.hibernate");
        log.setLevel(Level.OFF);

        ConfigurationManager.getInstance().loadConfigurationFile("src\\main\\resources\\http.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();
        BetterLogger.INFO("Using Port: " + conf.getPort());
        BetterLogger.INFO("Using WebRoot: " + conf.getWebroot());


        try {

            HttpServer server = HttpServer.create(new InetSocketAddress(conf.getPort()), 0);

            Files.createDirectories(Paths.get("src/main/java/com/nessam/server/assets"));
            server.createContext("/users", new UserHandler());

            server.createContext("/education", new EducationHandler());
            server.createContext("/req", new RequestHandler());
            server.createContext("/follows", new FollowHandler());
            server.setExecutor(null);
            server.start();


        } catch (
                IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

