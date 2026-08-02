/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafxmlapplication.FXMLLoginController;
import model.Session;
import model.User;

/**
 * FXML Controller class
 *
 * @author dario
 */
public class FXMLStatsController implements Initializable {

    @FXML
    private DatePicker filtroFecha;
    @FXML
    private Button buscarButton;
    @FXML
    private TableView<Session> tabla;
    @FXML
    private TableColumn<Session, String> filaFecha;
    @FXML
    private TableColumn<Session, Integer> filaAciertos;
    @FXML
    private TableColumn<Session, Integer> filaFallos;
    
    ObservableList<Session> sesionesUsuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        User currentUser = FXMLLoginController.getCurrentUser();
        if (currentUser == null) return;

        List<Session> sesiones = currentUser.getSessions();
        sesionesUsuario = FXCollections.observableArrayList(sesiones);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


        filaFecha.setCellValueFactory(cellData -> {
            String formattedDate = cellData.getValue().getTimeStamp().format(formatter);
            return new javafx.beans.property.SimpleStringProperty(formattedDate);
        });
        filaAciertos.setCellValueFactory(data ->
            new javafx.beans.property.SimpleIntegerProperty(data.getValue().getHits()).asObject()
        );
        filaFallos.setCellValueFactory(data ->
            new javafx.beans.property.SimpleIntegerProperty(data.getValue().getFaults()).asObject()
        );

        tabla.setItems(sesionesUsuario);
    }    

    @FXML
    private void filtrarSesiones(ActionEvent event) {
        LocalDate desde = filtroFecha.getValue();
        if (desde == null) {
            tabla.setItems(sesionesUsuario);
            return;
        }

        ObservableList<Session> filtradas = sesionesUsuario.stream()
            .filter(s -> s.getTimeStamp().toLocalDate().isAfter(desde.minusDays(1)))
            .collect(Collectors.toCollection(FXCollections::observableArrayList));

        tabla.setItems(filtradas);
    }
    
}
