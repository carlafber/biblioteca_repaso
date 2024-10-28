package com.example.biblioteca_repaso.controllers;

import com.example.biblioteca_repaso.BibliotecaApplication;
import com.example.biblioteca_repaso.CRUD.AutorCRUD;
import com.example.biblioteca_repaso.CRUD.LibroCRUD;
import com.example.biblioteca_repaso.classes.Libro;
import com.example.biblioteca_repaso.util.Alerta;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

//BT_CANCELAR

public class LibrosController implements Initializable {

    @FXML
    private Button bt_actualizar;

    @FXML
    private Button bt_cancelar;

    @FXML
    private Button bt_eliminar;

    @FXML
    private Button bt_nuevo;

    @FXML
    private Button bt_volver;

    @FXML
    private ComboBox<String> cb_autores;

    @FXML
    private ComboBox<String> cb_generos;

    @FXML
    private TableColumn<Libro, Integer> tc_ano;

    @FXML
    private TableColumn<Libro, String> tc_autor;

    @FXML
    private TableColumn<Libro, String> tc_estado;

    @FXML
    private TableColumn<Libro, String> tc_genero;

    @FXML
    private TableColumn<Libro, String> tc_titulo;

    @FXML
    private TableView<Libro> tv_libros;

    @FXML
    private TextField txt_ano;

    @FXML
    private TextField txt_titulo;

    List<Libro> libros;

    private LibroCRUD libroCRUD;

    private Libro libro_seleccionado;

    private AutorCRUD autorCRUD;

    @FXML
    void OnActualizarClick(ActionEvent event) {
        if (libro_seleccionado == null) {
            Alerta.mensajeError("Seleccione un coche de la tabla para poder modificarlo.");
        } else {
            libro_seleccionado.setAutor(cb_autores.getValue());
            libro_seleccionado.setAno_publicacion(Integer.parseInt(txt_ano.getText()));
            libro_seleccionado.setDisponible(true);
            libro_seleccionado.setGenero(cb_generos.getValue());

            libroCRUD.modificarLibro(libro_seleccionado);

            cargarLibros();
            Alerta.mensajeInfo("ÉXITO", "Coche modificado correctamente.");
        }
    }

    @FXML
    void OnCancelarClick(ActionEvent event) {
        limpiarCampos();
    }

    @FXML
    void OnEliminarClick(ActionEvent event) {
        if(libro_seleccionado == null){
            Alerta.mensajeError("Seleccione un libro para poder eliminarlo.");
        } else {
            libroCRUD.eliminarLibro(libro_seleccionado.getTitulo());
            cargarLibros();
            Alerta.mensajeInfo("ÉXITO", "Libro eliminado correctamente.");
            limpiarCampos();
        }
    }

    @FXML
    void OnGenerosClick(MouseEvent event) {
        actualizarGeneros();
    }


    @FXML
    void OnNuevoClick(ActionEvent event) {
        if(txt_ano.getText().isEmpty() || txt_titulo.getText().isEmpty() || cb_autores.getValue() == null || cb_generos.getValue() == null){
            Alerta.mensajeError("Complete todos los campos, por favor.");
        } else {
            Libro libro_nuevo = new Libro(txt_titulo.getText(), cb_autores.getValue(), Integer.parseInt(txt_ano.getText()), true, cb_generos.getValue());
            if(libroCRUD.insertarLibro(libro_nuevo)){
                cargarLibros();
                Alerta.mensajeInfo("ÉXITO", "Libro insertado correctamente.");
                limpiarCampos();
            }
            txt_titulo.clear();
        }
    }

    @FXML
    void OnVolverClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BibliotecaApplication.class.getResource("principal.fxml"));
            Parent root = fxmlLoader.load();
            PrincipalController controller = fxmlLoader.getController();

            Scene scene = new Scene(root);
            Stage stage = (Stage) bt_volver.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Alerta.mensajeError(e.getMessage());
        }
    }

    @FXML
    void OnLibroClick(MouseEvent event) {
        libro_seleccionado = tv_libros.getSelectionModel().getSelectedItem();
        if (libro_seleccionado != null) {
            txt_titulo.setText(libro_seleccionado.getTitulo());
            cb_autores.setValue(libro_seleccionado.getAutor());
            txt_ano.setText(String.valueOf(libro_seleccionado.getAno_publicacion()));
            cb_generos.setValue(libro_seleccionado.getGenero());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        libroCRUD = new LibroCRUD();
        autorCRUD = new AutorCRUD();
        libroCRUD.LibroCRUD();
        autorCRUD.AutorCRUD();

        List<String> autores = autorCRUD.obtenerNombreAutores();
        cb_autores.setItems(FXCollections.observableArrayList(autores));

        tc_titulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tc_autor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        tc_ano.setCellValueFactory(new PropertyValueFactory<>("ano_publicacion"));
        tc_genero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        tc_estado.setCellValueFactory(cellData -> {Libro libro = cellData.getValue();
            return new SimpleStringProperty(libro.isDisponible() ? "Disponible" : "No disponible");
        });

        cargarLibros();

        tv_libros.setOnMouseClicked(this::OnLibroClick);
    }

    public void actualizarGeneros() {
        String autor_seleccionado = cb_autores.getValue();
        if (autor_seleccionado != null) {
            List<String> generos = autorCRUD.obtenerGenerosAutor(autor_seleccionado);
            cb_generos.setItems(FXCollections.observableArrayList(generos));
        } else {
            cb_generos.getItems().clear();
        }
    }

    public void cargarLibros(){
        libros = libroCRUD.obtenerLibros();
        tv_libros.getItems().setAll(libros);
    }

    public void limpiarCampos(){
        cb_autores.setValue(null);
        cb_generos.setValue(null);
        txt_ano.clear();
        txt_titulo.clear();
    }
}
