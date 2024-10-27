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
    MongoClient con;
    MongoCollection<Document> collection = null;
    String json;
    Document doc;

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
        Usuario usuario = new Usuario("pepe@email.com", "1234");
        insertarUsuario(usuario);
    }


    public boolean insertarUsuario(Usuario usuario) {
        if (existeUsuario(usuario)) {
            Alerta.mensajeError("Ya existe este usuario: " + usuario.getNombre() + ". Inicie sesión.");
            return false;
        } else {
            String contrasena_encriptada = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt());
            Gson gson = new Gson();
            json = gson.toJson(usuario);
            doc = new Document("email", usuario.getEmail()).append("contraseña", contrasena_encriptada);
            collection.insertOne(doc);

            // Verificar si el coche fue insertado correctamente
            if (existeUsuario(usuario)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean existeUsuario(Usuario usuario) {
        Document doc = collection.find(Filters.eq("email", usuario.getEmail())).first();
        if (doc != null) {
            String contrasena_existente = doc.getString("contraseña");
            // Comparar la contraseña proporcionada con la almacenada
            return BCrypt.checkpw(usuario.getContrasena(), contrasena_existente);
        }
        return false; // Usuario no encontrado
    }
}
