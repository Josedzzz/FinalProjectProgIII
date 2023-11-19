package co.edu.uniquindio.agencia.controller;

import co.edu.uniquindio.agencia.app.AgenciaApp;
import co.edu.uniquindio.agencia.exceptions.ClienteNoRegistradoException;
import co.edu.uniquindio.agencia.model.AgenciaViajes;
import co.edu.uniquindio.agencia.model.Cliente;
import co.edu.uniquindio.agencia.model.Destino;
import co.edu.uniquindio.agencia.model.TipoDestino;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BuscadorDestinosController implements Initializable {

    @FXML
    private Button btnRecomendaciones;

    @FXML
    private Button btnRegresar;

    @FXML
    private Button btnVerDestino;

    @FXML
    private RadioButton rbAventura;

    @FXML
    private RadioButton rbBosque;

    @FXML
    private RadioButton rbCiudad;

    @FXML
    private RadioButton rbPlaya;

    @FXML
    private TableColumn<Destino, String> columnCiudad;

    @FXML
    private TableColumn<Destino, String> columnClima;

    @FXML
    private TableColumn<Destino, String> columnDescripcion;

    @FXML
    private TableColumn<Destino, String> columnNombre;

    @FXML
    private TableView<Destino> tableViewDestinos;

    //Variables auxiliares
    private Stage stage;
    private AgenciaApp agenciaApp;
    private InicioController inicioController;
    private Cliente clienteSesion;
    private ObservableList<Destino> listadoDestinos = FXCollections.observableArrayList();
    private Destino destinoSeleccion;

    //Uso de singleton
    private final AgenciaViajes agenciaViajes = AgenciaViajes.getInstance();

    public void setAgenciaApp(AgenciaApp agenciaApp) {
        this.agenciaApp = agenciaApp;
        //Lista de destinos a mostrar
        tableViewDestinos.getItems().clear();
        tableViewDestinos.setItems(getListaDestinos());
    }

    /**
     * Obtiene la lista de destinos
     *
     * @return la lista de destinos como una observableList para la tableView
     */
    private ObservableList<Destino> getListaDestinos() {
        listadoDestinos.clear();
        listadoDestinos.addAll(agenciaViajes.getListaDestinos());
        return listadoDestinos;
    }

    /**
     * Obtiene la lista de destinos de un tipo en especifico
     *
     * @param tipoDestino
     * @return
     */
    private ObservableList<Destino> getListadoDestinosTipo(TipoDestino tipoDestino) {
        listadoDestinos.clear();
        ArrayList<Destino> listaDestinosTipo = new ArrayList<>();
        listadoDestinos.addAll(agenciaViajes.obtenerDestinosTipo(tipoDestino, listaDestinosTipo, 0));
        return listadoDestinos;
    }

    /**
     * Obtiene la lista de destinos recomendados para un cliente
     * @return
     */
    public ObservableList<Destino> getListadoDestinosRecomendados() {
        try {
            listadoDestinos.clear();
            listadoDestinos.addAll(agenciaViajes.recomendarDestinosCLiente(clienteSesion, 0, 0, 0, 0, 0));
        } catch (ClienteNoRegistradoException e) {
            mostrarMensaje("Agencia", "Buscador de Destinos", e.getMessage(), Alert.AlertType.WARNING);
        }
        return listadoDestinos;
    }

    public void init(Stage stage, InicioController inicioController, Cliente clienteSesion) {
        this.stage = stage;
        this.inicioController = inicioController;
        this.clienteSesion = clienteSesion;
    }

    /**
     * Muestra la ventana de buscador de destinos
     */
    public void show() {
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Datos en la tableView de destinos
        this.columnNombre.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getNombre()));
        this.columnCiudad.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getCiudad()));
        this.columnClima.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getTipoClima().toString()));
        this.columnDescripcion.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getDescripcion()));
        //Seleccion de destinos en la tableView
        tableViewDestinos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                destinoSeleccion = newSelection;
                destinoSeleccion = tableViewDestinos.getSelectionModel().getSelectedItem();
            }
        });
        //Manejo de los radioButtoms
        ToggleGroup group = new ToggleGroup();
        rbAventura.setToggleGroup(group);
        rbBosque.setToggleGroup(group);
        rbPlaya.setToggleGroup(group);
        rbCiudad.setToggleGroup(group);
    }

    /**
     * Filtra los destinos de tipo aventura para verlos en la tabla
     *
     * @param event
     */
    @FXML
    void filtrarDestinoAventura(ActionEvent event) {
        tableViewDestinos.getItems().clear();
        tableViewDestinos.setItems(getListadoDestinosTipo(TipoDestino.AVENTURA));
        mostrarMensaje("Agencia", "Buscador de Destinos", "En la tabla se observan los destinos de tipo Aventura", Alert.AlertType.INFORMATION);
    }

    /**
     * Filtra los destinos de tipo bosque para verlos en la tabla
     *
     * @param event
     */
    @FXML
    void filtrarDestinoBosque(ActionEvent event) {
        tableViewDestinos.getItems().clear();
        tableViewDestinos.setItems(getListadoDestinosTipo(TipoDestino.BOSQUE));
        mostrarMensaje("Agencia", "Buscador de Destinos", "En la tabla se observan los destinos de tipo Bosque", Alert.AlertType.INFORMATION);
    }

    /**
     * Filtra los destinos de tipo ciudad para verlos en la tabla
     *
     * @param event
     */
    @FXML
    void filtrarDestinoCiudad(ActionEvent event) {
        tableViewDestinos.getItems().clear();
        tableViewDestinos.setItems(getListadoDestinosTipo(TipoDestino.CIUDAD));
        mostrarMensaje("Agencia", "Buscador de Destinos", "En la tabla se observan los destinos de tipo Ciudad", Alert.AlertType.INFORMATION);
    }

    /**
     * Filtra los destinos de tipo playa para verlos en la tabla
     *
     * @param event
     */
    @FXML
    void filtrarDestinoPlaya(ActionEvent event) {
        tableViewDestinos.getItems().clear();
        tableViewDestinos.setItems(getListadoDestinosTipo(TipoDestino.PLAYA));
        mostrarMensaje("Agencia", "Buscador de Destinos", "En la tabla se observan los destinos de tipo Playa", Alert.AlertType.INFORMATION);
    }

    /**
     * Muestra el destino seleccionado en la ventana de infoDestinos
     *
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
            controller.init(stage, this, clienteSesion, destinoSeleccion);
            stage.show();
            this.stage.close();
        } else {
            mostrarMensaje("Agencia", "Buscador de Destinos", "Por favor seleccione un destino", Alert.AlertType.WARNING);
        }
    }

    /**
     * Regresa a la ventana de inicio
     *
     * @param event
     */
    @FXML
    void regresar(ActionEvent event) {
        this.stage.close();
        inicioController.show();
    }

    /**
     * Muestra las recomendaciones de ddestinos para un cliente en especifico
     * @param event
     */
    @FXML
    void verRecomendaciones(ActionEvent event) {
        tableViewDestinos.getItems().clear();
        tableViewDestinos.setItems(getListadoDestinosRecomendados());
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
