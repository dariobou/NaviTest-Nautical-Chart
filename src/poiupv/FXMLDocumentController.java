/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import javafx.scene.text.Text;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafxmlapplication.FXMLLoginController;
import javafxmlapplication.FXMLLoginController;
import javafxmlapplication.FXMLPerfilController;
import javafxmlapplication.FXMLPerfilController;
import javafxmlapplication.FXMLRegisterController;
import model.User;
import poiupv.Poi;
import poiupv.Poi;
import poiupv.Poi;

/**
 *
 * @author jsoler
 */
public class FXMLDocumentController implements Initializable {

    //=======================================
    // hashmap para guardar los puntos de interes POI
    private final HashMap<String, Poi> hm = new HashMap<>();
    private ObservableList<Poi> data;
    @FXML
    private ToggleButton herramientaCirculo;
    @FXML
    private ToggleButton herramientaCruz;
    @FXML
    private Slider sliderTamano;

    private FXMLProblemasController fxmlProblemasController;
    private Point2D arcCenter;
    private Arc arcShape;

    
    // ======================================
    // la variable zoomGroup se utiliza para dar soporte al zoom
    // el escalado se realiza sobre este nodo, al escalar el Group no mueve sus nodos
    private Point2D lineaInicio = null;
    private javafx.scene.shape.Line previewLine = null;
    private Node previewPuntoA = null;
    private Node previewPuntoB = null;
    // Variables adicionales para manejar el arco
    /*private Circle circlePainting = null;
    private Point2D arcCenter = null;
    private Node marcaInicioArc = null;
    private Node marcaFinArc = null;*/
    @FXML
    private ToggleButton herramientaTexto;
    @FXML
    public HBox hBox;
    @FXML
    public Button perfil;
    @FXML
    public ImageView perfilImage;
    
    private FXMLLoginController mainController;

    @FXML
    private ToggleButton herramientaLatitud;
    @FXML
    private ToggleButton herramientaTransportador;
    @FXML
    private ImageView transportadorImg;
    @FXML
    private ImageView reglaView;
    @FXML
    private ToggleButton herramientaRegla;
    @FXML
    private Slider sliderRegTra;
    @FXML
    private Slider rotarRegla;
    @FXML
    private SplitPane splitPane;
    @FXML
    public Button problemasButton;
    @FXML
    public Button statsButton;
    @FXML
    private MenuButton formaChoiceBox;
    @FXML
    private Label sizeLabel;
    @FXML
    private HBox toolsConfig;


    @FXML
    private void handleProfile(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPerfil.fxml"));
            Parent root = loader.load();
            
            FXMLPerfilController perfilController = loader.getController();
            perfilController.setMainController(this);
        
            Stage secondaryStage = new Stage();
            secondaryStage.setScene(new Scene(root));
            secondaryStage.setTitle("Perfil");
            secondaryStage.setResizable(false);
            secondaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
            
            FXMLPerfilController controller = loader.getController();
            User user = FXMLLoginController.getCurrentUser();
            controller.setDatos(user.getNickName(), user.getEmail(), user.getPassword(), user.getBirthdate(), user.getAvatar());
        
            Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            secondaryStage.initOwner(primaryStage);
            secondaryStage.initModality(Modality.WINDOW_MODAL);
        
            secondaryStage.show();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @FXML
    private void handleCierre(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar cierre de sesión");
        alert.setHeaderText("¿Estás seguro de que quieres cerrar sesión?");
        alert.setContentText("Se cerrará tu sesión actual y volverás a la pantalla de inicio.");

        ButtonType botonSi = new ButtonType("Sí", ButtonBar.ButtonData.OK_DONE);
        ButtonType botonNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(botonSi, botonNo);

        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.isPresent() && resultado.get() == botonSi) {
            if (fxmlProblemasController != null) {
                fxmlProblemasController.guardarSesion();
                System.out.println("Sesión guardada al cerrar sesión");
            }
            FXMLLoginController.currentUser = null;

            ObservableList<Node> children = hBox.getChildren();
            int indexPerfil = children.indexOf(perfil);

            perfil.setVisible(false);
            cerrarSesion.setVisible(false);
            children.remove(cerrarSesion);
            children.remove(sesionButton);
            children.remove(registroButton);
            children.remove(perfil);
            children.add(indexPerfil - 3, sesionButton);
            children.add(indexPerfil - 3, registroButton);
            children.add(indexPerfil - 3, perfil);
            children.add(indexPerfil - 3, cerrarSesion);
            sesionButton.setVisible(true);
            registroButton.setVisible(true);
            problemasButton.setVisible(false);
            statsButton.setVisible(false);
        }
    }

  @FXML
private void pressedTransportador(ActionEvent event) {
    transportadorImg.setVisible(herramientaTransportador.isSelected());
    boolean selected = herramientaTransportador.isSelected();
    if (selected) {
        centrarHerramientas();
        herramientaRegla.setSelected(false);
        reglaView.setVisible(false);
    }
    updateSlidersVisibility();
}
    @FXML
    private void pressedRegla(ActionEvent event) {
     reglaView.setVisible(herramientaRegla.isSelected());
      boolean selected = herramientaRegla.isSelected();
    if (selected) {
        centrarHerramientas();
        herramientaTransportador.setSelected(false);
        transportadorImg.setVisible(false);
    }
    updateSlidersVisibility();
}

