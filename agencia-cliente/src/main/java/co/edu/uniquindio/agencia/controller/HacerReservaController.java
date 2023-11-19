package co.edu.uniquindio.agencia.controller;

import co.edu.uniquindio.agencia.app.AgenciaApp;
import co.edu.uniquindio.agencia.exceptions.AtributoIncorrectoException;
import co.edu.uniquindio.agencia.exceptions.AtributosVaciosException;
import co.edu.uniquindio.agencia.exceptions.FechaNoPermitidaException;
import co.edu.uniquindio.agencia.model.AgenciaViajes;
import co.edu.uniquindio.agencia.model.Cliente;
import co.edu.uniquindio.agencia.model.Guia;
import co.edu.uniquindio.agencia.model.PaqueteTuristico;
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
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class HacerReservaController implements Initializable {

    @FXML
    private Button btnHacerReserva;

    @FXML
    private Button btnRegresar;

    @FXML
    private Button btnVerGuiaSeleccionado;

    @FXML
    private Button btnVerPrecio;

    @FXML
    private CheckBox checkBoxGguia;

    @FXML
    private TableView<Guia> tableViewGuia;

    @FXML
    private TableColumn<Guia, String> columnAniosExperiencia;

    @FXML
    private TableColumn<Guia, String> columnCorreoGuia;

    @FXML
    private TableColumn<Guia, String> columnNombreGuia;

    @FXML
    private TableColumn<Guia, String> columnTelefonoGuia;

    @FXML
    private TextField txtCantidadPersonas;

    @FXML
    private TextField txtPrecio;

    //Variables auxiliares
    private Stage stage;
    private AgenciaApp agenciaApp;
    private InfoPaqueteController infoPaqueteController;
    private Cliente clienteSesion;
    private PaqueteTuristico paqueteSeleccion;
    private ObservableList<Guia> listadoGuias = FXCollections.observableArrayList();
    private Guia guiaSeleccion;

    //Uso de singleton
    private final AgenciaViajes agenciaViajes = AgenciaViajes.getInstance();

    public void setAgenciaApp(AgenciaApp agenciaApp) {
        this.agenciaApp = agenciaApp;
        //Lista de guias a mostrar
        tableViewGuia.getItems().clear();
        tableViewGuia.setItems(getListaGuias());
    }

    /**
     * Obtiene la lista de guias
     * @return lista de guias como un ObservableList para la tableView de guias
     */
    private ObservableList<Guia> getListaGuias() {
        listadoGuias.clear();
        listadoGuias.addAll(agenciaViajes.getListaGuiasTuristicos());
        return listadoGuias;
    }

    public void init(Stage stage, InfoPaqueteController infoPaqueteController, Cliente clienteSesion, PaqueteTuristico paqueteSeleccion) {
        this.stage = stage;
        this.infoPaqueteController = infoPaqueteController;
        this.clienteSesion = clienteSesion;
        this.paqueteSeleccion = paqueteSeleccion;
    }

    /**
     * Muestra la ventana para hacer reserva
     */
    public void show() {
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Datos en la tableView de guias
        this.columnNombreGuia.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getNombre()));
        this.columnCorreoGuia.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getCorreo()));
        this.columnTelefonoGuia.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getTelefono()));
        this.columnAniosExperiencia.setCellValueFactory(e -> {
            int anios = e.getValue().getAniosExperiencia();
            String aniosString = String.valueOf(anios);
            return new ReadOnlyStringWrapper(aniosString);
        });
        //Seleccion de guias en la tabla
        tableViewGuia.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                guiaSeleccion = newSelection;
                guiaSeleccion = tableViewGuia.getSelectionModel().getSelectedItem();
            }
        });
        //Para que en el texfield de precio y de cantidad de personas solo se pueden colocar numeros
        TextFormatter<Integer> textFormatterInt = new TextFormatter<>(new IntegerStringConverter(), 0, change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        });
        TextFormatter<Double> textFormatterDouble = new TextFormatter<>(new DoubleStringConverter(), 0.0, change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) {
                return change;
            }
            return null;
        });
        txtCantidadPersonas.setTextFormatter(textFormatterInt);
        txtPrecio.setTextFormatter(textFormatterDouble);
        txtPrecio.setDisable(true);
        //Deshabilito la tabla de los guias y el boton para ver informacion sobre el guia
        tableViewGuia.setVisible(false);
        btnVerGuiaSeleccionado.setVisible(false);
    }

    /**
     * Habilita la tableView de los guias y el boton de informacion sobre el guia
     * @param event
     */
    @FXML
    void aniadirGuiaTuristico(ActionEvent event) {
        tableViewGuia.setVisible(true);
        btnVerGuiaSeleccionado.setVisible(true);
        mostrarMensaje("Agencia", "Hacer Reserva", "Seleccione el guía turístico de la tabla que desea añadir para su reserva", Alert.AlertType.INFORMATION);
    }

    /**
     * Se hace una reserva teniendo en cuenta el cliente que inicio sesion y el paquete seleccionado
     * @param event
     */
    @FXML
    void hacerReserva(ActionEvent event) {
        try {
            agenciaViajes.crearReserva(
                    LocalDate.now(),
                    paqueteSeleccion.getFechaInicial(),
                    Integer.valueOf(txtCantidadPersonas.getText()),
                    clienteSesion,
                    paqueteSeleccion,
                    guiaSeleccion
            );
            limpiarCampos();
            mostrarMensaje("Agencia", "Hacer Reserva", "La reserva fue realizada de manera correcta. Dirigete desde el menú principal a la ventana de ver destinos para confirmar tu reserva", Alert.AlertType.INFORMATION);
        } catch (AtributoIncorrectoException e) {
            mostrarMensaje("Agencia", "Hacer Reserva", e.getMessage(), Alert.AlertType.WARNING);
        } catch (AtributosVaciosException e) {
            mostrarMensaje("Agencia", "Hacer Reserva", e.getMessage(), Alert.AlertType.WARNING);
        } catch (FechaNoPermitidaException e) {
            mostrarMensaje("Agencia", "Hacer Reserva", e.getMessage(), Alert.AlertType.WARNING);
        } catch (NumberFormatException e) {
            mostrarMensaje("Agencia", "Hacer Reserva", "Por favor ingresa un número en la cantidad de personas", Alert.AlertType.WARNING);
        }
    }

    /**
     * Limpia todos los campos de la interfaz
     */
    private void limpiarCampos() {
        txtCantidadPersonas.setText("0");
        txtPrecio.clear();
        checkBoxGguia.setSelected(false);
        btnVerGuiaSeleccionado.setVisible(false);
        tableViewGuia.setVisible(false);
    }

    /**
     * Regresa a la ventana de buscador de paquetes
     * @param event
     */
    @FXML
    void regresar(ActionEvent event) {
        this.stage.close();
        infoPaqueteController.show();
    }

    /**
     * Muestra el guia seleccionado
     * @param event
     * @throws IOException
     */
    @FXML
    void verGuiaSeleccionado(ActionEvent event) throws IOException {
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
            mostrarMensaje("Agencia", "Hacer Reserva", "Por favor seleccione un guía", Alert.AlertType.WARNING);
        }
    }

    /**
     * Calcula el precio para una reserva teniendo en cuenta la cantidad de personas
     * @param event
     */
    @FXML
    void verPrecio(ActionEvent event) {
        try {
            double precio = agenciaViajes.calcularPrecioReserva(paqueteSeleccion, Integer.valueOf(txtCantidadPersonas.getText()));
            txtPrecio.setText(String.valueOf(precio));
            mostrarMensaje("Agencia", "Hacer Reserva", "Se generó el precio correctamente", Alert.AlertType.INFORMATION);
        } catch (AtributoIncorrectoException e) {
            mostrarMensaje("Agencia", "Hacer Reserva", e.getMessage(), Alert.AlertType.WARNING);
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
