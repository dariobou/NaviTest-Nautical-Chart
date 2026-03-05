/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafxmlapplication.FXMLLoginController;
import model.Answer;
import model.NavDAOException;
import model.Navigation;
import model.Problem;
import model.User;

/**
 * FXML Controller class
 *
 * @author dario
 */
public class FXMLProblemasController implements Initializable {

    private List<Problem> listaProblemas;
    private Problem problemaActual;
    private int hits = 0;
    private int faults = 0;
    
    @FXML
    private ToggleGroup opciones;
    @FXML
    private RadioButton opcion1;
    @FXML
    private RadioButton opcion2;
    @FXML
    private RadioButton opcion3;
    @FXML
    private RadioButton opcion4;
    @FXML
    private Button corregirButton;
    @FXML
    private ComboBox<Problem> selectorPregunta;
    @FXML
    private Label resultado;
    @FXML
    private Label pregunta;
    @FXML
    private Button siguiente;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Navigation navigation;
        try {
            navigation = Navigation.getInstance();
            listaProblemas = navigation.getProblems();
        } catch (NavDAOException ex) {
            Logger.getLogger(FXMLProblemasController.class.getName()).log(Level.SEVERE, null, ex);
        }
        selectorPregunta.setItems(FXCollections.observableArrayList(listaProblemas));
        
        selectorPregunta.setCellFactory(cb -> new ListCell<Problem>() {
            private final Label label = new Label();

            {
                label.setMaxWidth(600);
                label.setEllipsisString("...");
                label.setStyle("-fx-text-overrun: ellipsis;");
            }

            @Override
            protected void updateItem(Problem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item.getText());
                    setGraphic(label);
                }
            }
        });

        selectorPregunta.setButtonCell(new ListCell<Problem>() {
            @Override
            protected void updateItem(Problem item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : truncateText(item.getText(), 50));
            }

            private String truncateText(String text, int maxLength) {
                return text.length() > maxLength ? text.substring(0, maxLength) + "..." : text;
            }
        });

        selectorPregunta.setOnAction(e -> mostrarProblema(selectorPregunta.getValue()));
           
        cargarProblemaAleatorio();
    }    

    @FXML
    private void handleCorregir(ActionEvent event) {
        Toggle seleccion = opciones.getSelectedToggle();
        if (seleccion == null) {
            resultado.setText("Selecciona una opción");
            return;
        }

        Answer respuestaSeleccionada = (Answer) seleccion.getUserData();
        boolean esCorrecta = respuestaSeleccionada.getValidity();

        if (esCorrecta) {
            resultado.setText("✔ Correcto");
            resultado.setStyle("-fx-text-fill: green;");
            hits++;
        } else {
            resultado.setText("✘ Incorrecto ");
            resultado.setStyle("-fx-text-fill: red;");
            faults++;
        }
        corregirButton.setDisable(true);
    }
    
    private void cargarProblemaAleatorio() {
        if (listaProblemas.isEmpty()) return;
        Random rand = new Random();
        int index = rand.nextInt(listaProblemas.size());
        mostrarProblema(listaProblemas.get(index));
    }
    
    private void mostrarProblema(Problem problem) {
        this.problemaActual = problem;
        pregunta.setText(problem.getText());
        List<Answer> answers = new ArrayList<>(problem.getAnswers());
        Collections.shuffle(answers);
        
        List<RadioButton> botones = List.of(opcion1, opcion2, opcion3, opcion4);
    for (int i = 0; i < 4; i++) {
        botones.get(i).setText(answers.get(i).getText());
        botones.get(i).setUserData(answers.get(i));
    }
        
        corregirButton.setDisable(false);
        opciones.selectToggle(null);
        resultado.setText("");
    }
    
    public void guardarSesion() {
        User currentUser = FXMLLoginController.getCurrentUser();
        if (currentUser != null) {
            currentUser.addSession(hits, faults);
        }
    }

    @FXML
    private void handleSiguiente(ActionEvent event) {
        cargarProblemaAleatorio();
    }
}