    @FXML
    private void handleProblemas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLProblemas.fxml"));
            Parent root = loader.load();
            FXMLProblemasController problemasController = loader.getController();
            this.fxmlProblemasController = problemasController;
            
            Stage newStage = new Stage();
            newStage.setTitle("Problemas");
            newStage.setScene(new Scene(root));
            newStage.setResizable(false);
            newStage.initModality(Modality.NONE);
            newStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleStats(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLStats.fxml"));
            Parent root = loader.load();
            
            Stage newStage = new Stage();
            newStage.setTitle("Estadísticas");
            newStage.setScene(new Scene(root));
            newStage.setResizable(false);
            newStage.initModality(Modality.NONE);
            newStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private enum ToolType {
    NONE, POINT, LINE, ARC, DELETE, TEXT, LATLONG
}

    private ToolType currentTool = ToolType.NONE;
    
    private enum PointShape {
    CIRCLE, CROSS
} 
    
    private PointShape currentShape = PointShape.CIRCLE; 
    private Group zoomGroup;
 
    @FXML
    private ListView<Poi> map_listview;
    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private Slider zoom_slider;
    @FXML
    private Label mousePosition;
    @FXML
    public Button sesionButton;
    @FXML
    public Button registroButton;
    @FXML
    public Button cerrarSesion;
    @FXML
    private ToggleButton herramientaPunto;
    @FXML
    private ToggleButton herramientaLinea;
    @FXML
    private ToggleButton herramientaArco;
    @FXML
    private ToggleButton herramientaEliminar;
    @FXML
    private ColorPicker elegirColor;
    @FXML
    private Button herramientaLimpiar;

    @FXML
    void zoomIn(ActionEvent event) {
        //================================================
        // el incremento del zoom dependerá de los parametros del 
        // slider y del resultado esperado
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal += 0.1);
    }

    @FXML
    void zoomOut(ActionEvent event) {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal + -0.1);
    }
    
    // esta funcion es invocada al cambiar el value del slider zoom_slider
    private void zoom(double scaleValue) {
        //===================================================
        //guardamos los valores del scroll antes del escalado
        double scrollH = map_scrollpane.getHvalue();
        double scrollV = map_scrollpane.getVvalue();
        //===================================================
        // escalamos el zoomGroup en X e Y con el valor de entrada
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        //===================================================
        // recuperamos el valor del scroll antes del escalado
        map_scrollpane.setHvalue(scrollH);
        map_scrollpane.setVvalue(scrollV);
    }  
    private static Scene scene;
    static void setRoot(Parent root){
    scene.setRoot(root);
    }

   


    private void initData() {
        data=map_listview.getItems();
 
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initData();
        //==========================================================
        // inicializamos el slider y enlazamos con el zoom
        

        zoom_slider.setValue(0.1);
        zoom_slider.setMin(0.2);
        zoom_slider.setMax(1.5);
        zoom_slider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));


        //=========================================================================
        //Envuelva el contenido de scrollpane en un grupo para que 
        //ScrollPane vuelva a calcular las barras de desplazamiento tras el escalado
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(contentGroup);
        zoom(zoom_slider.getValue());
        rotarRegla.setMin(0);
         rotarRegla.setMax(360);
       rotarRegla.setValue(0);
    rotarRegla.setShowTickLabels(true);
    rotarRegla.setShowTickMarks(true);
    rotarRegla.setMajorTickUnit(90);
    rotarRegla.setMinorTickCount(3);
    rotarRegla.setSnapToTicks(true);
    
    setupReglaRotation();
    updateSlidersVisibility();
     formaChoiceBox.setVisible(false);
    sizeLabel.setVisible(false);
    sliderTamano.setVisible(false);
    elegirColor.setVisible(false);
    map_listview.setCellFactory(lv -> {
    return new javafx.scene.control.ListCell<>() {
        @Override
        protected void updateItem(Poi item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setTooltip(null);
            } else {
                setText(item.toString());

                Tooltip tooltip = new Tooltip("Doble clic para editar nombre");
                setTooltip(tooltip);
                tooltip.setShowDelay(Duration.millis(100));  // 100ms de retraso para aparecer
                tooltip.setHideDelay(Duration.millis(100)); // 100ms para desaparecer
            }
        }
    };
});

