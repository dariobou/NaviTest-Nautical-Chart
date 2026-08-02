/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.NavDAOException;
import model.Navigation;
import model.User;

/**
 * FXML Controller class
 *
 * @author dario
 */
public class FXMLRegisterController implements Initializable {

    private boolean validEmail;
    private boolean validPassword;
    private boolean validUser;
    
    private ChangeListener<String> listenerEmail;
    private ChangeListener<String> listenerPassword;
    private ChangeListener<String> listenerPassword2;
    
    private String avatarPath;
    
    @FXML
    private Label emailError;
    @FXML
    private Label errorFecha;
    @FXML
    private TextField email1;
    @FXML
    private TextField password1;
    @FXML
    private TextField password2;
    @FXML
    private Label passwordError1;
    @FXML
    private Label passwordError2;
    @FXML
    private DatePicker date;
    @FXML
    private Button registerButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField usuario;
    @FXML
    private ImageView avatar;
    @FXML
    private Button imageButton;
    @FXML
    private Label errorUsuario;
    @FXML
    private Label errorCampos;
    @FXML
    private Button infoUser;
    @FXML
    private Button infoPassword;

    private void checkAge() {
        if (date.getValue() == null) {
            errorFecha.setVisible(false);
            return;
        }

        LocalDate selectedDate = date.getValue();
        LocalDate minDate = LocalDate.now().minusYears(16);

        if (selectedDate.isAfter(minDate)) {
            errorFecha.setVisible(true);
        } else {
            errorFecha.setVisible(false);
        }
    }
    
    public boolean isValidAge() {
        checkAge();
        return !errorFecha.isVisible();
    }
    
    private boolean checkPasswordsMatch() {
        boolean match = password1.getText().equals(password2.getText());
    
        if (!match && !password2.getText().isEmpty()) {
            passwordError2.setVisible(true);
        } else {
            passwordError2.setVisible(false);
        }
        return match;
    }
    
    @FXML
    private void cerrarRegister(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    //=========================================================
    // you must initialize here all related with the object 
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        
        email1.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // Cuando pierde el foco
                validEmail = User.checkEmail(email1.getText());
            if (!validEmail && !email1.getText().equals("")) {
                emailError.setVisible(true);
                emailError.setText("Email no válido");
            } else {
                emailError.setVisible(false);
            }
        }
        });
        
        password1.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                validPassword = User.checkPassword(password1.getText());
            if (!validPassword && !password1.getText().equals("")) {
                passwordError1.setVisible(true);
            } else {
                passwordError1.setVisible(false);
            }
        }
        });
        
        password1.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!password2.getText().isEmpty() && !password2.isFocused()) {
                checkPasswordsMatch();
            }
        });
        
        password2.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                checkPasswordsMatch();
            }
        });
        
        date.valueProperty().addListener((obs, oldVal, newVal) -> {
            checkAge();
        });
        
        usuario.focusedProperty().addListener((obs, oldVal, newVal) -> {
        if (!newVal) {
            try {
                validUser = User.checkNickName(usuario.getText());
                if (!validUser && !usuario.getText().equals("")) {
                    errorUsuario.setVisible(true);
                    errorUsuario.setText("Usuario no válido");
                } else if(validUser && Navigation.getInstance().exitsNickName(usuario.getText())){
                    errorUsuario.setVisible(true);
                    errorUsuario.setText("Usuario ya en uso");
                } else {
                    errorUsuario.setVisible(false);
                }
            } catch (NavDAOException ex) {
                System.out.println(ex);
            }
        }
    });
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        try{
            if (isValidAge() && checkPasswordsMatch() && validEmail && validPassword) {
                Navigation navigation = Navigation.getInstance();
                navigation.registerUser(usuario.getText(), email1.getText(), password1.getText(), avatar.getImage(), date.getValue());
            
                showAlert(Alert.AlertType.INFORMATION, "Registro exitoso", "Usuario creado correctamente");
                Stage stage = (Stage) usuario.getScene().getWindow();
                stage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error de validación", "Por favor corrige los campos marcados o vacíos");
            }
        } catch(NavDAOException n){ System.out.println(n); }
    }
    
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    private void imageSelect(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Avatar");
        
        // Filtros para tipos de imagen
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
            "Archivos de imagen", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);
        
        // Mostrar diálogo de selección
        Stage stage = (Stage) imageButton.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        
        if (file != null) {
            try {
                avatarPath = file.toURI().toString();
                // Cargar y mostrar la imagen
                Image image = new Image(avatarPath);
                avatar.setImage(image);
                
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", 
                    "No se pudo cargar la imagen: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleInfoUser(ActionEvent event) {
        showAlert(AlertType.INFORMATION, "Requisitos del nombre de usuario", "El usuario debe contener entre 6 y 15 caracteres o dígitos sin espacios, pudiendo usar guiones o sub-guiones");
    }

    @FXML
    private void handleInfoPassword(ActionEvent event) {
        showAlert(AlertType.INFORMATION, "Requisitos de la contraseña", "Debe contener entre 8 y 20 caracteres, incorpora al menos una letra en mayúsculas y minúsculas,  algún dígito y algún carácter especial");
    }

    @FXML
    private void handleRegisterEnter(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            try{
                if (isValidAge() && checkPasswordsMatch() && validEmail && validPassword) {
                    Navigation navigation = Navigation.getInstance();
                    navigation.registerUser(usuario.getText(), email1.getText(), password1.getText(), avatar.getImage(), date.getValue());

                    showAlert(Alert.AlertType.INFORMATION, "Registro exitoso", "Usuario creado correctamente");
                    Stage stage = (Stage) usuario.getScene().getWindow();
                    stage.close();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error de validación", "Por favor corrige los campos marcados o vacíos");
                }
            } catch(NavDAOException n){ System.out.println(n); }
        }
    }

    
    
}
