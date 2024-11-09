/*package com.example.biblioteca_repaso.CRUD;

import com.example.biblioteca_repaso.classes.Prestamo;
import com.example.biblioteca_repaso.classes.Usuario;
import com.example.biblioteca_repaso.util.Alerta;
import com.example.biblioteca_repaso.util.Conectar;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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

    public boolean existePrestamo(Prestamo prestamo) {
        boolean existe = false;
        if (prestamo.getId() != null){
            long cont_doc = collection.countDocuments(Filters.and(eq("_id", prestamo.getId()),eq("devuelto", prestamo.isDevuelto())));
            if(cont_doc > 0){
                existe = true;
            }
        }
        return existe;
    }

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
        if(existePrestamo(prestamo)){
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

            LibroCRUD libroCRUD = new LibroCRUD();
            libroCRUD.modificarDisponibilidad(prestamo.getLibro(), prestamo.isDevuelto());
            prestamo.setId(doc.getObjectId("_id"));
            return true;
        }
    }

    public List<Prestamo> obtenerPrestamosUsuaurio(Usuario usuario) {
        List<Prestamo> prestamos = new ArrayList<>();

        for (Document doc : collection.find(eq("usuario.nombre", usuario.getNombre()))) {
            String libro = doc.getString("libro");
            LocalDate fecha_prestamo = LocalDate.parse(doc.getString("fecha_prestamo"), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            LocalDate fecha_devolucion = LocalDate.parse(doc.getString("fecha_devolucion"), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            boolean devuelto = doc.getBoolean("devuelto");

            Prestamo prestamo = new Prestamo(doc.getObjectId("_id"), libro, usuario, fecha_prestamo, fecha_devolucion, devuelto);
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

            return new Prestamo(doc.getObjectId("_id"), titulo, usuario, fecha_prestamo, fecha_devolucion, devuelto);
        }
        return null;
    }

    public void modificarPrestamo(Prestamo prestamo) {
        if(!existePrestamo(prestamo)){
            usuarioDoc = new Document("nombre", prestamo.getUsuario().getNombre())
                    .append("email", prestamo.getUsuario().getEmail());

            doc = new Document("usuario", usuarioDoc)
                    .append("fecha_prestamo", prestamo.getFecha_prestamo_string())
                    .append("fecha_devolucion", prestamo.getFecha_devolucion_string())
                    .append("devuelto", prestamo.isDevuelto());

            collection.updateOne(eq("_id", prestamo.getId()), new Document("$set", doc));


            LibroCRUD libroCRUD = new LibroCRUD();
            libroCRUD.modificarDisponibilidad(prestamo.getLibro(), prestamo.isDevuelto());
        } else {
            Alerta.mensajeError("Ya existe un préstamo activo para el libro: " + prestamo.getLibro());
        }
    }
}
*/

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
import org.bson.types.ObjectId;

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

    public void insertarPrestamoPrueba() {
        Prestamo[] prestamos = {
                new Prestamo("El coronel no tiene quien le escriba",
                        new Usuario("Carlos Pérez", "carlos.p@email.com"),
                        LocalDate.parse("2024-04-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        LocalDate.parse("2024-04-15", DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        true),

                new Prestamo("Cien años de soledad",
                        new Usuario("Ana Rodríguez", "ana.rdgz@email.com"),
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

    public boolean existePrestamo(Prestamo prestamo) {
        boolean existe = false;
        if (prestamo.getId() != null){
            long cont_doc = collection.countDocuments(Filters.and(eq("_id", prestamo.getId()),eq("devuelto", prestamo.isDevuelto())));
            if(cont_doc > 0){
                existe = true;
            }
        }
        return existe;
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
            ObjectId id = doc.getObjectId("_id");
            String libro = doc.getString("libro");
            LocalDate fecha_prestamo = LocalDate.parse(doc.getString("fecha_prestamo"), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            LocalDate fecha_devolucion = LocalDate.parse(doc.getString("fecha_devolucion"), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            boolean devuelto = doc.getBoolean("devuelto");

            Prestamo prestamo = new Prestamo(id, libro, usuario, fecha_prestamo, fecha_devolucion, devuelto);
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

    //¡¡¡¡¡¡¡¡¡¡¡¡
    public void modificarPrestamo(Prestamo prestamo) {
        usuarioDoc = new Document("nombre", prestamo.getUsuario().getNombre())
                .append("email", prestamo.getUsuario().getEmail());

        doc = new Document("libro", prestamo.getLibro())
                .append("usuario", usuarioDoc)
                .append("fecha_prestamo", prestamo.getFecha_prestamo_string())
                .append("fecha_devolucion", prestamo.getFecha_devolucion_string())
                .append("devuelto", prestamo.isDevuelto());

        System.out.println("Modificando préstamo con ID: " + prestamo.getId());
        System.out.println("Campos a actualizar: " + doc.toJson());

        collection.updateOne(eq("_id", prestamo.getId()), new Document("$set", doc));


        LibroCRUD libroCRUD = new LibroCRUD();
        libroCRUD.modificarDisponibilidad(prestamo.getLibro(), prestamo.isDevuelto());
    }
}
