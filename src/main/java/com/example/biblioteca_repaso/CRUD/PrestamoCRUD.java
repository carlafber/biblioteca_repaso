package com.example.biblioteca_repaso.CRUD;

import com.example.biblioteca_repaso.classes.Prestamo;
import com.example.biblioteca_repaso.classes.Usuario;
import com.example.biblioteca_repaso.controllers.PrestamosController;
import com.example.biblioteca_repaso.util.Alerta;
import com.example.biblioteca_repaso.util.Conectar;
import com.example.biblioteca_repaso.util.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CountOptions;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class PrestamoCRUD {
    private MongoClient con;
    private MongoCollection<Document> collection = null;
    private MongoCollection<Document> usuariosCollection = null;
    private String json;
    private Document doc;
    private Document usuarioDoc;


    public void PrestamoCRUD(){
        //this.usuario_prestamo = usuario;
        try {
            con = Conectar.conectar();

            MongoDatabase database = con.getDatabase("biblioteca");

            //creando una coleccion
            database.createCollection("prestamos");

            //Inserto un documento en la coleccion prestamos
            collection = database.getCollection("prestamos");
            //usuariosCollection = database.getCollection("usuarios");

            if (collection.countDocuments() == 0) {
                insertarPrestamoPrueba();
            }


        } catch (Exception exception) {
            Alerta.mensajeError(exception.getClass().getName() + ": " + exception.getMessage());
        }
    }
    public boolean existePrestamo(String libro, boolean devuelto) {
        return collection.countDocuments(eq("libro", libro)) > 0 && collection.countDocuments(eq("devuelto", devuelto)) > 0;
    }

    public void insertarPrestamoPrueba() {
        // Crear el primer préstamo
        Prestamo prestamo1 = new Prestamo("El coronel no tiene quien le escriba",
                new Usuario("Carlos Pérez", "carlos.perez@example.com"),
                LocalDateTime.parse("2023-04-01 10:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse("2023-04-15 10:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                true);

        // Crear el segundo préstamo
        Prestamo prestamo2 = new Prestamo("Cien años de soledad",
                new Usuario("Ana Rodríguez", "ana.rodriguez@example.com"),
                LocalDateTime.parse("2023-05-05 15:30:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse("2023-05-19 15:30:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                false);

        insertarPrestamo(prestamo1);
        insertarPrestamo(prestamo2);
    }

    public boolean insertarPrestamo(Prestamo prestamo) {
        if (existePrestamo(prestamo.getLibro(), prestamo.isDevuelto())) {
            Alerta.mensajeError("Ya existe este préstamo: " + prestamo.getLibro());
            return false;
        } else {
            usuarioDoc = new Document("nombre", prestamo.getUsuario().getNombre())
                    .append("email", prestamo.getUsuario().getEmail());

            doc = new Document("libro", prestamo.getLibro())
                    .append("usuario", usuarioDoc)
                    .append("fecha_prestamo", prestamo.getFecha_prestamo_string())
                    .append("fecha_devolucion", prestamo.getFecha_devolucion_string())
                    .append("devuelto", prestamo.isDevuelto());

            collection.insertOne(doc);
            return true; // Devolver verdadero si la inserción fue exitosa
        }
    }

    public List<Prestamo> obtenerPrestamos(Usuario usuario) {
        List<Prestamo> prestamos = new ArrayList<>();

        for (Document doc : collection.find(eq("usuario.nombre", usuario.getNombre()))) {
            String libro = doc.getString("libro");
            LocalDateTime fecha_prestamo = LocalDateTime.parse(doc.getString("fecha_prestamo"), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
            LocalDateTime fecha_devolucion = LocalDateTime.parse(doc.getString("fecha_devolucion"), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
            boolean devuelto = doc.getBoolean("devuelto");

            Prestamo prestamo = new Prestamo(libro, usuario, fecha_prestamo, fecha_devolucion, devuelto);
            prestamos.add(prestamo);
        }

        return prestamos;
    }

    /*public void insertarPrestamoPrueba() {
        Prestamo prestamo1 = new Prestamo("El coronel no tiene quien le escriba",
                new Usuario("Carlos Pérez", "carlos.perez@example.com"),
                LocalDateTime.parse("2023-04-01 10:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse("2023-04-15 10:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                true);
        Prestamo prestamo2 = new Prestamo("Cien años de soledad",
                new Usuario("Ana Rodríguez", "ana.rodriguez@example.com"),
                LocalDateTime.parse("2023-05-05 15:30:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse("2023-05-19 15:30:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                false);

        insertarPrestamo(prestamo1);
        insertarPrestamo(prestamo2);
    }

    public boolean insertarPrestamo(Prestamo prestamo) {
        if (existePrestamo(prestamo.getLibro(), prestamo.isDevuelto())) {
            Alerta.mensajeError("Ya existe este préstamo: " + prestamo.getLibro());
            return false;
        } else {
            Document usuarioDoc = new Document("nombre", prestamo.getUsuario().getNombre())
                    .append("email", prestamo.getUsuario().getEmail());

            Document doc = new Document("libro", prestamo.getLibro())
                    .append("usuario", usuarioDoc)
                    .append("fecha_prestamo", prestamo.getFecha_prestamo().toString())
                    .append("fecha_devolucion", prestamo.getFecha_devolucion().toString())
                    .append("devuelto", prestamo.isDevuelto());

            collection.insertOne(doc);
            return true; // Devolver verdadero si la inserción fue exitosa
        }
    }


    public List<Prestamo> obtenerPrestamos(Usuario usuario) {
        List<Prestamo> prestamos = new ArrayList<>();

        for (Document doc : collection.find(eq("nombre", usuario.getNombre()))) {
            String libro = doc.getString("libro");
            LocalDateTime fecha_prestamo = LocalDateTime.parse(doc.getString("fecha_prestamo"));
            LocalDateTime fecha_devolucion = LocalDateTime.parse(doc.getString("fecha_devolucion"));
            boolean devuelto = doc.getBoolean("devuelto");

            Prestamo prestamo = new Prestamo(libro, usuario, fecha_prestamo, fecha_devolucion, devuelto);
            prestamos.add(prestamo);
        }

        return prestamos;
    }*/
}