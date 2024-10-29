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

public class LoginController {

    @FXML
    private Button bt_cancelar;

    @FXML
    private Button bt_login;

    @FXML
    private PasswordField pw_contrasena;

    @FXML
    private TextField txt_usuario;

    UsuarioCRUD usuarioCRUD;

    Usuario usuario;

    @FXML
    void OnCancelarClick(ActionEvent event) {
        try{
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
    void OnLogInClick(ActionEvent event) {
        if(txt_usuario.getText().isEmpty() || pw_contrasena.getText().isEmpty()){
            Alerta.mensajeError("Complete todos los campos.");
        } else {
            String email = txt_usuario.getText();
            String contrasena = pw_contrasena.getText();

            usuarioCRUD = new UsuarioCRUD();
            if(!usuarioCRUD.existeUsuario(email)){
                Alerta.mensajeError("Este usuario no existe, regístrese.");
                txt_usuario.clear();
                pw_contrasena.clear();
            } else {
                usuario = usuarioCRUD.obtenerUsuario(email);
                if(usuarioCRUD.validarContrasena(email, contrasena)){
                    Alerta.mensajeInfo("ÉXITO", "Sesión iniciada correctamente.");
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(BibliotecaApplication.class.getResource("principal.fxml"));
                        Parent root = fxmlLoader.load();
                        PrincipalController controller = fxmlLoader.getController();
                        controller.obtenerUsuario(usuario);

                        Scene scene = new Scene(root);
                        Stage stage = (Stage) bt_login.getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();

                    } catch (IOException e) {
                        Alerta.mensajeError(e.getMessage());
                    }
                } else {
                    Alerta.mensajeError("Contraseña incorrecta.");
                    pw_contrasena.clear();
                }
            }
        }
    }
}