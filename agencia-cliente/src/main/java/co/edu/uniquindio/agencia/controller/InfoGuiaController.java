package co.edu.uniquindio.agencia.controller;

import co.edu.uniquindio.agencia.app.AgenciaApp;
import co.edu.uniquindio.agencia.model.AgenciaViajes;
import co.edu.uniquindio.agencia.model.CalificacionGuia;
import co.edu.uniquindio.agencia.model.Guia;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class InfoGuiaController implements Initializable {

    @FXML
    private Button btnRegresar;

    @FXML
    private TableView<CalificacionGuia> tableViewCalificaciones;

    @FXML
    private TableColumn<CalificacionGuia, String> columnCalificacion;

    @FXML
    private TableColumn<CalificacionGuia, String> columnComentario;

    @FXML
    private TableColumn<CalificacionGuia, String> columnNombreCliente;

    @FXML
    private TextField txtAniosExperiencia;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTelefono;

    //Variables auxiliares
    private Stage stage;
    private AgenciaApp agenciaApp;
    private HacerReservaController hacerReservaController;
    private ReservasController reservasController;
    private CalificarGuiasController calificarGuiasController;
    private Guia guiaSeleccion;
    private ObservableList<CalificacionGuia> listadoCalificaciones = FXCollections.observableArrayList();

    //Uso de singleton
    private final AgenciaViajes agenciaViajes = AgenciaViajes.getInstance();

    public void setAgenciaApp(AgenciaApp agenciaApp) {
        this.agenciaApp = agenciaApp;
    }

    public void init(Stage stage, HacerReservaController hacerReservaController, Guia guiaSeleccion) {
        this.stage = stage;
        this.hacerReservaController = hacerReservaController;
        this.guiaSeleccion = guiaSeleccion;
        //Lista de calificaciones a mostrar
        tableViewCalificaciones.getItems().clear();
        tableViewCalificaciones.setItems(getListaCalificacionesGuia());
        llenarCamposGuia();
    }

    public void init(Stage stage, ReservasController reservasController, Guia guiaSeleccion) {
        this.stage = stage;
        this.reservasController = reservasController;
        this.guiaSeleccion = guiaSeleccion;
        //Lista de calificaciones a mostrar
        tableViewCalificaciones.getItems().clear();
        tableViewCalificaciones.setItems(getListaCalificacionesGuia());
        llenarCamposGuia();
    }

    public void init(Stage stage, CalificarGuiasController calificarGuiasController, Guia guiaSeleccion) {
        this.stage = stage;
        this.calificarGuiasController = calificarGuiasController;
        this.guiaSeleccion = guiaSeleccion;
        //Lista de calificaciones a mostrar
        tableViewCalificaciones.getItems().clear();
        tableViewCalificaciones.setItems(getListaCalificacionesGuia());
        llenarCamposGuia();
    }

    /**
     * Muestra la informacion del guia
     */
    private void llenarCamposGuia() {
        txtNombre.setText(guiaSeleccion.getNombre());
        txtTelefono.setText(guiaSeleccion.getTelefono());
        txtCorreo.setText(guiaSeleccion.getCorreo());
        txtAniosExperiencia.setText(String.valueOf(guiaSeleccion.getAniosExperiencia()));
    }

    /**
     * Obtiene la lista de calificaciones de un guia
     * @return lista de las calificaciones de un guia como un ObserableLista para las calificaciones en la tableView
     */
    private ObservableList<CalificacionGuia> getListaCalificacionesGuia() {
        listadoCalificaciones.clear();
        listadoCalificaciones.addAll(guiaSeleccion.getCalificaciones());
        return listadoCalificaciones;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Se llenan los campos en la tableView de calificaciones
        this.columnNombreCliente.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getClienteAsociado().getNombre()));
        this.columnComentario.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getComentario()));
        this.columnCalificacion.setCellValueFactory(e -> {
            double calificacion = e.getValue().getCalificacion();
            String calificacionString = String.valueOf(calificacion);
            return new ReadOnlyStringWrapper(calificacionString);
        });
        //Deshabilito los campos de la informacion del guia
        txtNombre.setDisable(true);
        txtTelefono.setDisable(true);
        txtCorreo.setDisable(true);
        txtAniosExperiencia.setDisable(true);
    }

    /**
     * Regresa a la correspondiente dependiendo del controller anterior
     * @param event
     */
    @FXML
    void regresar(ActionEvent event) {
        this.stage.close();
        if (hacerReservaController != null) {
            hacerReservaController.show();
        } else if (reservasController != null) {
            reservasController.show();
        } else {
            calificarGuiasController.show();
        }
    }

}

