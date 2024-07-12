package com.nessam.server;

import com.nessam.server.config.Configuration;
import com.nessam.server.config.ConfigurationManager;
import com.nessam.server.handlers.httpHandlers.FileDownloadHandler;
import com.nessam.server.handlers.httpHandlers.FileUploadHandler;
import com.nessam.server.handlers.httpHandlers.RequestHandler;
import com.nessam.server.handlers.modelHandlers.*;
import com.nessam.server.utils.BetterLogger;
import com.sun.net.httpserver.HttpServer;



import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;


public class Server {

    public static void main(String[] args) {


        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();

        BetterLogger.INFO("Using Port: " + conf.getPort());
        BetterLogger.INFO("Using WebRoot: " + conf.getWebroot());



        try {

            HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", conf.getPort()), 0);

            Files.createDirectories(Paths.get("src/main/java/com/nessam/server/assets"));
            server.createContext("/users", new UserHandler());
            server.createContext("/req", new RequestHandler());
            server.createContext("/education", new UserEducationHandler());
            server.createContext("/jobPosition", new JobPositionHandler());
            server.createContext("/follows", new FollowHandler());
            server.createContext("/message", new MessageHandler());
            server.createContext("/post", new PostHandler());
            server.createContext("/likes", new LikeHandler());
            server.createContext("/comment", new CommentHandler());
            server.createContext("/search", new SearchHandler());
            server.createContext("/upload", new FileUploadHandler());
            server.createContext("/download", new FileDownloadHandler());
            server.setExecutor(null);
            server.start();

        } catch (IOException e) {
            BetterLogger.ERROR(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