map_scrollpane.setOnMouseEntered(e -> {
    if (!herramientaEliminar.isSelected()) {
        zoomGroup.setStyle("-fx-cursor: crosshair;");
    }
});
map_scrollpane.setOnMouseExited(e -> {
    zoomGroup.setStyle("-fx-cursor: default;");
});
map_scrollpane.setOnDragDetected(e -> {
    if (currentTool == ToolType.NONE) {
        zoomGroup.setStyle("-fx-cursor: move;");
    }
});
map_scrollpane.setOnMouseReleased(e -> {
    if (!herramientaEliminar.isSelected()) {
        zoomGroup.setStyle("-fx-cursor: crosshair;");
    }
});
map_scrollpane.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
    if (currentTool == ToolType.NONE && !herramientaArco.isSelected()) {
        zoomGroup.setStyle("-fx-cursor: move;");
    }
});
map_scrollpane.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
    if (currentTool == ToolType.NONE && !herramientaArco.isSelected()) {
        zoomGroup.setStyle("-fx-cursor: crosshair;");
    }
});
    map_listview.setOnMouseClicked(e -> {
        Poi selectedPoi = map_listview.getSelectionModel().getSelectedItem();
        if (selectedPoi == null) return;

        // Coordenadas absolutas del POI en el zoomGroup
        double poiX = selectedPoi.getPosition().getX();
        double poiY = selectedPoi.getPosition().getY();

        // Tamaños
        double contentWidth = zoomGroup.getBoundsInLocal().getWidth();
        double contentHeight = zoomGroup.getBoundsInLocal().getHeight();
        double viewportWidth = map_scrollpane.getViewportBounds().getWidth();
        double viewportHeight = map_scrollpane.getViewportBounds().getHeight();

        // Calculamos el valor hvalue y vvalue para que el punto quede justo en el centro del viewport
        double hTarget = (poiX - viewportWidth / 2.0) / (contentWidth - viewportWidth);
        double vTarget = (poiY - viewportHeight / 2.0) / (contentHeight - viewportHeight);

        // Aseguramos que esté entre 0 y 1
        hTarget = Math.max(0, Math.min(hTarget, 1));
        vTarget = Math.max(0, Math.min(vTarget, 1));

        // Animación suave al punto centrado
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(400),
                new KeyValue(map_scrollpane.hvalueProperty(), hTarget),
                new KeyValue(map_scrollpane.vvalueProperty(), vTarget)
            )
        );
        timeline.play();

    // Si se hace doble clic, se edita el nombre
    if (e.getClickCount() == 2) {
        TextInputDialog dialog = new TextInputDialog(selectedPoi.getCode());
        dialog.setTitle("Renombrar POI");
        dialog.setHeaderText("Editar nombre del punto");
        dialog.setContentText("Nuevo nombre:");

        dialog.showAndWait().ifPresent(newCode -> {
            selectedPoi.setCode(newCode);
            map_listview.refresh(); // Refrescar para actualizar la vista
        });
    }
});


        // Inicialización de la regla
Image reglaImg = new Image(getClass().getResourceAsStream("/resources/regla.png"));
reglaView = new ImageView(reglaImg);
reglaView.setOpacity(1);
reglaView.setPreserveRatio(true);
reglaView.setFitWidth(1700);  // Tamaño más grande (ajusta según necesidad)
reglaView.setVisible(false); // Inicialmente oculta

// Posición inicial centrada
reglaView.setLayoutX(777);
reglaView.setLayoutY(320);

// Hacerla arrastrable
reglaView.setOnMousePressed(e -> {
    if (herramientaRegla.isSelected()) {
        e.consume(); // Para que no arrastre el mapa
        reglaView.setUserData(new Point2D(e.getSceneX(), e.getSceneY()));
    }
});

reglaView.setOnMouseDragged(e -> {
    if (herramientaRegla.isSelected()) {
        e.consume();
        Point2D last = (Point2D) reglaView.getUserData();
        Point2D current = zoomGroup.sceneToLocal(e.getSceneX(), e.getSceneY());
        Point2D previous = zoomGroup.sceneToLocal(last.getX(), last.getY());

        double dx = current.getX() - previous.getX();
        double dy = current.getY() - previous.getY();

        reglaView.setLayoutX(reglaView.getLayoutX() + dx);
        reglaView.setLayoutY(reglaView.getLayoutY() + dy);
        reglaView.setUserData(new Point2D(e.getSceneX(), e.getSceneY()));
    }
});

zoomGroup.getChildren().add(reglaView);
Image transportador = new Image(getClass().getResourceAsStream("/resources/transportador.png"));
transportadorImg = new ImageView(transportador);
transportadorImg.setFitWidth(1500);
sliderRegTra.valueProperty().addListener((obs, oldVal, newVal) -> {
    double scale = newVal.doubleValue() / 1500.0;
    transportadorImg.setScaleX(scale);
    transportadorImg.setScaleY(scale);
});

transportadorImg.setPreserveRatio(true);
transportadorImg.setOpacity(0.6);
transportadorImg.setVisible(false);
transportadorImg.setLayoutX(777);
transportadorImg.setLayoutY(320);
zoomGroup.getChildren().add(transportadorImg);
centrarHerramientas();


