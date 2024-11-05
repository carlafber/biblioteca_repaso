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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class PrestamoCRUD {
    private MongoClient con;
    private MongoCollection<Document> collection = null;
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

            if (collection.countDocuments() == 0) {
                insertarPrestamoPrueba();
            }


        } catch (Exception exception) {
            Alerta.mensajeError(exception.getClass().getName() + ": " + exception.getMessage());
        }
    }

    /*public boolean existePrestamo(String libro, boolean devuelto) {
        return collection.countDocuments(eq("libro", libro)) > 0 && collection.countDocuments(eq("devuelto", devuelto)) > 0;
    }*/

    public void insertarPrestamoPrueba() {
        Prestamo[] prestamos = {
                new Prestamo("El coronel no tiene quien le escriba",
                        new Usuario("Carlos Pérez", "carlos.perez@example.com"),
                        LocalDate.parse("2024-04-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        LocalDate.parse("2024-04-15", DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        true),

                new Prestamo("Cien años de soledad",
                        new Usuario("Ana Rodríguez", "ana.rodriguez@example.com"),
                        LocalDate.parse("2024-11-05", DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        LocalDate.parse("2024-12-19", DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        false),

                new Prestamo("Diez negritos",
                        new Usuario("Ana Rodríguez", "ana.rdgz@email.com"),
                        LocalDate.parse("2024-09-30", DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        LocalDate.parse("2024-11-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        true)
        };

        for (Prestamo prestamo : prestamos) {
            insertarPrestamo(prestamo);
        }
    }

    public boolean insertarPrestamo(Prestamo prestamo) {
        usuarioDoc = new Document("nombre", prestamo.getUsuario().getNombre())
                .append("email", prestamo.getUsuario().getEmail());

        doc = new Document("libro", prestamo.getLibro())
                .append("usuario", usuarioDoc)
                .append("fecha_prestamo", prestamo.getFecha_prestamo_string())
                .append("fecha_devolucion", prestamo.getFecha_devolucion_string())
                .append("devuelto", prestamo.isDevuelto());

        collection.insertOne(doc);

        LibroCRUD libroCRUD = new LibroCRUD();
        libroCRUD.modificarDisponibilidad(prestamo.getLibro(), prestamo.isDevuelto());
        return true;
    }

    public List<Prestamo> obtenerPrestamos(Usuario usuario) {
        List<Prestamo> prestamos = new ArrayList<>();

        for (Document doc : collection.find(eq("usuario.nombre", usuario.getNombre()))) {
            String libro = doc.getString("libro");
            LocalDate fecha_prestamo = LocalDate.parse(doc.getString("fecha_prestamo"), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            LocalDate fecha_devolucion = LocalDate.parse(doc.getString("fecha_devolucion"), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            boolean devuelto = doc.getBoolean("devuelto");

            Prestamo prestamo = new Prestamo(libro, usuario, fecha_prestamo, fecha_devolucion, devuelto);
            prestamos.add(prestamo);
        }

        return prestamos;
    }

    public Prestamo buscarPrestamoTitulo(String titulo) {
        Document doc = collection.find(eq("libro", titulo)).first();

        if (doc != null) {
            Usuario usuario = new Usuario(doc.get("usuario.nombre").toString(), doc.get("usuario.email").toString());
            LocalDate fecha_prestamo = LocalDate.parse(doc.getString("fecha_prestamo"), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            LocalDate fecha_devolucion = LocalDate.parse(doc.getString("fecha_devolucion"), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            boolean devuelto = doc.getBoolean("devuelto");

            return new Prestamo(titulo, usuario, fecha_prestamo, fecha_devolucion, devuelto);
        }
        return null;
    }

    public void modificarPrestamo(Prestamo prestamo) {
        /*Document doc = new Document("nacionalidad", autor.getNacionalidad())
                .append("fecha_nacimiento", autor.getFecha_nacimiento_string())
                .append("generos", autor.getGeneros());

        collection.updateOne(Filters.eq("nombre", autor.getNombre()), new Document("$set", doc));*/
    }
}