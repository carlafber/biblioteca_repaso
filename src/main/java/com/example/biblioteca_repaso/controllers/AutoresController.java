package com.example.biblioteca_repaso.controllers;

import com.example.biblioteca_repaso.BibliotecaApplication;
import com.example.biblioteca_repaso.CRUD.AutorCRUD;
import com.example.biblioteca_repaso.util.Alerta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
    private TableColumn<?, ?> tc_ano;

    @FXML
    private TableColumn<?, ?> tc_autor;

    @FXML
    private TableColumn<?, ?> tc_genero;

    @FXML
    private TableColumn<?, ?> tc_titulo;

    @FXML
    private TableView<?> tv_biblioteca;

    @FXML
    private TextField txt_generos;

    @FXML
    private TextField txt_nacionalidad;

    @FXML
    private TextField txt_nombre;

    AutorCRUD autorCRUD;

    @FXML
    void OnActualizarClick(ActionEvent event) {

    }

    @FXML
    void OnAutorClick(MouseEvent event) {

    }


    @FXML
    void OnCancelarClick(ActionEvent event) {

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        autorCRUD = new AutorCRUD();
        autorCRUD.AutorCRUD();

        /*tc_matricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        tc_marca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        tc_modelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        tc_tipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        cargarCoches();

        tv_coches.setOnMouseClicked(this::OnCocheClick);*/
    }

    /*public void cargarCoches(){
        coches = cocheCRUD.obtenerCoches();
        tv_coches.getItems().setAll(coches);
    }*/
}
