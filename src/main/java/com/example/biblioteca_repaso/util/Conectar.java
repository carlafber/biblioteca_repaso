package com.example.biblioteca_repaso.util;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Conectar {
    public static MongoClient conectar(){
        try{
            Properties configuration = new Properties();
            configuration.load(new FileInputStream(new File("src/main/resources/configuration/database.properties")));

            String username = configuration.getProperty("username");
            String password = configuration.getProperty("password");
            String host = configuration.getProperty("host");
            String port = configuration.getProperty("port");
            String author = configuration.getProperty("author");

            final MongoClient conexion = new MongoClient(new MongoClientURI("mongodb://" + username + ":" + password + "@" + host + ":" + port + "/?authSource=" + author));
            return conexion;
        } catch (Exception e) {
            Alerta.mensajeError("Conexión Fallida\n" + e.getMessage());
            return null;
        }
    }
}
