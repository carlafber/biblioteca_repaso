package com.example.biblioteca_repaso.controllers;

import com.example.biblioteca_repaso.BibliotecaApplication;
import com.example.biblioteca_repaso.CRUD.UsuarioCRUD;
import com.example.biblioteca_repaso.classes.Usuario;
import com.example.biblioteca_repaso.util.Alerta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignupController {

    @FXML
    private Button bt_cancelar;

    @FXML
    private Button bt_signup;

    @FXML
    private PasswordField pw_confcontrasena;

    @FXML
    private PasswordField pw_contrasena;

    @FXML
    private TextField txt_correo;

    @FXML
    private TextField txt_nombre;

    UsuarioCRUD usuarioCRUD;

    Usuario usuario;


    @FXML
    void OnCancelarClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BibliotecaApplication.class.getResource("inicio.fxml"));
            Parent root = fxmlLoader.load();
            InicioController controller = fxmlLoader.getController();

            Scene scene = new Scene(root);
            Stage stage = (Stage) bt_cancelar.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            Alerta.mensajeError(e.getMessage());
        }
    }

    @FXML
    void OnSignUpClick(ActionEvent event) {
        if (txt_correo.getText().isEmpty() || txt_nombre.getText().isEmpty() || pw_contrasena.getText().isEmpty() || pw_confcontrasena.getText().isEmpty()) {
            Alerta.mensajeError("Complete todos los campos.");
        } else {
            String nombre = txt_nombre.getText();
            String email = txt_correo.getText();
            if (!pw_contrasena.getText().equals(pw_confcontrasena.getText())) {
                Alerta.mensajeError("Las contraseñas no coinciden, inténtelo de nuevo.");
                pw_contrasena.clear();
                pw_confcontrasena.clear();
            } else {
                String contrasena = pw_contrasena.getText();
                usuario = new Usuario(nombre, email, contrasena);

                usuarioCRUD = new UsuarioCRUD();
                if (usuarioCRUD.existeUsuario(usuario.getEmail())) {
                    Alerta.mensajeError("Ya existe este usuario: " + usuario.getNombre() + ". Inicie sesión.");
                } else {
                    usuarioCRUD.insertarUsuario(usuario);
                    Alerta.mensajeInfo("ÉXITO", "Usuario registrado correctamente.");
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(BibliotecaApplication.class.getResource("principal.fxml"));
                        Parent root = fxmlLoader.load();
                        PrincipalController controller = fxmlLoader.getController();

                        Scene scene = new Scene(root);
                        Stage stage = (Stage) bt_signup.getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();

                    } catch (IOException e) {
                        Alerta.mensajeError(e.getMessage());
                    }
                }
            }
        }
    }
}