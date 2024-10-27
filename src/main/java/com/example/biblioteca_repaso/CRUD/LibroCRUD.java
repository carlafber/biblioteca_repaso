package com.example.biblioteca_repaso.CRUD;

import com.example.biblioteca_repaso.classes.Libro;
import com.example.biblioteca_repaso.util.Alerta;
import com.example.biblioteca_repaso.util.Conectar;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

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

            if (collection.countDocuments() == 0) {
                insetarLibroPrueba();
            }


        } catch (Exception exception) {
            Alerta.mensajeError(exception.getClass().getName() + ": " + exception.getMessage());
        }
    }

    public void insetarLibroPrueba() {
        Libro libro1 = new Libro("Cien años de soledad", "Gabriel García Márquez", 1967, true, "Realismo Mágico");
        Libro libro2 = new Libro("1984", "George Orwell", 1949, true, "Distopía");
        Libro libro3 = new Libro("El coronel no tiene quien le escriba", "Gabriel García Márquez", 1961, false, "Novela");

        insertarLibro(libro1);
        insertarLibro(libro2);
        insertarLibro(libro3);

    }


    public boolean insertarLibro(Libro libro) {
        if (existeLibro(libro)) {
            Alerta.mensajeError("Ya existe el libro: " + libro.getTitulo());
            return false;
        } else {
            Gson gson = new Gson();
            json = gson.toJson(libro);
            doc = Document.parse(json);
            collection.insertOne(doc);

            // Verificar si el coche fue insertado correctamente
            if (existeLibro(libro)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean existeLibro(Libro libro) {
        Document doc = collection.find(Filters.eq("titulo", libro.getTitulo())).first();
        return doc != null;
    }


    public List<Libro> obtenerLibrosBiblioteca(){
        List<Libro> libros = new ArrayList<>();
        Gson gson = new Gson();
        try {
            // Utiliza projection para obtener solo los campos necesarios
            for (Document doc : collection.find().projection(Projections.fields(
                    Projections.include("titulo", "autor", "disponible")))) {

                String titulo = doc.getString("titulo");
                String autor = doc.getString("autor");
                boolean disponible = doc.getBoolean("disponible");

                // Agregar libro a la lista
                libros.add(new Libro(titulo, autor, disponible));
            }
        } catch (Exception e) {
            Alerta.mensajeError("Error al obtener libros: " + e.getMessage());
        }

        return libros;
    }

    public List<Libro> obtenerLibros(){
        List<Libro> libros = new ArrayList<>();
        Gson gson = new Gson();

        for (Document doc : collection.find()) {
            System.out.println("Documento recuperado: " + doc.toJson()); // Depuración

            Libro libro = gson.fromJson(doc.toJson(), Libro.class);
            libros.add(libro);
        }

        return libros;
    }
}
