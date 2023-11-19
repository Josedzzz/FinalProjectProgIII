package co.edu.uniquindio.agencia.controller;

import co.edu.uniquindio.agencia.app.AgenciaApp;
import co.edu.uniquindio.agencia.exceptions.AtributosVaciosException;
import co.edu.uniquindio.agencia.exceptions.EstadoReservaException;
import co.edu.uniquindio.agencia.model.AgenciaViajes;
import co.edu.uniquindio.agencia.model.Cliente;
import co.edu.uniquindio.agencia.model.Guia;
import co.edu.uniquindio.agencia.model.Reserva;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ReservasController implements Initializable {

    @FXML
    private Button btnAceptarReserva;

    @FXML
    private Button btnCancelarReserva;

    @FXML
    private Button btnRegresar;

    @FXML
    private Button btnVerGuia;

    @FXML
    private Button btnVerPaqueteTuristico;

    @FXML
    private TableView<Reserva> tableViewReservas;

    @FXML
    private TableColumn<Reserva, String> columnCedula;

    @FXML
    private TableColumn<Reserva, String> columnCupos;

    @FXML
    private TableColumn<Reserva, String> columnEstadoReserva;

    @FXML
    private TableColumn<Reserva, String> columnFechaReserva;

    @FXML
    private TableColumn<Reserva, String> columnGuia;

    @FXML
    private TableColumn<Reserva, String> columnPaqueteTuristico;

    //Variables auxiliares
    private Stage stage;
    private AgenciaApp agenciaApp;
    private InicioController inicioController;
    private Cliente clienteSesion;
    private ObservableList<Reserva> listadoReservasCliente = FXCollections.observableArrayList();
    private Reserva reservaSeleccion;

    //Uso de singleton
    private final AgenciaViajes agenciaViajes = AgenciaViajes.getInstance();

    public void setAgenciaApp(AgenciaApp agenciaApp) {
        this.agenciaApp = agenciaApp;
    }

    public void init(Stage stage, InicioController inicioController, Cliente clienteSesion) {
        this.stage = stage;
        this.inicioController = inicioController;
        this.clienteSesion = clienteSesion;
        //Lista de reservas del cliente a mostrar
        tableViewReservas.getItems().clear();
        tableViewReservas.setItems(getListaReservasCliente());
    }

    /**
     * Obtiene la lista de reservas pendientes del cliente
     * @return lista de reservas del cliente como un observableList para la tableView
     */
    private ObservableList<Reserva> getListaReservasCliente() {
        listadoReservasCliente.clear();
        ArrayList<Reserva> listaReservasCliente = new ArrayList<>();
        listadoReservasCliente.addAll(agenciaViajes.obtenerReservasPendientesCliente(clienteSesion, LocalDate.now(), listaReservasCliente, 0));
        return listadoReservasCliente;
    }

    /**
     * Muestra la ventana de reservas
     */
    public void show() {
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Se llenan los campos en la tableView de reservas
        this.columnCedula.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getClienteInvolucrado().getId()));
        this.columnFechaReserva.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getFechaReserva().toString()));
        this.columnEstadoReserva.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getEstadoReserva().toString()));
        this.columnPaqueteTuristico.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getPaqueteTuristicoSeleccionado().getNombre()));
        this.columnCupos.setCellValueFactory(e -> {
            int cupos = e.getValue().getCantidadPersonas();
            String cuposString = String.valueOf(cupos);
            return new ReadOnlyStringWrapper(cuposString);
        });
        //this.columnGuia.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getGuia().getNombre()));
        this.columnGuia.setCellValueFactory(e -> {
            Guia guia = e.getValue().getGuia();
            if (guia != null) {
                return new ReadOnlyStringWrapper(guia.getNombre());
            } else {
                return new ReadOnlyStringWrapper("Sin guía");
            }
        });
        //Seleccion de reservas en la tabla
        tableViewReservas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                reservaSeleccion = newSelection;
                reservaSeleccion = tableViewReservas.getSelectionModel().getSelectedItem();
            }
        });
    }

    /**
     * Acepta una reserva
     * @param event
     */
    @FXML
    void aceptarReserva(ActionEvent event) {
        try {
            agenciaViajes.confirmarReserva(reservaSeleccion);
            tableViewReservas.refresh();
            mostrarMensaje("Agencia", "Reservas", "La reserva fue confirmada de manera correcta", Alert.AlertType.INFORMATION);
        } catch (EstadoReservaException e) {
            mostrarMensaje("Agencia", "Reservas", e.getMessage(), Alert.AlertType.WARNING);
        } catch (AtributosVaciosException e) {
            mostrarMensaje("Agencia", "Reservas", e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    /**
     * Cancela una reserva
     * @param event
     */
    @FXML
    void cancelarReserva(ActionEvent event) {
        try {
            agenciaViajes.cancelarReserva(reservaSeleccion);
            tableViewReservas.refresh();
            mostrarMensaje("Agencia", "Reservas", "La reserva fue cancelada de manera correcta", Alert.AlertType.INFORMATION);
        } catch (EstadoReservaException e) {
            mostrarMensaje("Agencia", "Reservas", e.getMessage(), Alert.AlertType.WARNING);
        } catch (AtributosVaciosException e) {
            mostrarMensaje("Agencia", "Reservas", e.getMessage(), Alert.AlertType.WARNING);
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
     * Muestra el guia de la reserva seleccionada (si la reserva contiene guia)
     * @param event
     */
    @FXML
    void verGuia(ActionEvent event) {
        try {
            if (agenciaViajes.reservaContieneGuia(reservaSeleccion)) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(AgenciaApp.class.getResource("/views/InfoGuiaView.fxml"));
                BorderPane borderPane = (BorderPane) loader.load();
                InfoGuiaController controller = loader.getController();
                controller.setAgenciaApp(agenciaApp);
                Scene scene = new Scene(borderPane);
                Stage stage = new Stage();
                stage.setTitle("Información del Guía");
                stage.setScene(scene);
                controller.init(stage, this, reservaSeleccion.getGuia());
                stage.show();
                this.stage.close();
            } else {
                mostrarMensaje("Agencia", "Reservas", "La reserva no contiene ningún guía que mostrar", Alert.AlertType.INFORMATION);
            }
        } catch (AtributosVaciosException e) {
            mostrarMensaje("Agencia", "Reservas", e.getMessage(), Alert.AlertType.WARNING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Lleva a la ventana para ver la informacion de un paquete
     * @param event
     * @throws IOException
     */
    @FXML
    void verPaqueteTuristico(ActionEvent event) throws IOException {
        if (reservaSeleccion != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AgenciaApp.class.getResource("/views/InfoPaqueteView.fxml"));
            BorderPane borderPane = (BorderPane) loader.load();
            InfoPaqueteController controller = loader.getController();
            controller.setAgenciaApp(agenciaApp);
            Scene scene = new Scene(borderPane);
            Stage stage = new Stage();
            stage.setTitle("Información del Paquete Turístico");
            stage.setScene(scene);
            controller.init(stage, this, clienteSesion, reservaSeleccion.getPaqueteTuristicoSeleccionado());
            stage.show();
            this.stage.close();
        } else {
            mostrarMensaje("Agencia", "Reservas", "Por favor seleccione una reserva", Alert.AlertType.WARNING);
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