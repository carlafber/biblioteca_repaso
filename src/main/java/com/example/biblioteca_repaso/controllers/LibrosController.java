package com.example.biblioteca_repaso.controllers;

import com.example.biblioteca_repaso.BibliotecaApplication;
import com.example.biblioteca_repaso.util.Alerta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LibrosController {

    @FXML
    private Button bt_actualizar;

    @FXML
    private Button bt_eliminar;

    @FXML
    private Button bt_nuevo;

    @FXML
    private Button bt_volver;

    @FXML
    private ComboBox<?> cb_autores;

    @FXML
    private ComboBox<?> cb_generos;

    @FXML
    private TableColumn<?, ?> tc_ano;

    @FXML
    private TableColumn<?, ?> tc_autor;

    @FXML
    private TableColumn<?, ?> tc_estado;

    @FXML
    private TableColumn<?, ?> tc_genero;

    @FXML
    private TableColumn<?, ?> tc_titulo;

    @FXML
    private TableView<?> tv_biblioteca;

    @FXML
    private TextField txt_ano;

    @FXML
    private TextField txt_titulo;

    @FXML
    void OnActualizarClick(ActionEvent event) {

    }

    @FXML
    void OnEliminarClick(ActionEvent event) {

    }

    @FXML
    void OnNuevoClick(ActionEvent event) {

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

}
