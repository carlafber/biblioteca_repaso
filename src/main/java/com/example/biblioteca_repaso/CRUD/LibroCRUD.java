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
    private MongoClient con;
    private MongoCollection<Document> collection = null;
    private String json;
    private Document doc;

    public  LibroCRUD(){
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

    public boolean libro_prestado(String titulo) {
        PrestamoCRUD prestamoCRUD = new PrestamoCRUD();
        return prestamoCRUD.buscarPrestamoTitulo(titulo).isDevuelto();
    }

    public void modificarDisponibilidad(String titulo, boolean disponible) {
        collection.findOneAndUpdate(Filters.eq("titulo", titulo),new Document("$set", new Document("disponible", disponible)));
        //collection.updateOne(Filters.eq("titulo", titulo), new Document("$set", new Document("disponible", disponible)));
    }


    public void insetarLibroPrueba() {
        Libro[] libros = {
                new Libro("Cien años de soledad", "Gabriel García Márquez", 1967, false, "Realismo Mágico"),
                new Libro("1984", "George Orwell", 1949, true, "Distopía"),
                new Libro("El coronel no tiene quien le escriba", "Gabriel García Márquez", 1961, true, "Novela"),
                new Libro("Diez negritos", "Agatha Christie", 1939, true, "Policiaco"),
                new Libro("Asesinato en el Orient Express", "Agatha Christie", 1934, true, "Suspense"),
                new Libro("Días sin ti", "Elvira Sastre", 2019, true, "Novela")
        };

        for (Libro libro : libros) {
            insertarLibro(libro);
        }
    }


    public boolean insertarLibro(Libro libro) {
        if (existeLibro(libro)) {
            Alerta.mensajeError("Ya existe el libro: " + libro.getTitulo());
            return false;
        } else {
            /*if(libro_prestado(libro.getTitulo())) {
                libro.setDisponible(false);
            } else {
                libro.setDisponible(true);
            }*/
            Gson gson = new Gson();
            json = gson.toJson(libro);
            doc = Document.parse(json);
            collection.insertOne(doc);

            // Verificar si el coche fue insertado correctamente
            return existeLibro(libro);
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
            Libro libro = gson.fromJson(doc.toJson(), Libro.class);
            libros.add(libro);
        }

        return libros;
    }

    public List<String> obtenerNombreLibros() {
        List<String> nombres = new ArrayList<>();
        List<Libro> libros = obtenerLibros();
        for (Libro libro : libros) {
            if(libro.isDisponible()){
                nombres.add(libro.getTitulo());
            }
        }
        return nombres;
    }

    public void eliminarLibro(String titulo) {
        collection.deleteOne(new Document("titulo", titulo));
    }

    public void modificarLibro(Libro libro) {
        Document doc = new Document("autor", libro.getAutor())
                .append("ano_publicacion", libro.getAno_publicacion())
                .append("disponible", libro.isDisponible())
                .append("genero", libro.getGenero());

        collection.updateOne(Filters.eq("titulo", libro.getTitulo()), new Document("$set", doc));
    }
}
