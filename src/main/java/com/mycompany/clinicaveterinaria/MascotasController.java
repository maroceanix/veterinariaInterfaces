package com.mycompany.clinicaveterinaria;

import com.mycompany.clinicaveterinaria.model.Mascota;
import dao.MascotaDAO;
import dao.MascotaDAOImpl;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class MascotasController implements Initializable {

    @FXML private TableView<Mascota> tablaMascotas;
    @FXML private TableColumn<Mascota, Integer> colId;
    @FXML private TableColumn<Mascota, String>  colNombre;
    @FXML private TableColumn<Mascota, String>  colEspecie;
    @FXML private TableColumn<Mascota, Integer> colEdad;

    @FXML private TextField txtNombre, txtEspecie, txtEdad, txtBuscar;
    @FXML private Button btnAniadir, btnModificar, btnEliminar;

    private MascotaDAO dao = new MascotaDAOImpl();
    private ObservableList<Mascota> lista = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // SEGURIDAD: Si no es admin, ocultamos el botón eliminar
        if (Sesion.rolActual != null && !Sesion.rolActual.equals("admin")) {
            btnEliminar.setVisible(false);
            btnEliminar.setManaged(false); // Para que no ocupe espacio
        }

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEspecie.setCellValueFactory(new PropertyValueFactory<>("especie"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));

        cargarTabla();

        tablaMascotas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtNombre.setText(newVal.getNombre());
                txtEspecie.setText(newVal.getEspecie());
                txtEdad.setText(String.valueOf(newVal.getEdad()));
            }
        });
    }

    private void cargarTabla() {
        lista.setAll(dao.getAll());
        tablaMascotas.setItems(lista);
    }

    @FXML
    public void handleAniadir() {
        try {
            Mascota m = new Mascota();
            m.setNombre(txtNombre.getText());
            m.setEspecie(txtEspecie.getText());
            m.setEdad(Integer.parseInt(txtEdad.getText()));
            m.setIdPropietario(1); 
            dao.insertar(m);
            cargarTabla();
            limpiarFormulario();
        } catch (NumberFormatException e) {
            mostrarError("Error", "La edad debe ser un número.");
        }
    }

    @FXML
    public void handleEliminar() {
        Mascota sel = tablaMascotas.getSelectionModel().getSelectedItem();
        if (sel != null) {
            dao.eliminar(sel.getId());
            cargarTabla();
            limpiarFormulario();
        }
    }

    @FXML public void handleModificar() { /* ... igual que añadir pero con dao.actualizar ... */ }

    @FXML
    private Button btnCerrarSesion;

    @FXML
    public void handleCerrarSesion() {
        try {
            Sesion.cerrarSesion(); // Limpiamos los datos de la sesión estática
            FXMLLoader loader = new FXMLLoader(getClass().getResource("singInAdminVeterinario.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleBuscar() {
        String filtro = txtBuscar.getText().toLowerCase();
        ObservableList<Mascota> filtrada = lista.filtered(m -> 
            m.getNombre().toLowerCase().contains(filtro) || 
            m.getEspecie().toLowerCase().contains(filtro)
        );
        tablaMascotas.setItems(filtrada);
    }

    private void limpiarFormulario() {
        txtNombre.clear(); txtEspecie.clear(); txtEdad.clear();
    }

    private void mostrarError(String t, String m) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(t); a.setContentText(m); a.showAndWait();
    }
}