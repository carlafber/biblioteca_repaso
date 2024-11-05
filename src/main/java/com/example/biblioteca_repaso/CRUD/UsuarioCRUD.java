package com.example.biblioteca_repaso.CRUD;

import com.example.biblioteca_repaso.classes.Usuario;
import com.example.biblioteca_repaso.util.Alerta;
import com.example.biblioteca_repaso.util.Conectar;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;


public class UsuarioCRUD {
    private MongoClient con;
    private MongoCollection<Document> collection = null;
    private String json;
    private Document doc;

    public UsuarioCRUD() {
        try {
            con = Conectar.conectar();

            MongoDatabase database = con.getDatabase("biblioteca");

            //creando una coleccion
            database.createCollection("usuarios");

            //Inserto un documento en la coleccion autores
            collection = database.getCollection("usuarios");

            if (collection.countDocuments() == 0) {
                insertarUsuariosPrueba();
            }

        } catch (Exception exception) {
            Alerta.mensajeError(exception.getClass().getName() + ": " + exception.getMessage());
        }
    }

    public void insertarUsuariosPrueba() {
        Usuario usuario1 = new Usuario("Carlos Pérez", "carlos.p@email.com", "1234");
        Usuario usuario2 = new Usuario("Ana Rodríguez", "ana.rdgz@email.com", "Ana1999");

        insertarUsuario(usuario1);
        insertarUsuario(usuario2);
    }


    public boolean insertarUsuario(Usuario usuario) {
        if (existeUsuario(usuario.getEmail())) {
            Alerta.mensajeError("Ya existe este usuario: " + usuario.getNombre() + ". Inicie sesión.");
            return false;
        } else {
            String contrasena_encriptada = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt());
            Gson gson = new Gson();
            json = gson.toJson(usuario);
            doc = new Document("nombre", usuario.getNombre()).append("email", usuario.getEmail()).append("contraseña", contrasena_encriptada);
            collection.insertOne(doc);

            // Verificar si el coche fue insertado correctamente
            if (existeUsuario(usuario.getEmail())) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean existeUsuario(String email) {
        Document doc = collection.find(Filters.eq("email", email)).first();
        return doc != null;
    }

    public boolean validarContrasena(String email, String contrasena_proporcionada) {
        Document doc = collection.find(Filters.eq("email", email)).first();
        if (doc != null) {
            String contrasen_existente = doc.getString("contraseña");
            return BCrypt.checkpw(contrasena_proporcionada, contrasen_existente);
        }
        return false;
    }

    public Usuario obtenerUsuario(String email) {
        Document doc = collection.find(Filters.eq("email", email)).first();
        if (doc != null) {
            // Asumiendo que tu clase Usuario tiene un constructor que acepta email, contraseña y nombre
            return new Usuario(doc.getString("nombre"), doc.getString("email"), null);
        }
        return null; // Usuario no encontrado
    }
}
