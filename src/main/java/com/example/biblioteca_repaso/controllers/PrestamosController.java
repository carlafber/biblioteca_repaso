package com.example.biblioteca_repaso.controllers;

import com.example.biblioteca_repaso.BibliotecaApplication;
import com.example.biblioteca_repaso.CRUD.AutorCRUD;
import com.example.biblioteca_repaso.CRUD.LibroCRUD;
import com.example.biblioteca_repaso.CRUD.PrestamoCRUD;
import com.example.biblioteca_repaso.classes.Autor;
import com.example.biblioteca_repaso.classes.Prestamo;
import com.example.biblioteca_repaso.classes.Usuario;
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

    private Prestamo prestamo_seleccionado;

    @FXML
    void OnActualizarClick(ActionEvent event) {
        if(prestamo_seleccionado == null){
            Alerta.mensajeError("Seleccione un préstamo de la tabla para poder modificarlo.");
        } else {
            prestamo_seleccionado.setLibro(cb_libros.getValue());
            prestamo_seleccionado.setUsuario(usuario);
            prestamo_seleccionado.setFecha_prestamo(dt_fprestamo.getValue());
            prestamo_seleccionado.setFecha_devolucion(dt_fdevolucion.getValue());
            if(prestamo_seleccionado.getFecha_devolucion().isBefore(LocalDate.now())){
                prestamo_seleccionado.setDevuelto(true);
            } else {
                prestamo_seleccionado.setDevuelto(false);
            }
            prestamoCRUD.modificarPrestamo(prestamo_seleccionado);
            cargarPrestamos(usuario);
            Alerta.mensajeInfo("ÉXITO", "Préstamo modificado correctamente.");
        }
    }

    @FXML
    void OnCancelarClick(ActionEvent event) {
        limpiarCampos();
    }

    @FXML
    void OnEliminarClick(ActionEvent event) {

    }

    @FXML
    void OnNuevoClick(ActionEvent event) {
        if(cb_libros.getValue() == null || dt_fprestamo.getValue() == null ||dt_fdevolucion.getValue() == null){
            Alerta.mensajeError("Complete todos los campos, por favor.");
        } else {
            boolean devuelto = dt_fdevolucion.getValue().isBefore(LocalDate.now());
            Prestamo prestamo_nuevo = new Prestamo(cb_libros.getValue(), usuario, dt_fprestamo.getValue(), dt_fdevolucion.getValue(), devuelto);
            if (prestamoCRUD.insertarPrestamo(prestamo_nuevo)) {
                System.out.println(prestamo_nuevo);
                cargarPrestamos(usuario);
                cargarLibros();
                Alerta.mensajeInfo("ÉXITO", "Préstamo insertado correctamente.");
                limpiarCampos();
            }
        }
    }

    @FXML
    void OnPrestamoClick(MouseEvent event) {
        prestamo_seleccionado = tv_prestamos.getSelectionModel().getSelectedItem();
        if (prestamo_seleccionado != null) {
            dt_fprestamo.setValue(prestamo_seleccionado.getFecha_prestamo());
            dt_fdevolucion.setValue(prestamo_seleccionado.getFecha_devolucion());
            cb_libros.setValue(prestamo_seleccionado.getLibro());
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

        cargarLibros();

        tv_prestamos.setOnMouseClicked(this::OnPrestamoClick);
    }

    public void cargarBD(){
        autorCRUD = new AutorCRUD();
        libroCRUD = new LibroCRUD();
        prestamoCRUD = new PrestamoCRUD();
        autorCRUD.AutorCRUD();
        prestamoCRUD.PrestamoCRUD();
    }

    public void cargarPrestamos(Usuario usuario){
        prestamos = prestamoCRUD.obtenerPrestamos(usuario);
        tv_prestamos.getItems().setAll(prestamos);
        if(prestamos.isEmpty()){
            Alerta.mensajeInfo("INFO","Este usuario no ha realizado ningún préstamo.");
        }
    }

    public void cargarLibros(){
        List<String> libros = libroCRUD.obtenerNombreLibros();
        cb_libros.setItems(FXCollections.observableArrayList(libros));
    }

    public void limpiarCampos(){
        cb_libros.setValue(null);
        dt_fprestamo.setValue(null);
        dt_fdevolucion.setValue(null);
    }
}