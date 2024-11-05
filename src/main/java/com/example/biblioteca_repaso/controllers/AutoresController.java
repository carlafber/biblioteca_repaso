package com.example.biblioteca_repaso.controllers;

import com.example.biblioteca_repaso.BibliotecaApplication;
import com.example.biblioteca_repaso.CRUD.AutorCRUD;
import com.example.biblioteca_repaso.CRUD.LibroCRUD;
import com.example.biblioteca_repaso.CRUD.PrestamoCRUD;
import com.example.biblioteca_repaso.classes.Autor;
import com.example.biblioteca_repaso.classes.Usuario;
import com.example.biblioteca_repaso.util.Alerta;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class AutoresController implements Initializable {
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
    private DatePicker dt_fecha;

    @FXML
    private TableColumn<Autor, String> tc_fecha;

    @FXML
    private TableColumn<Autor, String> tc_nombre;

    @FXML
    private TableColumn<Autor, String> tc_generos;

    @FXML
    private TableColumn<Autor, String> tc_nacionalidad;

    @FXML
    private TableView<Autor> tv_autores;

    @FXML
    private TextField txt_generos;

    @FXML
    private TextField txt_nacionalidad;

    @FXML
    private TextField txt_nombre;

    private AutorCRUD autorCRUD;

    private LibroCRUD libroCRUD;

    private PrestamoCRUD prestamoCRUD;

    List<Autor> autores;

    private Autor autor_seleccionado;

    private Usuario usuario;

    public void obtenerUsuario(Usuario usuario_login) {
        this.usuario = usuario_login;
    }


    @FXML
    void OnActualizarClick(ActionEvent event) {
        if (autor_seleccionado == null) {
            Alerta.mensajeError("Seleccione un autor de la tabla para poder modificarlo.");
        } else {
            autor_seleccionado.setNacionalidad(txt_nacionalidad.getText());
            autor_seleccionado.setFecha_nacimiento(dt_fecha.getValue());
            autor_seleccionado.setGeneros(new ArrayList<>(Arrays.asList(txt_generos.getText().split(", "))));
            autorCRUD.modificarAutor(autor_seleccionado);

            cargarAutores();
            Alerta.mensajeInfo("ÉXITO", "Autor modificado correctamente.");
        }
    }

    @FXML
    void OnAutorClick(MouseEvent event) {
        autor_seleccionado = tv_autores.getSelectionModel().getSelectedItem();
        if (autor_seleccionado != null) {
            txt_nombre.setText(autor_seleccionado.getNombre());
            txt_nacionalidad.setText(autor_seleccionado.getNacionalidad());
            dt_fecha.setValue(autor_seleccionado.getFecha_nacimiento());
            txt_generos.setText(String.join(", ", autor_seleccionado.getGeneros()));
        }
    }


    @FXML
    void OnCancelarClick(ActionEvent event) {
        limpiarCampos();
    }


    @FXML
    void OnEliminarClick(ActionEvent event) {
        if(autor_seleccionado == null){
            Alerta.mensajeError("Seleccione un autor para poder eliminarlo.");
        } else {
            autorCRUD.eliminarAutor(autor_seleccionado.getNombre());
            cargarAutores();
            Alerta.mensajeInfo("ÉXITO", "Autor eliminado correctamente.");
            limpiarCampos();
        }
    }

    @FXML
    void OnNuevoClick(ActionEvent event) {
        if (txt_nombre.getText().isEmpty() || txt_nacionalidad.getText().isEmpty() || txt_generos.getText().isEmpty() || dt_fecha.getValue() == null) {
            Alerta.mensajeError("Complete todos los campos, por favor.");
        } else {
            String genero_seleccionado = txt_generos.getText();
            List<String> generos = Arrays.asList(genero_seleccionado.split(", "));
            Autor autor_nuevo = new Autor(txt_nombre.getText(), txt_nacionalidad.getText(), dt_fecha.getValue(), generos);
            if (autorCRUD.insertarAutor(autor_nuevo)) {
                cargarAutores();
                Alerta.mensajeInfo("ÉXITO", "Autor insertado correctamente.");
                limpiarCampos();
            }
        }
    }

    @FXML
    void OnVolverClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BibliotecaApplication.class.getResource("principal.fxml"));
            Parent root = fxmlLoader.load();
            PrincipalController controller = fxmlLoader.getController();
            controller.obtenerUsuario(usuario);

            Scene scene = new Scene(root);
            Stage stage = (Stage) bt_volver.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Alerta.mensajeError(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarBD();

        tc_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tc_nacionalidad.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));
        tc_fecha.setCellValueFactory(cellData -> {Autor autor = cellData.getValue();
            return new SimpleStringProperty(autor.getFecha_nacimiento_string());
        });
        tc_generos.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenerosString()));

        cargarAutores();

        tv_autores.setOnMouseClicked(this::OnAutorClick);

    }

    public void cargarBD(){
        autorCRUD = new AutorCRUD();
        libroCRUD = new LibroCRUD();
        prestamoCRUD = new PrestamoCRUD();
        autorCRUD.AutorCRUD();
        prestamoCRUD.PrestamoCRUD();
    }

    public void cargarAutores(){
        autores = autorCRUD.obtenerAutores();
        tv_autores.getItems().setAll(autores);
    }

    public void limpiarCampos(){
        txt_nombre.clear();
        txt_nacionalidad.clear();
        dt_fecha.setValue(null);
        txt_generos.clear();
    }
}
