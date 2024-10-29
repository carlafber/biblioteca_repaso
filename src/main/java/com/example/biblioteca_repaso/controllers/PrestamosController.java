package com.example.biblioteca_repaso.controllers;

import com.example.biblioteca_repaso.BibliotecaApplication;
import com.example.biblioteca_repaso.CRUD.AutorCRUD;
import com.example.biblioteca_repaso.CRUD.LibroCRUD;
import com.example.biblioteca_repaso.CRUD.PrestamoCRUD;
import com.example.biblioteca_repaso.classes.Libro;
import com.example.biblioteca_repaso.classes.Usuario;
import com.example.biblioteca_repaso.util.Alerta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class PrestamosController implements Initializable {

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
    private Text id_nombreusuario;

    @FXML
    private TableColumn<?, ?> tc_fdevolucion;

    @FXML
    private TableColumn<?, ?> tc_fprestamo;

    @FXML
    private TableColumn<?, ?> tc_libro;

    @FXML
    private TableView<?> tv_prestamos;

    private Usuario usuario;

    private LibroCRUD libroCRUD;

    private AutorCRUD autorCRUD;

    private PrestamoCRUD prestamoCRUD;

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

    public void obtenerUsuario(Usuario usuario_login) {
        this.usuario = usuario_login;
        if (this.usuario != null) {
            id_nombreusuario.setText(usuario.getNombre());
        } else {
            id_nombreusuario.setText("Usuario no disponible");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        libroCRUD = new LibroCRUD();
        autorCRUD = new AutorCRUD();
        prestamoCRUD = new PrestamoCRUD();
        libroCRUD.LibroCRUD();
        autorCRUD.AutorCRUD();
        //prestamoCRUD.PrestamoCRUD();
    }
}