transportadorImg.setOnMousePressed(e -> {
    transportadorImg.setUserData(new Point2D(e.getSceneX(), e.getSceneY()));
    e.consume();
});
transportadorImg.setOnMouseDragged(e -> {
    Point2D last = (Point2D) transportadorImg.getUserData();
Point2D current = zoomGroup.sceneToLocal(e.getSceneX(), e.getSceneY());
Point2D previous = zoomGroup.sceneToLocal(last.getX(), last.getY());

double dx = current.getX() - previous.getX();
double dy = current.getY() - previous.getY();

   transportadorImg.setLayoutX(transportadorImg.getLayoutX() + dx);
transportadorImg.setLayoutY(transportadorImg.getLayoutY() + dy);
transportadorImg.setUserData(new Point2D(e.getSceneX(), e.getSceneY()));

    e.consume();
});

        zoomGroup.setOnMouseMoved(e -> {
    if (currentTool == ToolType.LINE && lineaInicio != null) {
        Point2D pos = zoomGroup.sceneToLocal(e.getSceneX(), e.getSceneY());
        double size = sliderTamano.getValue();

        if (previewLine == null) {
            previewLine = new javafx.scene.shape.Line();
            previewLine.getStrokeDashArray().addAll(5.0, 5.0); // línea discontinua
            previewLine.setStroke(Color.GRAY);
            zoomGroup.getChildren().add(previewLine);
        }

        previewLine.setStartX(lineaInicio.getX());
        previewLine.setStartY(lineaInicio.getY());
        previewLine.setEndX(pos.getX());
        previewLine.setEndY(pos.getY());

        // Crear marcador para punto final temporal
        if (previewPuntoB != null) zoomGroup.getChildren().remove(previewPuntoB);

        if (currentShape == PointShape.CIRCLE) {
            Circle c = new Circle(pos.getX(), pos.getY(), size, Color.TRANSPARENT);
            c.setStroke(Color.GRAY);
            previewPuntoB = c;
        } else {
            javafx.scene.shape.Line l1 = new javafx.scene.shape.Line(
                pos.getX() - size, pos.getY() - size,
                pos.getX() + size, pos.getY() + size
            );
            javafx.scene.shape.Line l2 = new javafx.scene.shape.Line(
                pos.getX() - size, pos.getY() + size,
                pos.getX() + size, pos.getY() - size
            );
            Group cruz = new Group(l1, l2);
            l1.setStroke(Color.GRAY);
            l2.setStroke(Color.GRAY);
            previewPuntoB = cruz;
        }

        zoomGroup.getChildren().add(previewPuntoB);
    }
});
        herramientaCirculo.setSelected(true);
