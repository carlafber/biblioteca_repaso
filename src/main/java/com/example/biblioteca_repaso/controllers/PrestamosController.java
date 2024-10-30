package com.example.biblioteca_repaso.controllers;

import com.example.biblioteca_repaso.BibliotecaApplication;
import com.example.biblioteca_repaso.CRUD.AutorCRUD;
import com.example.biblioteca_repaso.CRUD.LibroCRUD;
import com.example.biblioteca_repaso.CRUD.PrestamoCRUD;
import com.example.biblioteca_repaso.classes.Prestamo;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
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
    private ComboBox<String> cb_libros;

    @FXML
    private DatePicker dt_fdevolucion;

    @FXML
    private DatePicker dt_fprestamo;

    @FXML
    private Text id_nombreusuario;

    @FXML
    private TableColumn<Prestamo, String> tc_fdevolucion;

    @FXML
    private TableColumn<Prestamo, String> tc_fprestamo;

    @FXML
    private TableColumn<Prestamo, String> tc_libro;

    @FXML
    private TableView<Prestamo> tv_prestamos;

    private Usuario usuario;

    private LibroCRUD libroCRUD;

    private AutorCRUD autorCRUD;

    private PrestamoCRUD prestamoCRUD;

    List<Prestamo> prestamos;

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
            controller.obtenerUsuario(usuario);

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
            cargarPrestamos(usuario);
        } else {
            id_nombreusuario.setText("Usuario no disponible");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarBD();

        tc_libro.setCellValueFactory(new PropertyValueFactory<>("libro"));
        tc_fprestamo.setCellValueFactory(cellData -> {Prestamo prestamo = cellData.getValue();
            return new SimpleStringProperty(prestamo.getFecha_prestamo_string());
        });
        tc_fdevolucion.setCellValueFactory(cellData -> {Prestamo prestamo = cellData.getValue();
            return new SimpleStringProperty(prestamo.getFecha_devolucion_string());
        });

        tv_prestamos.setOnMouseClicked(this::OnPrestamoClick);
    }

    public void cargarBD(){
        autorCRUD = new AutorCRUD();
        libroCRUD = new LibroCRUD();
        prestamoCRUD = new PrestamoCRUD();
        autorCRUD.AutorCRUD();
        libroCRUD.LibroCRUD();
        prestamoCRUD.PrestamoCRUD();
    }

    public void cargarPrestamos(Usuario usuario){
        prestamos = prestamoCRUD.obtenerPrestamos(usuario);
        tv_prestamos.getItems().setAll(prestamos);
        System.out.println(prestamos.toString());
    }

    //QUITAR HORA, SOLO DÍA
}
