package com.example.biblioteca_repaso.CRUD;

import com.example.biblioteca_repaso.classes.Autor;
import com.example.biblioteca_repaso.util.Alerta;
import com.example.biblioteca_repaso.util.Conectar;
import com.example.biblioteca_repaso.util.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutorCRUD {
    private MongoClient con;
    private MongoCollection<Document> collection = null;
    private String json;
    private Document doc;

    public void AutorCRUD() {
        try {
            con = Conectar.conectar();

            MongoDatabase database = con.getDatabase("biblioteca");

            // creando una colección
            database.createCollection("autores");

            // Inserto un documento en la colección autores
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
        return cont_autores > 0;
    }

    public void insertarAutorPrueba() {
        Autor autor1 = new Autor("Gabriel García Márquez", "Colombiana", LocalDate.of(1927, 3, 6), Arrays.asList("Realismo Mágico", "Novela"));
        Autor autor2 = new Autor("George Orwell", "Británica", LocalDate.of(1903, 6, 25), Arrays.asList("Distopía", "Ensayo"));

        insertarAutor(autor1);
        insertarAutor(autor2);
    }

    public boolean insertarAutor(Autor autor) {
        if (existeAutor(autor.getNombre())) {
            Alerta.mensajeError("Ya existe este autor: " + autor.getNombre());
            return false;
        } else {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();

            Document doc = new Document("nombre", autor.getNombre())
                    .append("nacionalidad", autor.getNacionalidad())
                    .append("fecha_nacimiento", autor.getFecha_nacimiento_string())
                    .append("generos", autor.getGeneros());

            collection.insertOne(doc);
            return existeAutor(autor.getNombre());
        }
    }

    public List<Autor> obtenerAutores() {
        List<Autor> autores = new ArrayList<>();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        for (Document doc : collection.find()) {
            String nombre = doc.getString("nombre");
            String nacionalidad = doc.getString("nacionalidad");
            LocalDate fecha_nacimiento = gson.fromJson(doc.get("fecha_nacimiento").toString(), LocalDate.class);
            List<String> generos = (List<String>) doc.get("generos");
            autores.add(new Autor(nombre, nacionalidad, fecha_nacimiento, generos));
        }
        return autores;
    }

    public List<String> obtenerNombreAutores() {
        List<String> nombres = new ArrayList<>();
        List<Autor> autores = obtenerAutores();
        for (Autor autor : autores) {
            nombres.add(autor.getNombre());
        }
        return nombres;
    }

    public List<String> obtenerGenerosAutor(String nombreAutor) {
        List<String> generos = new ArrayList<>();
        List<Autor> autores = obtenerAutores();  // Metodo que obtiene la lista de autores

        for (Autor autor : autores) {
            if (autor.getNombre().equalsIgnoreCase(nombreAutor)) {
                generos.addAll(autor.getGeneros());
                break; // Detener el bucle una vez que se encuentra el autor
            }
        }
        return generos;
    }


    public void eliminarAutor(String nombre) {
        collection.deleteOne(new Document("nombre", nombre));
    }

    public void modificarAutor(Autor autor) {
        Document doc = new Document("nacionalidad", autor.getNacionalidad())
                .append("fecha_nacimiento", autor.getFecha_nacimiento_string())
                .append("generos", autor.getGeneros());

        collection.updateOne(Filters.eq("nombre", autor.getNombre()), new Document("$set", doc));
    }
}