herramientaCruz.setSelected(false);
currentShape = PointShape.CIRCLE;

        zoomGroup.setOnMouseClicked(e -> {
            map_listview.getSelectionModel().clearSelection();

            double tamano = sliderTamano.getValue();
            Point2D pos = zoomGroup.sceneToLocal(e.getSceneX(), e.getSceneY());
    Color selectedColor = elegirColor.getValue();
    
        if (currentTool == ToolType.DELETE) {
        Node clickedNode = e.getPickResult().getIntersectedNode();
        
        // Buscar el arco en la jerarquía de nodos
        while (clickedNode != null && !(clickedNode instanceof Arc) && clickedNode != zoomGroup) {
            clickedNode = clickedNode.getParent();
        }
        
        if (clickedNode instanceof Arc) {
            zoomGroup.getChildren().remove(clickedNode);
           e.consume();
        }
    }
   else if (currentTool == ToolType.POINT) {
        if (currentShape == PointShape.CIRCLE) {
            Circle punto = new Circle(pos.getX(), pos.getY(), tamano);
            punto.setFill(selectedColor);
            punto.setStroke(Color.BLACK);
          String defaultName = "POI sin nombre #" + (map_listview.getItems().size() + 1);
Poi poi = new Poi(defaultName, "", pos.getX(), pos.getY());
poi.setPosition(pos);
map_listview.getItems().add(poi);

            
            
            // Añadir comportamiento de eliminar
            punto.setOnMouseClicked(ev -> {
                if (currentTool == ToolType.DELETE) {
                    zoomGroup.getChildren().remove(punto);
                     map_listview.getItems().remove(poi);
                    ev.consume();
                }
            });

            zoomGroup.getChildren().add(punto);

        }
      

       else if (currentShape == PointShape.CROSS) {
    double size = tamano;
    javafx.scene.shape.Line l1 = new javafx.scene.shape.Line(
        pos.getX() - size, pos.getY() - size,
        pos.getX() + size, pos.getY() + size
    );
    javafx.scene.shape.Line l2 = new javafx.scene.shape.Line(
        pos.getX() - size, pos.getY() + size,
        pos.getX() + size, pos.getY() - size
    );

    l1.setStroke(selectedColor);
    l2.setStroke(selectedColor);
    l1.setStrokeWidth(size / 2);  // Usar el tamaño del slider para el grosor
    l2.setStrokeWidth(size / 2);  // Usar el tamaño del slider para el grosor

    Group cruz = new Group(l1, l2);
    
    // Crear POI y asociarlo a la cruz
    String defaultName = "POI sin nombre #" + (map_listview.getItems().size() + 1);
        Poi poi = new Poi(defaultName, "", pos.getX(), pos.getY());
        poi.setPosition(pos);
        map_listview.getItems().add(poi);

            cruz.setOnMouseClicked(ev -> {
                if (currentTool == ToolType.DELETE) {
                    zoomGroup.getChildren().remove(cruz);
                    map_listview.getItems().remove(poi); // eliminar también de la lista
                    ev.consume();
                }
            });

            zoomGroup.getChildren().add(cruz);
        }
            }
            
            else if (currentTool == ToolType.LINE) {
            if (lineaInicio == null) {
                lineaInicio = pos;

                // Crear punto inicial temporal
                Node puntoInicial = null;
                if (currentShape == PointShape.CIRCLE) {
                    puntoInicial = new Circle(pos.getX(), pos.getY(), tamano, selectedColor);
                    ((Circle)puntoInicial).setStroke(Color.BLACK);
                } else {
                    javafx.scene.shape.Line l1 = new javafx.scene.shape.Line(
                        pos.getX() - tamano, pos.getY() - tamano,
                        pos.getX() + tamano, pos.getY() + tamano
                    );
                    javafx.scene.shape.Line l2 = new javafx.scene.shape.Line(
                        pos.getX() - tamano, pos.getY() + tamano,
                        pos.getX() + tamano, pos.getY() - tamano
                    );
                    l1.setStroke(selectedColor);
                    l2.setStroke(selectedColor);
                    l1.setStrokeWidth(tamano / 2);  
l2.setStrokeWidth(tamano / 2);  
                    puntoInicial = new Group(l1, l2);
                }

                previewPuntoA = puntoInicial;
                zoomGroup.getChildren().add(previewPuntoA);

            } else {
                // Crear grupo para línea + puntos
                Group grupoLinea = new Group();

                // Eliminar punto temporal previewPuntoA si existe
                if (previewPuntoA != null) {
                    zoomGroup.getChildren().remove(previewPuntoA);
                    previewPuntoA = null;
                }

                // Línea - aquí aplicamos el grosor según el slider
                javafx.scene.shape.Line linea = new javafx.scene.shape.Line(
                    lineaInicio.getX(), lineaInicio.getY(),
                    pos.getX(), pos.getY()
                );
                linea.setStroke(selectedColor);
                linea.setStrokeWidth(tamano/2);  // Ajustamos el grosor en función del slider

                grupoLinea.getChildren().add(linea);

                // Punto inicial (definitivo)
                if (currentShape == PointShape.CIRCLE) {
                    Circle punto1 = new Circle(lineaInicio.getX(), lineaInicio.getY(), tamano, selectedColor);
                    punto1.setStroke(Color.BLACK);
                    grupoLinea.getChildren().add(punto1);
                } else {
                    javafx.scene.shape.Line l1 = new javafx.scene.shape.Line(
                        lineaInicio.getX() - tamano, lineaInicio.getY() - tamano,
                        lineaInicio.getX() + tamano, lineaInicio.getY() + tamano
                    );
                    javafx.scene.shape.Line l2 = new javafx.scene.shape.Line(
                        lineaInicio.getX() - tamano, lineaInicio.getY() + tamano,
                        lineaInicio.getX() + tamano, lineaInicio.getY() - tamano
                    );
                    l1.setStroke(selectedColor);
                    l2.setStroke(selectedColor);
                    l1.setStrokeWidth(tamano/2);
                    l2.setStrokeWidth(tamano/2);
                    grupoLinea.getChildren().addAll(l1, l2);
                }

                // Punto final
                if (currentShape == PointShape.CIRCLE) {
                    Circle punto2 = new Circle(pos.getX(), pos.getY(), tamano, selectedColor);
                    punto2.setStroke(Color.BLACK);
                    grupoLinea.getChildren().add(punto2);
                } else {
                    javafx.scene.shape.Line l1 = new javafx.scene.shape.Line(
                        pos.getX() - tamano, pos.getY() - tamano,
                        pos.getX() + tamano, pos.getY() + tamano
                    );
                    javafx.scene.shape.Line l2 = new javafx.scene.shape.Line(
                        pos.getX() - tamano, pos.getY() + tamano,
                        pos.getX() + tamano, pos.getY() - tamano
                    );
                    l1.setStroke(selectedColor);
                    l2.setStroke(selectedColor);
                    l1.setStrokeWidth(tamano / 2);
                    l2.setStrokeWidth(tamano / 2);
                    grupoLinea.getChildren().addAll(l1, l2);
                }


                // Configurar eliminación del grupo
                grupoLinea.setOnMouseClicked(ev -> {
                    if (currentTool == ToolType.DELETE) {
                        zoomGroup.getChildren().remove(grupoLinea);
                        ev.consume();
                    }
                });

                zoomGroup.getChildren().add(grupoLinea);

                // Limpiar estado temporal
                lineaInicio = null;
                if (previewLine != null) {
                    zoomGroup.getChildren().remove(previewLine);
                    previewLine = null;
                }
                if (previewPuntoB != null) {
                    zoomGroup.getChildren().remove(previewPuntoB);
                    previewPuntoB = null;
                }
            }
        }
        });

        
