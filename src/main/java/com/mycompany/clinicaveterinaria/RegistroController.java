package com.mycompany.clinicaveterinaria;


import com.mycompany.clinicaveterinaria.model.Usuario;
import dao.UsuarioDAO;
import dao.UsuarioDAOImpl;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistroController implements Initializable {

    @FXML private TextField usu;
    @FXML private TextField nombre;
    @FXML private TextField contra;
    @FXML private ChoiceBox<String> elegirRol;
    @FXML private Button registrarme;

    private UsuarioDAO dao = new UsuarioDAOImpl();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        elegirRol.getItems().addAll("admin", "veterinario");
        elegirRol.setValue("veterinario"); // valor por defecto
    }

    @FXML
    public void handleRegistrar() {
        String usuario = usu.getText().trim();
        String nombreCompleto = nombre.getText().trim();
        String password = contra.getText().trim();
        String rol = elegirRol.getValue();

        // Validaciones
        if (usuario.isEmpty() || password.isEmpty() || nombreCompleto.isEmpty()) {
            mostrarError("Campos vacíos", "Rellena todos los campos.");
            return;
        }
        if (rol == null) {
            mostrarError("Rol no seleccionado", "Selecciona un rol.");
            return;
        }

        Usuario u = new Usuario();
        u.setNombre(usuario);
        u.setPassword(password);
        u.setRol(rol);

        dao.insertar(u);

        mostrarInfo("Registro exitoso", "Usuario creado correctamente.");
        cambiarPantalla("singInVeterinario");
    }

    private void cambiarPantalla(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/mycompany/clinicaveterinaria/" + fxml + ".fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) registrarme.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarError(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void mostrarInfo(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}