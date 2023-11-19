package co.edu.uniquindio.agencia.controller;

import co.edu.uniquindio.agencia.app.AgenciaApp;
import co.edu.uniquindio.agencia.exceptions.AtributoIncorrectoException;
import co.edu.uniquindio.agencia.exceptions.AtributosVaciosException;
import co.edu.uniquindio.agencia.exceptions.CalificacionException;
import co.edu.uniquindio.agencia.model.AgenciaViajes;
import co.edu.uniquindio.agencia.model.Cliente;
import co.edu.uniquindio.agencia.model.Destino;
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

public class CalificarDestinosController implements Initializable {

    @FXML
    private Button btnEnviarCalificacion;

    @FXML
    private Button btnRegresar;

    @FXML
    private Button btnVerDestino;

    @FXML
    private TableView<Destino> tableViewDestinos;

    @FXML
    private TableColumn<Destino, String> columnCiudad;

    @FXML
    private TableColumn<Destino, String> columnClima;

    @FXML
    private TableColumn<Destino, String> columnDescripcion;

    @FXML
    private TableColumn<Destino, String> columnNombre;

    @FXML
    private TextArea textAreaComentario;

    @FXML
    private TextField txtCalificacion;

    //Variables auxiliares
    private Stage stage;
    private AgenciaApp agenciaApp;
    private InicioController inicioController;
    private Cliente clienteSesion;
    private ObservableList<Destino> listadoDestinosCliente = FXCollections.observableArrayList();
    private Destino destinoSeleccion;

    //Uso de singleton
    private final AgenciaViajes agenciaViajes = AgenciaViajes.getInstance();

    public void setAgenciaApp(AgenciaApp agenciaApp) {
        this.agenciaApp = agenciaApp;
    }

    public void init(Stage stage, InicioController inicioController, Cliente clienteSesion) {
        this.stage = stage;
        this.inicioController = inicioController;
        this.clienteSesion = clienteSesion;
        //Lista de destinos visitados por el cliente a mostrar
        tableViewDestinos.getItems().clear();
        tableViewDestinos.setItems(obtenerDestinosVisitadosCliente());
    }

    /**
     * Obtiene la lista de destinos visitados por el cliente que inicio sesion
     * @return
     */
    private ObservableList<Destino> obtenerDestinosVisitadosCliente() {
        listadoDestinosCliente.clear();
        ArrayList<Destino> listaDestinosCliente = new ArrayList<>();
        listadoDestinosCliente.addAll(agenciaViajes.obtenerDestinosVisitadosCliente(clienteSesion, LocalDate.now(), listaDestinosCliente, 0));
        return listadoDestinosCliente;
    }

    /**
     * Muestra la ventana de calificaciones destinos
     */
    public void show() {
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Se llenan los campos en la tableView de destinos del cliente
        this.columnNombre.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getNombre()));
        this.columnCiudad.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getCiudad()));
        this.columnClima.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getTipoClima().toString()));
        this.columnDescripcion.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getDescripcion()));
        //Seleccion de destinos en la tabla
        tableViewDestinos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                destinoSeleccion = newSelection;
                destinoSeleccion = tableViewDestinos.getSelectionModel().getSelectedItem();
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
     * Califica un destino seleccionado por el cliente
     * @param event
     */
    @FXML
    void enviarCalificacion(ActionEvent event) {
        try {
            agenciaViajes.calificarDestino(clienteSesion, destinoSeleccion, Double.valueOf(txtCalificacion.getText()), textAreaComentario.getText());
            mostrarMensaje("Agencia", "Calificar Destinos", "La calificación fue realizada con exito", Alert.AlertType.INFORMATION);
            txtCalificacion.clear();
            textAreaComentario.clear();
        } catch (AtributosVaciosException e) {
            mostrarMensaje("Agencia", "Calificar Destinos", e.getMessage(), Alert.AlertType.WARNING);
        } catch (CalificacionException e) {
            mostrarMensaje("Agencia", "Calificar Destinos", e.getMessage(), Alert.AlertType.WARNING);
        } catch (AtributoIncorrectoException e) {
            mostrarMensaje("Agencia", "Calificar Destinos", e.getMessage(), Alert.AlertType.WARNING);
        } catch (NumberFormatException e) {
            mostrarMensaje("Agencia", "Calificar Destinos", "Por favor ingrese un número en la calificación", Alert.AlertType.WARNING);
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
     * Se dirige a la ventana sobre info del destino
     * @param event
     * @throws IOException
     */
    @FXML
    void verDestino(ActionEvent event) throws IOException {
        if (destinoSeleccion != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AgenciaApp.class.getResource("/views/InfoDestinoView.fxml"));
            BorderPane borderPane = (BorderPane) loader.load();
            InfoDestinoController controller = loader.getController();
            controller.setAgenciaApp(agenciaApp);
            Scene scene = new Scene(borderPane);
            Stage stage = new Stage();
            stage.setTitle("Información del Paquete Turístico");
            stage.setScene(scene);
            controller.initCalificacion(stage, this, clienteSesion, destinoSeleccion);
            stage.show();
            this.stage.close();
        } else {
            mostrarMensaje("Agencia", "Calificar Destinos", "Por favor seleccione un destino en la tabla", Alert.AlertType.WARNING);
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