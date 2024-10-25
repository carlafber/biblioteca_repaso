package com.example.biblioteca_repaso.CRUD;

import com.example.biblioteca_repaso.classes.Autor;
import com.example.biblioteca_repaso.util.Alerta;
import com.example.biblioteca_repaso.util.Conectar;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.time.LocalDate;
import java.util.Arrays;

public class AutorCRUD {
    MongoClient con;
    MongoCollection<Document> collection = null;
    String json;
    Document doc;

    public void crearBD(){
        try {
            con = Conectar.conectar();

            MongoDatabase database = con.getDatabase("biblioteca");

            //creando una coleccion
            database.createCollection("autores");

            //Inserto un documento en la coleccion autores
            collection = database.getCollection("autores");

            if (collection.countDocuments() == 0) {
                insertarAutorPrueba();
            }


        } catch (Exception exception) {
            Alerta.mensajeError(exception.getClass().getName() + ": " + exception.getMessage());
        }
    }

    public boolean existeAutor(String nombre) {
        long cont_autores = collection.countDocuments(Filters.eq("nombre", nombre));
        if(cont_autores > 0){
            return true;
        }
        return false;
    }

    public void insertarAutorPrueba() {
        Autor autor1 = new Autor("Gabriel García Márquez", "Colombiana", LocalDate.of(1927, 3, 6),  Arrays.asList("Realismo Mágico", "Novela"));
        Autor autor2 = new Autor("George Orwell", "Británica", LocalDate.of(1903, 6, 25),  Arrays.asList("Distopía", "Ensayo"));

        insertarAutor(autor1);
        insertarAutor(autor2);
    }

    public boolean insertarAutor(Autor autor) {
        if (existeAutor(autor.getNombre())) {
            Alerta.mensajeError("Ya existe este autor: " + autor.getNombre());
            return false;
        } else {
            Gson gson = new Gson();
            json = gson.toJson(autor);
            doc = Document.parse(json);
            collection.insertOne(doc);

            // Verificar si el coche fue insertado correctamente
            if (existeAutor(autor.getNombre())) {
                return true;
            } else {
                return false;
            }
        }
    }
}