zoomGroup.setOnMousePressed(event -> {
    if (currentTool == ToolType.TEXT) {
        // Crear TextField en la posición del click
        TextField textField = new TextField();
        textField.setLayoutX(event.getX());
        textField.setLayoutY(event.getY());

        // Aplicar color seleccionado
        textField.setStyle("-fx-text-fill: " + toHexString(elegirColor.getValue()) + ";");

        // Aplicar tamaño del slider (convertido a tamaño de fuente apropiado)
        double fontSize = sliderTamano.getValue() * 5;
        textField.setStyle(textField.getStyle() + "-fx-font-size: " + fontSize + "px;");

        zoomGroup.getChildren().add(textField);
        textField.requestFocus();

        // Manejar cuando se termina de editar
        textField.setOnAction(e -> {
            crearTextoDefinitivo(textField.getText(), textField.getLayoutX(), textField.getLayoutY());
            zoomGroup.getChildren().remove(textField);
            e.consume();
        });

        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                crearTextoDefinitivo(textField.getText(), textField.getLayoutX(), textField.getLayoutY());
                zoomGroup.getChildren().remove(textField);
            }
        });

        event.consume();
    }
    else if (herramientaArco.isSelected() && event.isPrimaryButtonDown()) {
        arcCenter = new Point2D(event.getX(), event.getY());

        arcShape = new Arc();
        arcShape.setCenterX(arcCenter.getX());
        arcShape.setCenterY(arcCenter.getY());
        arcShape.setRadiusX(1);
        arcShape.setRadiusY(1);
        arcShape.setStartAngle(0);
        arcShape.setLength(0);
        arcShape.setType(ArcType.OPEN);
        arcShape.setStroke(elegirColor.getValue());
        arcShape.setStrokeWidth(sliderTamano.getValue());
        arcShape.setFill(null);

        zoomGroup.getChildren().add(arcShape);
    }
});

        Platform.runLater(() -> {
            Stage stage = (Stage) herramientaLimpiar.getScene().getWindow(); // usa cualquier botón del FXML
            stage.setOnCloseRequest(event -> {
                if (fxmlProblemasController != null) {
                    fxmlProblemasController.guardarSesion();
                    System.out.println("Sesión guardada al cerrar ventana principal");
                }
            });
        });
        
        

       zoomGroup.setOnMouseDragged(event -> {
    if (herramientaArco.isSelected() && arcShape != null) {
        
        if (currentTool != ToolType.DELETE) {
            double dx = event.getX() - arcCenter.getX();
            double dy = event.getY() - arcCenter.getY();
            double radius = Math.sqrt(dx * dx + dy * dy);

            arcShape.setRadiusX(radius);
            arcShape.setRadiusY(radius);

            double angle = Math.toDegrees(Math.atan2(-dy, dx));
            if (angle < 0) angle += 360;

            arcShape.setStartAngle(angle);
            arcShape.setLength(90);
        }
        event.consume();
    }
});
        zoomGroup.setOnMouseReleased(event -> {
    if (herramientaArco.isSelected() && arcShape != null) {
        arcShape = null;
        arcCenter = null;
    }
    if (!herramientaArco.isSelected()) {
        map_scrollpane.setPannable(true);
    }
});    }
   
private void updateSlidersVisibility() {
    sliderRegTra.setVisible(herramientaTransportador.isSelected());
    rotarRegla.setVisible(herramientaRegla.isSelected());
}

// Rotación de la regla
private void setupReglaRotation() {
    rotarRegla.valueProperty().addListener((obs, oldVal, newVal) -> {
        if (herramientaRegla.isSelected()) {
            reglaView.setRotate(newVal.doubleValue());
        }
    });
}
  private void crearTextoDefinitivo(String contenido, double x, double y) {
    if (contenido.isEmpty()) return;
    
    Text texto = new Text(contenido);
    texto.setX(x);
    texto.setY(y);
    
    // Aplicar color y tamaño
    texto.setFill(elegirColor.getValue());
    double fontSize = sliderTamano.getValue() * 5;
    texto.setStyle("-fx-font-size: " + fontSize + "px;");
    
    // Hacer el texto eliminable
    texto.setOnMouseClicked(ev -> {
        if (currentTool == ToolType.DELETE) {
            zoomGroup.getChildren().remove(texto);
            ev.consume();
        }
    });
    
    // Añadir menú contextual para eliminar
    ContextMenu contextMenu = new ContextMenu();
    MenuItem deleteItem = new MenuItem("Eliminar");
    deleteItem.setOnAction(e -> zoomGroup.getChildren().remove(texto));
    contextMenu.getItems().add(deleteItem);
    
    texto.setOnContextMenuRequested(e -> {
        contextMenu.show(texto, e.getScreenX(), e.getScreenY());
        e.consume();
    });
    
    zoomGroup.getChildren().add(texto);
}

private String toHexString(Color color) {
    return String.format("#%02X%02X%02X",
        (int)(color.getRed() * 255),
        (int)(color.getGreen() * 255),
        (int)(color.getBlue() * 255));
}
    private void showPosition(MouseEvent event) {
        mousePosition.setText("sceneX: " + (int) event.getSceneX() + ", sceneY: " + (int) event.getSceneY() + "\n"
                + "         X: " + (int) event.getX() + ",          Y: " + (int) event.getY());
    }
