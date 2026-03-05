/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import poiupv.FXMLDocumentController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.NavDAOException;
import model.Navigation;
import model.User;

/**
 * FXML Controller class
 *
 * @author dario
 */
public class FXMLLoginController implements Initializable {
    
    private boolean correctPassword;
    private User user;
    public static User currentUser;
    
    private FXMLDocumentController mainController;
    
    @FXML
    private Label passwordError;
    @FXML
    private TextField usuario;
    @FXML
    private TextField password;
    @FXML
    private Button loginButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label userError;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PauseTransition pause = new PauseTransition(Duration.millis(300));
        password.textProperty().addListener((obs, oldText, newText) -> {
            pause.setOnFinished(e -> {
                try {
                    user = Navigation.getInstance().authenticate(usuario.getText(), newText);
                } catch (NavDAOException ex) {
                    System.out.println(ex);
                }

                if (user != null) {
                    correctPassword = user.chekCredentials(usuario.getText(), newText);
                }

                passwordError.setVisible(!correctPassword);
                if (!correctPassword) {
                    passwordError.setText("Contraseña incorrecta");
                }
            });
            pause.playFromStart();
        });

    }    

    @FXML
    private void cerrarLogin(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void loginHandler(ActionEvent event) throws IOException {
        if(correctPassword){
            mainController.sesionButton.setVisible(false);
            mainController.registroButton.setVisible(false);
            mainController.problemasButton.setVisible(true);
            mainController.statsButton.setVisible(true);
            currentUser = user;
            
            ObservableList<Node> children = mainController.hBox.getChildren();
            int indexCerrar = children.indexOf(mainController.cerrarSesion);
            int indexSesion = children.indexOf(mainController.sesionButton);
            

            if (indexCerrar != -1 && indexSesion != -1) {
                if (indexCerrar > indexSesion) {
                    children.remove(indexCerrar);
                    children.remove(indexSesion);
                    children.remove(mainController.perfil);
                    children.add(indexSesion - 1, mainController.cerrarSesion);
                    children.add(indexCerrar - 1, mainController.sesionButton);
                    children.add(indexSesion, mainController.perfil);
                } else {
                    children.remove(indexSesion);
                    children.remove(indexCerrar);
                    children.remove(mainController.perfil);
                    children.add(indexCerrar - 1, mainController.sesionButton);
                    children.add(indexSesion - 1, mainController.cerrarSesion);
                    children.add(indexSesion, mainController.perfil);
                }
            }
            mainController.cerrarSesion.setVisible(true);
            mainController.perfilImage.setImage(currentUser.getAvatar());
            mainController.perfil.setVisible(true);
            Stage stage = (Stage) usuario.getScene().getWindow();
            stage.close();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error de validación", "Por favor corrige los campos incorrectos o vacíos", "Error de validación");
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String message, String header) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void setMainController(FXMLDocumentController controller) {
        this.mainController = controller;
    }
    
    public static User getCurrentUser(){
        return currentUser;
    }

    @FXML
    private void handleLoginEnter(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)){
            if(correctPassword){
                mainController.sesionButton.setVisible(false);
                mainController.registroButton.setVisible(false);
                mainController.problemasButton.setVisible(true);
                mainController.statsButton.setVisible(true);
                currentUser = user;

                ObservableList<Node> children = mainController.hBox.getChildren();
                int indexCerrar = children.indexOf(mainController.cerrarSesion);
                int indexSesion = children.indexOf(mainController.sesionButton);


                if (indexCerrar != -1 && indexSesion != -1) {
                    if (indexCerrar > indexSesion) {
                        children.remove(indexCerrar);
                        children.remove(indexSesion);
                        children.remove(mainController.perfil);
                        children.add(indexSesion - 1, mainController.cerrarSesion);
                        children.add(indexCerrar - 1, mainController.sesionButton);
                        children.add(indexSesion, mainController.perfil);
                    } else {
                        children.remove(indexSesion);
                        children.remove(indexCerrar);
                        children.remove(mainController.perfil);
                        children.add(indexCerrar - 1, mainController.sesionButton);
                        children.add(indexSesion - 1, mainController.cerrarSesion);
                        children.add(indexSesion, mainController.perfil);
                    }
                }
                mainController.cerrarSesion.setVisible(true);
                mainController.perfilImage.setImage(currentUser.getAvatar());
                mainController.perfil.setVisible(true);
                Stage stage = (Stage) usuario.getScene().getWindow();
                stage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error de validación", "Por favor corrige los campos incorrectos o vacíos", "Error de validación");
            }
        }
    }
}
