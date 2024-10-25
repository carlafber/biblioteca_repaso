package com.example.biblioteca_repaso.CRUD;

import com.example.biblioteca_repaso.util.Alerta;
import com.example.biblioteca_repaso.util.Conectar;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

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

    public void insertarAutorPrueba() {
        /*Coche coche1 = new Coche("6666HHH", "Renault", "Clio", "Deportivo");
        Coche coche2 = new Coche("5555BCD", "Ford", "SMax", "Familiar");

        insertarCoche(coche1);
        insertarCoche(coche2);*/
    }
}