private void centrarHerramientas() {
    // Esperar a que la escena esté disponible
    Platform.runLater(() -> {
        if (map_scrollpane.getScene() != null) {
            // Obtener centro del área visible
            double centerX = map_scrollpane.getWidth() / 2;
            double centerY = map_scrollpane.getHeight() / 2;
            
            // Convertir a coordenadas del zoomGroup
            Point2D center = zoomGroup.sceneToLocal(
                map_scrollpane.localToScene(centerX, centerY));
            
            // Posicionar herramientas
            if (transportadorImg != null) {
                transportadorImg.setLayoutX(center.getX() - transportadorImg.getFitWidth()/2);
                transportadorImg.setLayoutY(center.getY() - transportadorImg.getFitHeight()/2);
            }
            
            if (reglaView != null) {
                reglaView.setLayoutX(center.getX() - reglaView.getFitWidth()/2);
                reglaView.setLayoutY(center.getY() - reglaView.getFitHeight()/2);
            }
        }
    });
}
    private void closeApp(ActionEvent event) {
        ((Stage) zoom_slider.getScene().getWindow()).close();
    }
    

    @FXML
    private void about(ActionEvent event) {
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        // Acceder al Stage del Dialog y cambiar el icono
        Stage dialogStage = (Stage) mensaje.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
        mensaje.setTitle("Acerca de");
        mensaje.setHeaderText("IPC - 2025");
        mensaje.showAndWait();
    }

    private void addPoi(MouseEvent event) {

        if (event.isControlDown()) {
            Dialog<Poi> poiDialog = new Dialog<>();
            poiDialog.setTitle("Nuevo POI");
            poiDialog.setHeaderText("Introduce un nuevo POI");
            // Acceder al Stage del Dialog y cambiar el icono
            Stage dialogStage = (Stage) poiDialog.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));

            ButtonType okButton = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
            poiDialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

            TextField nameField = new TextField();
            nameField.setPromptText("Nombre del POI");

            TextArea descArea = new TextArea();
            descArea.setPromptText("Descripción...");
            descArea.setWrapText(true);
            descArea.setPrefRowCount(5);

            VBox vbox = new VBox(10, new Label("Nombre:"), nameField, new Label("Descripción:"), descArea);
            poiDialog.getDialogPane().setContent(vbox);

            poiDialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButton) {
                    return new Poi(nameField.getText().trim(), descArea.getText().trim(), 0, 0);
                }
                return null;
            });
            Optional<Poi> result = poiDialog.showAndWait();

            if(result.isPresent()) {
                Point2D localPoint = zoomGroup.sceneToLocal(event.getSceneX(), event.getSceneY());
                Poi poi=result.get();
                poi.setPosition(localPoint);
                map_listview.getItems().add(poi);
            }
        }
    }
   private Node createMarker(double x, double y, Color color, double size, PointShape shape) {
    if (shape == PointShape.CIRCLE) {
        Circle c = new Circle(x, y, size, color);
        c.setStroke(Color.BLACK);
        return c;
    } else {
        javafx.scene.shape.Line l1 = new javafx.scene.shape.Line(x - size, y - size, x + size, y + size);
        javafx.scene.shape.Line l2 = new javafx.scene.shape.Line(x - size, y + size, x + size, y - size);
        l1.setStroke(color);
        l2.setStroke(color);
        l1.setStrokeWidth(size / 2);  // Usar el tamaño del slider para el grosor
        l2.setStrokeWidth(size / 2);  // Usar el tamaño del slider para el grosor

        return new Group(l1, l2);
    }
}

private void makeDeletable(Node node) {
    node.setOnMouseClicked(ev -> {
        if (currentTool == ToolType.DELETE) {
            zoomGroup.getChildren().remove(node);
            ev.consume();
        }
    });
}

    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLRegister.fxml"));
            Parent root = loader.load();
        
            Stage secondaryStage = new Stage();
            secondaryStage.setScene(new Scene(root));
            secondaryStage.setTitle("Registro");
            secondaryStage.setResizable(false);
            secondaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));

            Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            secondaryStage.initOwner(primaryStage);
            secondaryStage.initModality(Modality.WINDOW_MODAL);
        
            secondaryStage.show();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLLogin.fxml"));
            Parent root = loader.load();
            
            FXMLLoginController loginController = loader.getController();
            loginController.setMainController(this);

            Stage secondaryStage = new Stage();
            secondaryStage.setScene(new Scene(root));
            secondaryStage.setTitle("Iniciar Sesión");
            secondaryStage.setResizable(false);
        
            Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            secondaryStage.initOwner(primaryStage);
            secondaryStage.initModality(Modality.WINDOW_MODAL);
            secondaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
        
            secondaryStage.show();
        
        } catch (IOException e) {
            System.out.println(e);
        }
    }

   private void updateToolSelection(ToolType selectedTool) {
    herramientaPunto.setSelected(false);
    herramientaLinea.setSelected(false);
    herramientaArco.setSelected(false);
    herramientaEliminar.setSelected(false);
    herramientaTexto.setSelected(false);
    herramientaLatitud.setSelected(false);
    
    // Actualizar visibilidad de los controles de configuración
    boolean showShapeSizeColor = false;
    boolean showSizeColorOnly = false;
    
    switch(selectedTool) {
        case POINT:
            herramientaPunto.setSelected(true);
            showShapeSizeColor = true;
            break;
        case LINE:
            herramientaLinea.setSelected(true);
            showShapeSizeColor = true;
            break;
        case ARC:
            herramientaArco.setSelected(true);
            showShapeSizeColor = true;
            break;
        case DELETE:
            herramientaEliminar.setSelected(true);
            break;
        case TEXT:
            herramientaTexto.setSelected(true);
            showSizeColorOnly = true;
            break;
        case LATLONG:
            herramientaLatitud.setSelected(true);
            break;
        case NONE:
            break;
    }
    
    // Actualizar visibilidad de los controles
    formaChoiceBox.setVisible(showShapeSizeColor);
    toolsConfig.setVisible(showShapeSizeColor || showSizeColorOnly);
    sizeLabel.setVisible(showShapeSizeColor || showSizeColorOnly);
    sliderTamano.setVisible(showShapeSizeColor || showSizeColorOnly);
    elegirColor.setVisible(showShapeSizeColor || showSizeColorOnly);
    
    currentTool = selectedTool;
}

