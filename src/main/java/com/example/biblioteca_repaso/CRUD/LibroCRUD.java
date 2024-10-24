package com.example.biblioteca_repaso.CRUD;

import com.example.biblioteca_repaso.util.Alerta;
import com.example.biblioteca_repaso.util.Conectar;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class LibroCRUD {
    MongoClient con;
    MongoCollection<Document> collection = null;
    String json;
    Document doc;

    public void crearBD(){
        try {
            con = Conectar.conectar();

            MongoDatabase database = con.getDatabase("biblioteca");

            //creando una coleccion
            database.createCollection("libros");

            //Inserto un documento en la coleccion libros
            collection = database.getCollection("libros");


        } catch (Exception exception) {
            Alerta.mensajeError(exception.getClass().getName() + ": " + exception.getMessage());
        }
    }
}
