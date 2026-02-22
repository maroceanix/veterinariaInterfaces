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

public class LoginAdminController implements Initializable { // Cambiar a LoginVeterinarioController en el otro archivo

    @FXML private TextField usu;
    @FXML private PasswordField contra;
    // IMPORTANTE: En Scene Builder, añade un TextField con fx:id="txtContraVisible" 
    // encima del PasswordField dentro del StackPane
    @FXML private TextField txtContraVisible; 
    @FXML private CheckBox mostrarContra;
    @FXML private Button accederBtn;
    @FXML private Button entrarVeterinario; // O entrarAdmin

    private UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Lógica para el CheckBox de mostrar contraseña
        if (txtContraVisible != null) {
            txtContraVisible.managedProperty().bind(mostrarContra.selectedProperty());
            txtContraVisible.visibleProperty().bind(mostrarContra.selectedProperty());
            contra.managedProperty().bind(mostrarContra.selectedProperty().not());
            contra.visibleProperty().bind(mostrarContra.selectedProperty().not());
            // Sincroniza ambos campos
            txtContraVisible.textProperty().bindBidirectional(contra.textProperty());
        }
    }
   @FXML
    public void handleLogin() {
    String nombre = usu.getText().trim();
    String password = contra.getText().trim();

    // 1. Validar que no estén vacíos antes de ir a la BD
    if (nombre.isEmpty() || password.isEmpty()) {
        mostrarAlerta("Campos obligatorios", "Por favor, introduce usuario y contraseña.", Alert.AlertType.WARNING);
        return;
    }

    // 2. Intentar obtener el usuario del DAO
    Usuario user = usuarioDAO.getByNombreYPassword(nombre, password);

    // 3. Si el usuario existe y es admin, entramos
    if (user != null && "admin".equals(user.getRol())) {
        Sesion.usuarioActual = user.getNombre();
        Sesion.rolActual = user.getRol();
        irAMascotas();
    } 
    // 4. Si el usuario no existe (credenciales mal) saltamos el ALERT
    else {
        mostrarAlerta("Error de Autenticación", 
                      "Usuario o contraseña incorrectos. Verifica tus credenciales.", 
                      Alert.AlertType.ERROR);
    }
}
    private void irAMascotas() {
        try {
            // Asegúrate de que el nombre del FXML sea exacto
            FXMLLoader loader = new FXMLLoader(getClass().getResource("panelMascotas.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) accederBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestión de Mascotas - " + Sesion.usuarioActual);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error cargando panelMascotas.fxml. Revisa la ruta.");
        }
    }

    @FXML
    public void irAVeterinarioLogin() { // O irAAdminLogin
        try {
            String fxml = "singInVeterinario.fxml"; // Cambiar según corresponda
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) entrarVeterinario.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}