@FXML
private void pressedPunto(ActionEvent event) {
    if (herramientaPunto.isSelected()) {
        updateToolSelection(ToolType.POINT);
    } else {
        updateToolSelection(ToolType.NONE);
    }
}

@FXML
private void pressedLatLong(ActionEvent event) {
    if (herramientaLatitud.isSelected()) {
        updateToolSelection(ToolType.LATLONG);

        Poi selectedPoi = map_listview.getSelectionModel().getSelectedItem();
        if (selectedPoi != null) {
            Point2D pos = selectedPoi.getPosition();
            double tamano = sliderTamano.getValue();
            Color selectedColor = elegirColor.getValue();

            double width = zoomGroup.getBoundsInLocal().getWidth();
            double height = zoomGroup.getBoundsInLocal().getHeight();

            javafx.scene.shape.Line verticalLine = new javafx.scene.shape.Line(
                pos.getX(), 0,
                pos.getX(), height
            );
            verticalLine.setStroke(selectedColor);
            verticalLine.setStrokeWidth(tamano);
            verticalLine.getStrokeDashArray().addAll(4.0, 4.0);

            javafx.scene.shape.Line horizontalLine = new javafx.scene.shape.Line(
                0, pos.getY(),
                width, pos.getY()
            );
            horizontalLine.setStroke(selectedColor);
            horizontalLine.setStrokeWidth(tamano);
            horizontalLine.getStrokeDashArray().addAll(4.0, 4.0);

            Group latLongGroup = new Group(verticalLine, horizontalLine);
            latLongGroup.setOnMouseClicked(ev -> {
                if (currentTool == ToolType.DELETE) {
                    zoomGroup.getChildren().remove(latLongGroup);
                    ev.consume();
                }
            });

            zoomGroup.getChildren().add(latLongGroup);
            map_listview.getSelectionModel().clearSelection();

        } else {
            herramientaLatitud.setSelected(false);
            currentTool = ToolType.NONE;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sin punto seleccionado");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona un punto en la lista para aplicar latitud/longitud.");
            alert.showAndWait();
        }
    } else {
        updateToolSelection(ToolType.NONE);
    }
}


@FXML
private void pressedLinea(ActionEvent event) {
    if (herramientaLinea.isSelected()) {
        updateToolSelection(ToolType.LINE);
    } else {
        updateToolSelection(ToolType.NONE);
    }
}

@FXML
private void pressedArco(ActionEvent event) {
    if (herramientaArco.isSelected()) {
        updateToolSelection(ToolType.ARC);
        map_scrollpane.setPannable(false); // Desactivar el paneo del mapa
    } else {
        updateToolSelection(ToolType.NONE);
        map_scrollpane.setPannable(true); // Reactivar el paneo del mapa
    }
}

@FXML
private void pressedEliminar(ActionEvent event) {
    if (herramientaEliminar.isSelected()) {
        updateToolSelection(ToolType.DELETE);
        map_scrollpane.setPannable(false);
        zoomGroup.setStyle("-fx-cursor: crosshair;");
    } else {
        updateToolSelection(ToolType.NONE);
        map_scrollpane.setPannable(true);
        zoomGroup.setStyle("-fx-cursor: crosshair;");
    }
}

@FXML
private void pressedTexto(ActionEvent event) {
    if (herramientaTexto.isSelected()) {
        updateToolSelection(ToolType.TEXT);
    } else {
        updateToolSelection(ToolType.NONE);
    }
}
@FXML
private void pressedCirculo(ActionEvent event) {
    if (herramientaCirculo.isSelected()) {
        herramientaCruz.setSelected(false);
        currentShape = PointShape.CIRCLE;
    } else {
        herramientaCirculo.setSelected(true);
    }
}


   @FXML
private void pressedCruz(ActionEvent event) {
    if (herramientaCruz.isSelected()) {
        herramientaCirculo.setSelected(false);
        currentShape = PointShape.CROSS;
    } else {
        herramientaCruz.setSelected(true);
    }
}

    @FXML
    private void pressedLimpiar(ActionEvent event) {
    zoomGroup.getChildren().removeIf(node ->
        node instanceof Arc || 
                node instanceof Circle || 
        node instanceof javafx.scene.shape.Line || 
        node instanceof Group || 
        node instanceof Text        
    );
     map_listview.getItems().clear(); 
    }
    
}
