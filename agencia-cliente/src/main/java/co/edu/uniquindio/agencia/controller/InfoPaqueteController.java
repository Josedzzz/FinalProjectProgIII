package co.edu.uniquindio.agencia.controller;

import co.edu.uniquindio.agencia.app.AgenciaApp;
import co.edu.uniquindio.agencia.model.*;
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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InfoPaqueteController implements Initializable {

    @FXML
    private Button btnRegresar;

    @FXML
    private Button btnReservarPaquete;

    @FXML
    private Button btnVerDestino;

    @FXML
    private CheckBox checkBoxAlimentos;

    @FXML
    private CheckBox checkBoxBar;

    @FXML
    private CheckBox checkBoxRecreacion;

    @FXML
    private CheckBox checkBoxSeguros;

    @FXML
    private CheckBox checkBoxTransporte;

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
    private DatePicker dpFechaFinal;

    @FXML
    private DatePicker dpFechaInicial;

    @FXML
    private TextField txtCupos;

    @FXML
    private TextField txtNombrePaquete;

    @FXML
    private TextField txtPrecio;

    //Variables auxiliares
    private Stage stage;
    private AgenciaApp agenciaApp;
    private BuscadorPaquetesController buscadorPaquetesController;
    private ReservasController reservasController;
    private Cliente clienteSesion;
    private PaqueteTuristico paqueteSeleccion;
    private ObservableList<Destino> listadoDestinos = FXCollections.observableArrayList();
    private Destino destinoSeleccion;

    //Uso de singleton
    private final AgenciaViajes agenciaViajes = AgenciaViajes.getInstance();

    public void setAgenciaApp(AgenciaApp agenciaApp) {
        this.agenciaApp = agenciaApp;
    }

    public void init(Stage stage, BuscadorPaquetesController buscadorPaquetesController, Cliente clienteSesion, PaqueteTuristico paqueteSeleccion) {
        this.stage = stage;
        this.buscadorPaquetesController = buscadorPaquetesController;
        this.clienteSesion = clienteSesion;
        this.paqueteSeleccion = paqueteSeleccion;
        llenarCamposPaquete();
        //Lista de destinos del paquete a mostrar
        tableViewDestinos.getItems().clear();
        tableViewDestinos.setItems(getListaDestinosPaquete());
    }

    public void init(Stage stage, ReservasController reservasController, Cliente clienteSesion, PaqueteTuristico paqueteSeleccion) {
        this.stage = stage;
        this.reservasController = reservasController;
        this.clienteSesion = clienteSesion;
        this.paqueteSeleccion = paqueteSeleccion;
        llenarCamposPaquete();
        //Lista de destinos del paquete a mostrar
        tableViewDestinos.getItems().clear();
        tableViewDestinos.setItems(getListaDestinosPaquete());
    }

    /**
     * Muestra la ventana info del paquete
     */
    public void show() {
        stage.show();
    }

    /**
     * Muestra la informacion del paquete turistico
     */
    private void llenarCamposPaquete() {
        txtNombrePaquete.setText(paqueteSeleccion.getNombre());
        dpFechaInicial.setValue(paqueteSeleccion.getFechaInicial());
        dpFechaFinal.setValue(paqueteSeleccion.getFechaFinal());
        txtCupos.setText(String.valueOf(paqueteSeleccion.getCupoDisponible()));
        txtPrecio.setText(String.valueOf(paqueteSeleccion.getPrecio()));
        //Verifico los servicios adicionales del paquete para llenar los checkBox
        checkBoxTransporte.setSelected(agenciaViajes.tieneServicioAdicionalPaquete(paqueteSeleccion, ServicioAdicional.TRANSPORTE, 0));
        checkBoxRecreacion.setSelected(agenciaViajes.tieneServicioAdicionalPaquete(paqueteSeleccion, ServicioAdicional.RECREACION, 0));
        checkBoxSeguros.setSelected(agenciaViajes.tieneServicioAdicionalPaquete(paqueteSeleccion, ServicioAdicional.SEGUROS, 0));
        checkBoxAlimentos.setSelected(agenciaViajes.tieneServicioAdicionalPaquete(paqueteSeleccion, ServicioAdicional.ALIMENTOS, 0));
        checkBoxBar.setSelected(agenciaViajes.tieneServicioAdicionalPaquete(paqueteSeleccion, ServicioAdicional.BAR, 0));
    }

    /**
     * Obtiene la lista de destinos al paquete que se va a observar
     * @return lista de destinos como un ObservableList para la tableView
     */
    private ObservableList<Destino> getListaDestinosPaquete() {
        listadoDestinos.clear();
        listadoDestinos.addAll(paqueteSeleccion.getListaDestinos());
        return listadoDestinos;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Se llenan los campos en la tableView de destinos del paquete
        this.columnNombre.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getNombre()));
        this.columnCiudad.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getCiudad()));
        this.columnClima.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getTipoClima().toString()));
        this.columnDescripcion.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getDescripcion()));
        //Seleccion de destinos en la tableView de destinos
        tableViewDestinos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                destinoSeleccion = newSelection;
                destinoSeleccion = tableViewDestinos.getSelectionModel().getSelectedItem();
            }
        });
        //Deshabilito campos
        txtNombrePaquete.setDisable(true);
        dpFechaInicial.setDisable(true);
        dpFechaFinal.setDisable(true);
        txtCupos.setDisable(true);
        txtPrecio.setDisable(true);
        checkBoxTransporte.setDisable(true);
        checkBoxBar.setDisable(true);
        checkBoxAlimentos.setDisable(true);
        checkBoxSeguros.setDisable(true);
        checkBoxRecreacion.setDisable(true);
    }

    /**
     * Regresa a la ventana de buscador de paquetes
     * @param event
     */
    @FXML
    void regresar(ActionEvent event) {
        this.stage.close();
        if (buscadorPaquetesController == null) {
            reservasController.show();
        } else {
            buscadorPaquetesController.show();
        }
    }

    /**
     * Lleva a la ventana para haceer la reserva del paquete turistico seleccionado
     * @param event
     * @throws IOException
     */
    @FXML
    void reservarPaquete(ActionEvent event) throws IOException {
        if (paqueteSeleccion != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AgenciaApp.class.getResource("/views/HacerReservaView.fxml"));
            BorderPane borderPane = (BorderPane) loader.load();
            HacerReservaController controller = loader.getController();
            controller.setAgenciaApp(agenciaApp);
            Scene scene = new Scene(borderPane);
            Stage stage = new Stage();
            stage.setTitle("Hacer Reserva");
            stage.setScene(scene);
            controller.init(stage, this, clienteSesion, paqueteSeleccion);
            stage.show();
            this.stage.close();
        } else {
            mostrarMensaje("Agencia", "Información del Paquete", "Ocurrió un error con el paquete", Alert.AlertType.WARNING);
        }
    }

    /**
     * Muestra el destino seleccionado en la ventana de info destinos
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
            stage.setTitle("Información del destino");
            stage.setScene(scene);
            controller.initPaquete(stage, this, clienteSesion, destinoSeleccion);
            stage.show();
            this.stage.close();
        } else {
            mostrarMensaje("Agencia", "Información del Paquete", "Por favor seleccione un destino en la tabla", Alert.AlertType.WARNING);
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
