/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import poiupv.FXMLDocumentController;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.User;

/**
 * FXML Controller class
 *
 * @author dario
 */
public class FXMLPerfilController implements Initializable {
    
    private String avatarPath;
    private boolean validEmail;
    private boolean validPassword;
    
    private FXMLDocumentController mainController;

    
    @FXML
    private ImageView fotoPerfil;
    @FXML
    private Button imageButton;
    @FXML
    private TextField usuario;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private DatePicker fecha;
    @FXML
    private Label errorFecha;
    @FXML
    private Button guardarCambios;
    @FXML
    private Label emailError;
    @FXML
    private Label passwordError;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        fecha.valueProperty().addListener((obs, oldVal, newVal) -> {
            checkAge();
        });
        
        email.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // Cuando pierde el foco
                validEmail = User.checkEmail(email.getText());
            if (!validEmail && !email.getText().equals("")) {
                emailError.setVisible(true);
            } else {
                emailError.setVisible(false);
            }
        }
        });
        
        password.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                validPassword = User.checkPassword(password.getText());
            if (!validPassword && !password.getText().equals("")) {
                passwordError.setVisible(true);
            } else {
                passwordError.setVisible(false);
            }
        }
        });
    }    

    @FXML
    private void imageSelect(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Avatar");
        
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
            "Archivos de imagen", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);
        
        Stage stage = (Stage) imageButton.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        
        if (file != null) {
            try {
                avatarPath = file.toURI().toString();
                // Cargar y mostrar la imagen
                Image image = new Image(avatarPath);
                fotoPerfil.setImage(image);
                
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", 
                    "No se pudo cargar la imagen: " + e.getMessage());
            }
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void setDatos(String nombre, String email, String password, LocalDate fecha, Image image) {
        usuario.setText(nombre);
        this.email.setText(email);
        this.password.setText(password);
        this.fecha.setValue(fecha);
        fotoPerfil.setImage(image);
    }

    @FXML
    private void handleGuardar(ActionEvent event) {
        if(User.checkPassword(password.getText()) && User.checkEmail(email.getText()) && checkAge()){
            User user = FXMLLoginController.getCurrentUser();
            user.setAvatar(fotoPerfil.getImage());
            user.setEmail(email.getText());
            user.setPassword(password.getText());
            user.setBirthdate(fecha.getValue());
            mainController.perfilImage.setImage(fotoPerfil.getImage());
            showAlert(Alert.AlertType.INFORMATION, "Cambios guardados", "Su información personal ha sido actualizada");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Revise que los campos sean correctos");
        }
    }
    
    private boolean checkAge() {
        if (fecha.getValue() == null) {
            errorFecha.setVisible(false);
            return false;
        }

        LocalDate selectedDate = fecha.getValue();
        LocalDate minDate = LocalDate.now().minusYears(16);

        if (selectedDate.isAfter(minDate)) {
            errorFecha.setVisible(true);
            return false;
        } else {
            errorFecha.setVisible(false);
            return true;
        }
    }
    
    public void setMainController(FXMLDocumentController controller) {
        this.mainController = controller;
    }
    
}
