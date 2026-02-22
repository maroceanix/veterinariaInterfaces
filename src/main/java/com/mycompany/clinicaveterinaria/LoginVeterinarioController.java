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
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginVeterinarioController implements Initializable {

    @FXML private TextField usu;
    @FXML private PasswordField contra;
    // Asegúrate de poner fx:id="txtContraVisible" en el TextField del StackPane en Scene Builder
    @FXML private TextField txtContraVisible; 
    @FXML private CheckBox mostrarContra;
    @FXML private Button accederBtn;
    @FXML private Button entrarAdmin;

    private UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Lógica para mostrar/ocultar contraseña (sincronizada con el CheckBox)
        if (txtContraVisible != null) {
            txtContraVisible.managedProperty().bind(mostrarContra.selectedProperty());
            txtContraVisible.visibleProperty().bind(mostrarContra.selectedProperty());
            
            contra.managedProperty().bind(mostrarContra.selectedProperty().not());
            contra.visibleProperty().bind(mostrarContra.selectedProperty().not());

            // Sincroniza lo que escribes en uno con el otro
            txtContraVisible.textProperty().bindBidirectional(contra.textProperty());
        }
    }

    @FXML
    public void handleLogin() {
    String nombre = usu.getText().trim();
    String password = contra.getText().trim();

    if (nombre.isEmpty() || password.isEmpty()) {
        mostrarAlerta("Campos vacíos", "Debes rellenar todos los campos.", Alert.AlertType.WARNING);
        return;
    }

    Usuario user = usuarioDAO.getByNombreYPassword(nombre, password);

    if (user != null && "veterinario".equals(user.getRol())) {
        Sesion.usuarioActual = user.getNombre();
        Sesion.rolActual = user.getRol();
        irAMascotas();
    } 
    // Si el usuario existe pero es Admin, le avisamos
    else if (user != null && "admin".equals(user.getRol())) {
        mostrarAlerta("Acceso Denegado", "Esta cuenta es de Administrador. Usa el login correspondiente.", Alert.AlertType.ERROR);
    }
    // Si no existe o la contraseña está mal
    else {
        mostrarAlerta("Acceso Fallido", "Usuario o contraseña no encontrados en el sistema.", Alert.AlertType.ERROR);
    }
}
    private void irAMascotas() {
        try {
            // Carga el panel principal de gestión
            FXMLLoader loader = new FXMLLoader(getClass().getResource("panelMascotas.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) accederBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Clínica Veterinaria - Panel Veterinario: " + Sesion.usuarioActual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void irAAdminLogin() {
        try {
            // Vuelve al FXML de Admin
            Parent root = FXMLLoader.load(getClass().getResource("singInAdminVeterinario.fxml"));
            Stage stage = (Stage) entrarAdmin.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}