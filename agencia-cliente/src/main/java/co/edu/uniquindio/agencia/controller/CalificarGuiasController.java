package co.edu.uniquindio.agencia.controller;

import co.edu.uniquindio.agencia.app.AgenciaApp;
import co.edu.uniquindio.agencia.exceptions.AtributoIncorrectoException;
import co.edu.uniquindio.agencia.exceptions.AtributosVaciosException;
import co.edu.uniquindio.agencia.exceptions.CalificacionException;
import co.edu.uniquindio.agencia.model.AgenciaViajes;
import co.edu.uniquindio.agencia.model.Cliente;
import co.edu.uniquindio.agencia.model.Guia;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CalificarGuiasController implements Initializable {

    @FXML
    private Button btnEnviarCalificacion;

    @FXML
    private Button btnRegresar;

    @FXML
    private Button btnVerGuia;

    @FXML
    private TableView<Guia> tableViewGuias;

    @FXML
    private TableColumn<Guia, String> columnAniosExperiencia;

    @FXML
    private TableColumn<Guia, String> columnCorreo;

    @FXML
    private TableColumn<Guia, String> columnNombre;

    @FXML
    private TableColumn<Guia, String> columnTelefono;

    @FXML
    private TextArea textAreaComentario;

    @FXML
    private TextField txtCalificacion;

    //Variables auxiliares
    private Stage stage;
    private AgenciaApp agenciaApp;
    private InicioController inicioController;
    private Cliente clienteSesion;
    private ObservableList<Guia> listadoGuiasCliente = FXCollections.observableArrayList();
    private Guia guiaSeleccion;

    //Uso de singleton
    private final AgenciaViajes agenciaViajes = AgenciaViajes.getInstance();

    public void setAgenciaApp(AgenciaApp agenciaApp) {
        this.agenciaApp = agenciaApp;
    }

    public void init(Stage stage, InicioController inicioController, Cliente clienteSesion) {
        this.stage = stage;
        this.inicioController = inicioController;
        this.clienteSesion = clienteSesion;
        //Lista de guias contratados por el cliente a mostrar
        tableViewGuias.getItems().clear();
        tableViewGuias.setItems(obtenerGuiasCliente());
    }

    /**
     * Obtiene la lista de guias contratados por el cliente
     * @return
     */
    private ObservableList<Guia> obtenerGuiasCliente() {
        listadoGuiasCliente.clear();
        ArrayList<Guia> listaGuiasCliente = new ArrayList<>();
        listadoGuiasCliente.addAll(agenciaViajes.obtenerGuiasCliente(clienteSesion, LocalDate.now(), listaGuiasCliente, 0));
        return listadoGuiasCliente;
    }

    /**
     * Muestra la ventana de calificaciones de guia
     */
    public void show() {
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Se llenan los campos en la tableView de guias cliente
        this.columnNombre.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getNombre()));
        this.columnCorreo.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getCorreo()));
        this.columnTelefono.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getTelefono()));
        this.columnAniosExperiencia.setCellValueFactory(e -> {
            int anios = e.getValue().getAniosExperiencia();
            String aniosString = String.valueOf(anios);
            return new ReadOnlyStringWrapper(aniosString);
        });
        //Seleccion de guias en la tabla
        tableViewGuias.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                guiaSeleccion = newSelection;
                guiaSeleccion = tableViewGuias.getSelectionModel().getSelectedItem();
            }
        });
        //Para que en el textField de calificacion solo se puedan colocar numeros
        TextFormatter<Double> textFormatterDouble = new TextFormatter<>(new DoubleStringConverter(), 0.0, change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) {
                return change;
            }
            return null;
        });
        txtCalificacion.setTextFormatter(textFormatterDouble);
    }

    /**
     * Califica un guia seleccionado por el cliente
     * @param event
     */
    @FXML
    void enviarCalificacion(ActionEvent event) {
        try {
            agenciaViajes.calificarGuia(clienteSesion, guiaSeleccion, Double.valueOf(txtCalificacion.getText()), textAreaComentario.getText());
            mostrarMensaje("Agencia", "Calificar Guías", "La calificación fue realizada con exito", Alert.AlertType.INFORMATION);
            txtCalificacion.clear();
            textAreaComentario.clear();
        } catch (AtributosVaciosException e) {
            mostrarMensaje("Agencia", "Calificar Guías", e.getMessage(), Alert.AlertType.WARNING);
        } catch (CalificacionException e) {
            mostrarMensaje("Agencia", "Calificar Guías", e.getMessage(), Alert.AlertType.WARNING);
        } catch (AtributoIncorrectoException e) {
            mostrarMensaje("Agencia", "Calificar Guías", e.getMessage(), Alert.AlertType.WARNING);
        } catch (NumberFormatException e) {
            mostrarMensaje("Agencia", "Calificar Guías", "Por favor ingrese un número en la calificación", Alert.AlertType.WARNING);
        }
    }

    /**
     * Regresa a la ventana de inicio
     * @param event
     */
    @FXML
    void regresar(ActionEvent event) {
        this.stage.close();
        inicioController.show();
    }

    /**
     * Se dirige a la ventana de informacion del guia
     * @param event
     * @throws IOException
     */
    @FXML
    void verGuia(ActionEvent event) throws IOException {
        if (guiaSeleccion != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AgenciaApp.class.getResource("/views/InfoGuiaView.fxml"));
            BorderPane borderPane = (BorderPane) loader.load();
            InfoGuiaController controller = loader.getController();
            controller.setAgenciaApp(agenciaApp);
            Scene scene = new Scene(borderPane);
            Stage stage = new Stage();
            stage.setTitle("Información del Guía");
            stage.setScene(scene);
            controller.init(stage, this, guiaSeleccion);
            stage.show();
            this.stage.close();
        } else {
            mostrarMensaje("Agencia", "Calificar Guías", "Por favor seleccione un guía en la tabla", Alert.AlertType.WARNING);
        }
    }

    /**
     * Muestra un mensaje dependiendo el tipo de alerta seleccionado
     * @param title
     * @param header
     * @param content
     * @param alertType
     */
    private void mostrarMensaje(String title, String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
