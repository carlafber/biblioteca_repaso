package com.example.biblioteca_repaso.controllers;

import com.example.biblioteca_repaso.BibliotecaApplication;
import com.example.biblioteca_repaso.CRUD.LibroCRUD;
import com.example.biblioteca_repaso.classes.Autor;
import com.example.biblioteca_repaso.classes.Libro;
import com.example.biblioteca_repaso.classes.Usuario;
import com.example.biblioteca_repaso.util.Alerta;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PrincipalController implements Initializable {

    @FXML
    private Button bt_autores;

    @FXML
    private Button bt_libros;

    @FXML
    private Button bt_prestamos;

    @FXML
    private Button bt_salir;

    @FXML
    private TableColumn<Autor, String> tc_autor;

    @FXML
    private TableColumn<Libro, String> tc_estado;

    @FXML
    private TableColumn<Libro, String> tc_titulo;

    @FXML
    private TableView<Libro> tv_biblioteca;

    private LibroCRUD libroCRUD;

    List<Libro> libros;

    private Usuario usuario;

    public void obtenerUsuario(Usuario usuario_login) {
        this.usuario = usuario_login;
    }

    @FXML
    void OnAutoresClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BibliotecaApplication.class.getResource("autores.fxml"));
            Parent root = fxmlLoader.load();
            AutoresController controller = fxmlLoader.getController();
            controller.obtenerUsuario(usuario);

            Scene scene = new Scene(root);
            Stage stage = (Stage) bt_autores.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Alerta.mensajeError(e.getMessage());
        }
    }

    @FXML
    void OnCerrarSesionClick(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(BibliotecaApplication.class.getResource("inicio.fxml"));
            Parent root = fxmlLoader.load();
            InicioController controller = fxmlLoader.getController();

            Scene scene = new Scene(root);
            Stage stage = (Stage) bt_salir.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            Alerta.mensajeError(e.getMessage());
        }
    }

    @FXML
    void OnLibrosClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BibliotecaApplication.class.getResource("prestamos.fxml"));
            Parent root = fxmlLoader.load();
            LibrosController controller = fxmlLoader.getController();
            controller.obtenerUsuario(usuario);

            Scene scene = new Scene(root);
            Stage stage = (Stage) bt_libros.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Alerta.mensajeError(e.getMessage());
        }
    }

    @FXML
    void OnPrestamosClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BibliotecaApplication.class.getResource("prestamos.fxml"));
            Parent root = fxmlLoader.load();
            PrestamosController controller = fxmlLoader.getController();
            controller.obtenerUsuario(usuario);

            Scene scene = new Scene(root);
            Stage stage = (Stage) bt_prestamos.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Alerta.mensajeError(e.getMessage());
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        libroCRUD = new LibroCRUD();
        libroCRUD.LibroCRUD();
        tc_titulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tc_autor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        tc_estado.setCellValueFactory(cellData -> {Libro libro = cellData.getValue();
            return new SimpleStringProperty(libro.isDisponible() ? "Disponible" : "No disponible");
        });

        cargarLibros();
    }

    public void cargarLibros(){
        libros = libroCRUD.obtenerLibrosBiblioteca();
        tv_biblioteca.getItems().setAll(libros);
    }


}
