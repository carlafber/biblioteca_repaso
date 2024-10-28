package com.example.biblioteca_repaso.controllers;

import com.example.biblioteca_repaso.classes.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class PrestamosController {
//prestamos de -----
//quitar email usuario
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
    private ComboBox<?> cb_libros;

    @FXML
    private DatePicker dt_fdevolucion;

    @FXML
    private DatePicker dt_fprestamo;

    @FXML
    private TableColumn<?, ?> tc_fdevolucion;

    @FXML
    private TableColumn<?, ?> tc_fprestamo;

    @FXML
    private TableColumn<?, ?> tc_libro;

    @FXML
    private TableColumn<?, ?> tc_usuario;

    @FXML
    private TableView<?> tv_prestamos;

    @FXML
    private TextField txt_email;

    Usuario usuario;

    @FXML
    void OnActualizarClick(ActionEvent event) {

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
    void OnPrestamoClick(MouseEvent event) {

    }

    @FXML
    void OnVolverClick(ActionEvent event) {

    }

    public void obtenerUsuario(Usuario usuario_login) {
        usuario = usuario_login;
    }

}
