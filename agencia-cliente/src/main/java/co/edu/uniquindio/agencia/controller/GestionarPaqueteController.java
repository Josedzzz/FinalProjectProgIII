package co.edu.uniquindio.agencia.controller;

import co.edu.uniquindio.agencia.app.AgenciaApp;
import co.edu.uniquindio.agencia.exceptions.*;
import co.edu.uniquindio.agencia.model.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GestionarPaqueteController implements Initializable {

    @FXML
    private Button btnActualizarPaquete;

    @FXML
    private Button btnAgregarDestino;

    @FXML
    private Button btnCrearPaquete;

    @FXML
    private Button btnEliminarPaquete;

    @FXML
    private Button btnNuevoPaquete;

    @FXML
    private Button btnRegresar;

    @FXML
    private Button btnVerDestinosDisponibles;

    @FXML
    private Button btnVerDestinosPaquete;

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
    private TableColumn<Destino, String> columnCiudadDestino;

    @FXML
    private TableColumn<Destino, String> columnClimaDestino;

    @FXML
    private TableColumn<PaqueteTuristico, String> columnCupoPaquete;

    @FXML
    private TableColumn<Destino, String> columnDescripcionDestino;

    @FXML
    private TableColumn<PaqueteTuristico, String> columnFechaFinalPaquete;

    @FXML
    private TableColumn<PaqueteTuristico, String> columnFechaInicialPaquete;

    @FXML
    private TableColumn<Destino, String> columnNombreDestino;

    @FXML
    private TableColumn<PaqueteTuristico, String> columnNombrePaquete;

    @FXML
    private TableColumn<PaqueteTuristico, String> columnPrecioPaquete;

    @FXML
    private TableView<Destino> tableViewDestinos;

    @FXML
    private TableView<PaqueteTuristico> tableViewPaquetes;

    @FXML
    private DatePicker dtFechaFinal;

    @FXML
    private DatePicker dtFechaInicial;

    @FXML
    private TextField txtCupoMaximo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPrecio;

    //Variables auxiliares
    private AgenciaApp agenciaApp;
    private Stage stage;
    private GestionarAgenciaController gestionarAgenciaController;
    private Administrador administradorSesion;
    private ObservableList<Destino> listaDestinos = FXCollections.observableArrayList();
    private Destino destinoSeleccion;
    private ObservableList<PaqueteTuristico> listaPaquetes = FXCollections.observableArrayList();
    private PaqueteTuristico paqueteSeleccion;

    //Uso de singleton
    private final AgenciaViajes agenciaViajes = AgenciaViajes.getInstance();

    public void setAgenciaApp(AgenciaApp agenciaApp) {
        this.agenciaApp = agenciaApp;
        //Lista de destinos a mostrar
        tableViewDestinos.getItems().clear();
        tableViewDestinos.setItems(getListaDestinos());
        //Lista de paquetes a mostrar
        tableViewPaquetes.getItems().clear();
        tableViewPaquetes.setItems(getListaPaquetes());
    }

    /**
     * Obtiene la lista de destinos
     * @return lista de destinos como un ObservableList para la tableView de destinos
     */
    private ObservableList<Destino> getListaDestinos() {
        listaDestinos.clear();
        listaDestinos.addAll(agenciaViajes.getListaDestinos());
        return listaDestinos;
    }

    /**
     * Obtiene la lista de paquetes
     * @return lista de paquetes como un ObservableList para la tableView de paquetes
     */
    private ObservableList<PaqueteTuristico> getListaPaquetes() {
        listaPaquetes.clear();
        listaPaquetes.addAll(agenciaViajes.getListaPaquetesTuristicos());
        return listaPaquetes;
    }

    public void init(Stage stage, GestionarAgenciaController gestionarAgenciaController, Administrador administradorSesion) {
        this.stage = stage;
        this.gestionarAgenciaController = gestionarAgenciaController;
        this.administradorSesion = administradorSesion;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Datos en la tableView de paquetes
        this.columnNombrePaquete.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getNombre()));
        this.columnFechaInicialPaquete.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getFechaInicial().toString()));
        this.columnFechaFinalPaquete.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getFechaFinal().toString()));
        this.columnPrecioPaquete.setCellValueFactory(e -> {
            double precio = e.getValue().getPrecio();
            String precioString = String.valueOf(precio);
            return new ReadOnlyStringWrapper(precioString);
        });
        this.columnCupoPaquete.setCellValueFactory(e -> {
            int cupo = e.getValue().getCupoMaximo();
            String cupoString = String.valueOf(cupo);
            return new ReadOnlyStringWrapper(cupoString);
        });
        //Seleccion de paquetes en la tabla
        tableViewPaquetes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                paqueteSeleccion = newSelection;
                paqueteSeleccion = tableViewPaquetes.getSelectionModel().getSelectedItem();
                //Lleno campos de la interfaz con el paquete seleccionado y deshabilito campos
                txtNombre.setDisable(true);
                dtFechaInicial.setDisable(true);
                dtFechaFinal.setDisable(true);
                llenarCamposPaquete(paqueteSeleccion);
            }
        });
        //Para que en el textField de cupoMaximo solo se puedan colocar numeros
        TextFormatter<Integer> textFormatterInt = new TextFormatter<>(new IntegerStringConverter(), 0, change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        });
        txtCupoMaximo.setTextFormatter(textFormatterInt);
        //Para que en el textField de precio solo se puedan colocar numeros
        TextFormatter<Double> textFormatterDouble = new TextFormatter<>(new DoubleStringConverter(), 0.0, change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) {
                return change;
            }
            return null;
        });
        txtPrecio.setTextFormatter(textFormatterDouble);

        //Datos en la tableView de destinos
        this.columnNombreDestino.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getNombre()));
        this.columnCiudadDestino.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getCiudad()));
        this.columnClimaDestino.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getTipoClima().toString()));
        this.columnDescripcionDestino.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getDescripcion()));
        //Seleccion de destinos en la tabla
        tableViewDestinos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                destinoSeleccion = newSelection;
                destinoSeleccion = tableViewDestinos.getSelectionModel().getSelectedItem();
            }
        });
    }

    /**
     * Llena los campos de los texfields con la info del paquete pasado como parametro
     * @param paqueteSeleccion
     */
    private void llenarCamposPaquete(PaqueteTuristico paqueteSeleccion) {
        try {
            txtNombre.setText(paqueteSeleccion.getNombre());
            dtFechaInicial.setValue(paqueteSeleccion.getFechaInicial());
            dtFechaFinal.setValue(paqueteSeleccion.getFechaFinal());
            txtPrecio.setText(String.valueOf(paqueteSeleccion.getPrecio()));
            txtCupoMaximo.setText(String.valueOf(paqueteSeleccion.getCupoMaximo()));
            //Verifico que servicios adicionales tiene el paquete para llenar las checkBox
            checkBoxTransporte.setSelected(agenciaViajes.tieneServicioAdicionalPaquete(paqueteSeleccion, ServicioAdicional.TRANSPORTE, 0));
            checkBoxRecreacion.setSelected(agenciaViajes.tieneServicioAdicionalPaquete(paqueteSeleccion, ServicioAdicional.RECREACION, 0));
            checkBoxSeguros.setSelected(agenciaViajes.tieneServicioAdicionalPaquete(paqueteSeleccion, ServicioAdicional.SEGUROS, 0));
            checkBoxAlimentos.setSelected(agenciaViajes.tieneServicioAdicionalPaquete(paqueteSeleccion, ServicioAdicional.ALIMENTOS, 0));
            checkBoxBar.setSelected(agenciaViajes.tieneServicioAdicionalPaquete(paqueteSeleccion, ServicioAdicional.BAR, 0));
            //Se muestra en la tableView de destinos, los destinos del paquete
            tableViewDestinos.getItems().clear();
            listaDestinos.addAll(agenciaViajes.obtenerDestinosPaquete(paqueteSeleccion));
            tableViewDestinos.setItems(listaDestinos);
            mostrarMensaje("Agencia", "Gestionar Paquetes", "En el apartado de destinos, se muetras los destinos del paquete seleccionado", Alert.AlertType.INFORMATION);
        } catch (AtributosVaciosException e) {
            mostrarMensaje("Agencia", "Gestionar Paquetes", e.getMessage(), Alert.AlertType.INFORMATION);
        } catch (PaqueteTutisticoNoRegistradoException e) {
            mostrarMensaje("Agencia", "Gestionar Paquetes", e.getMessage(), Alert.AlertType.INFORMATION);
        }
    }

    /**
     * Limpia los textFields y checkBox de la interfaz
     */
    private void limpiarCamposPaquete() {
        txtNombre.clear();
        dtFechaInicial.setValue(LocalDate.now());
        dtFechaFinal.setValue(LocalDate.now());
        txtPrecio.clear();
        txtCupoMaximo.clear();
        checkBoxTransporte.setSelected(false);
        checkBoxRecreacion.setSelected(false);
        checkBoxSeguros.setSelected(false);
        checkBoxAlimentos.setSelected(false);
        checkBoxBar.setSelected(false);
    }

    /**
     * Actualiza los datos del paquete
     * @param event
     */
    @FXML
    void actualizarPaquete(ActionEvent event) {
        try {
            agenciaViajes.actualizarPaqueteTuristico(
                    txtNombre.getText(),
                    dtFechaInicial.getValue(),
                    dtFechaFinal.getValue(),
                    Double.parseDouble(txtPrecio.getText()),
                    Integer.parseInt(txtCupoMaximo.getText()),
                    agenciaViajes.obtenerServiciosAdicionalesPaquete(checkBoxTransporte.isSelected(), checkBoxAlimentos.isSelected(), checkBoxSeguros.isSelected(), checkBoxRecreacion.isSelected(), checkBoxBar.isSelected())
            );
            tableViewPaquetes.getItems().clear();
            tableViewPaquetes.setItems(getListaPaquetes());
            mostrarMensaje("Agencia", "Gestionar Paquetes", "El paquete " + txtNombre.getText() + " ha sido actualizado correctamente", Alert.AlertType.INFORMATION);
            limpiarCamposPaquete();
        } catch (PaqueteTutisticoNoRegistradoException e) {
            mostrarMensaje("Agencia", "Gestionar Paquetes", e.getMessage(), Alert.AlertType.WARNING);
        } catch (CampoObligatorioPaqueteTuristicoException e) {
            mostrarMensaje("Agencia", "Gestionar Paquetes", e.getMessage(), Alert.AlertType.WARNING);
        } catch (FechaNoPermitidaException e) {
            mostrarMensaje("Agencia", "Gestionar Paquetes", e.getMessage(), Alert.AlertType.WARNING);
        } catch (NumberFormatException e) {
            mostrarMensaje("Agencia", "Gestionar Paquetes", "Asegurese de ingresar un número en el precio y en el cupo maxímo del paquete", Alert.AlertType.WARNING);
        }
    }

    /**
     * Agrega un destino seleccionado al paquete seleccionado
     * @param event
     */
    @FXML
    void agregarDestino(ActionEvent event) {
        try {
            if (destinoSeleccion != null) {
                agenciaViajes.agregarDestinoPaquete(paqueteSeleccion, destinoSeleccion);
                mostrarMensaje("Agencia", "Gestionar Paquetes", "Se agrega correctamente el destino " + destinoSeleccion.getNombre() + " al paquete " + paqueteSeleccion.getNombre(), Alert.AlertType.INFORMATION);
                limpiarCamposPaquete();
            }
        } catch (PaqueteTutisticoNoRegistradoException e) {
            mostrarMensaje("Agencia", "Gestionar Paquetes", e.getMessage(), Alert.AlertType.WARNING);
        } catch (DestinoNoRegistradoException e) {
            mostrarMensaje("Agencia", "Gestionar Paquetes", e.getMessage(), Alert.AlertType.WARNING);
        } catch (DestinoYaExistenteException e) {
            mostrarMensaje("Agencia", "Gestionar Paquetes", e.getMessage(), Alert.AlertType.WARNING);
        }

    }

    /**
     * Crea un paquete turistico
     * @param event
     */
    @FXML
    void crearPaquete(ActionEvent event) {
        ArrayList<Destino> destinosPaquete = new ArrayList<>();
        try {
            agenciaViajes.crearPaqueteTuristico(
                    txtNombre.getText(),
                    dtFechaInicial.getValue(),
                    dtFechaFinal.getValue(),
                    Double.parseDouble(txtPrecio.getText()),
                    Integer.parseInt(txtCupoMaximo.getText()),
                    agenciaViajes.obtenerServiciosAdicionalesPaquete(checkBoxTransporte.isSelected(), checkBoxAlimentos.isSelected(), checkBoxSeguros.isSelected(), checkBoxRecreacion.isSelected(), checkBoxBar.isSelected()),
                    destinosPaquete
            );
            tableViewPaquetes.getItems().clear();
            tableViewPaquetes.setItems(getListaPaquetes());
            mostrarMensaje("Agencia", "Gestionar Paquetes", "El paquete " + txtNombre.getText() + " ha sido registrado", Alert.AlertType.INFORMATION);
            limpiarCamposPaquete();
        } catch (PaqueteTuristicoYaExistenteException e) {
            mostrarMensaje("Agencia", "Gestionar Paquetes", e.getMessage(), Alert.AlertType.WARNING);
        } catch (CampoObligatorioPaqueteTuristicoException e) {
            mostrarMensaje("Agencia", "Gestionar Paquetes", e.getMessage(), Alert.AlertType.WARNING);
        } catch (FechaNoPermitidaException e) {
            mostrarMensaje("Agencia", "Gestionar Paquetes", e.getMessage(), Alert.AlertType.WARNING);
        } catch (NumberFormatException e) {
            mostrarMensaje("Agencia", "Gestionar Paquetes", "Asegurese de ingresar un número en el precio y en el cupo maxímo del paquete", Alert.AlertType.WARNING);
        }
    }

    /**
     * Elimina el paquete seleccionado
     * @param event
     */
    @FXML
    void eliminarPaquete(ActionEvent event) {
        try {
            if (paqueteSeleccion != null) {
                String nombre = paqueteSeleccion.getNombre();
                agenciaViajes.eliminarPaqueteTuristico(nombre, paqueteSeleccion.getFechaInicial(), paqueteSeleccion.getFechaFinal());
                //Elimina el paquete en la tableView
                tableViewPaquetes.getItems().clear();
                tableViewPaquetes.setItems(getListaPaquetes());
                mostrarMensaje("Agencia", "Gestionar Paquetes", "El paquete " + nombre + " ha sido eliminado correctamente", Alert.AlertType.INFORMATION);
                limpiarCamposPaquete();
            } else {
                mostrarMensaje("Agencia", "Gestionar Paquetes", "Por favor seleccione un paquete en la tabla", Alert.AlertType.WARNING);
            }
        } catch (PaqueteTutisticoNoRegistradoException e) {
            mostrarMensaje("Agencia", "Gestionar Paquetes", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Limpia los campos de la interfaz
     * @param event
     */
    @FXML
    void nuevoPaquete(ActionEvent event) {
        limpiarCamposPaquete();
        txtNombre.setDisable(false);
        dtFechaInicial.setDisable(false);
        dtFechaFinal.setDisable(false);
    }

    /**
     * Regresa a la ventana gestionar agencia
     * @param event
     */
    @FXML
    void regresar(ActionEvent event) {
        this.stage.close();
        gestionarAgenciaController.show();
    }

    /**
     * Actualiza la tabla de destinos con todos los destinos disponibles
     * @param event
     */
    @FXML
    void verDestinosDispobibles(ActionEvent event) {
        tableViewDestinos.getItems().clear();
        tableViewDestinos.setItems(getListaDestinos());
        mostrarMensaje("Agencia", "Gestionar Paquetes", "Se realizo correctamente el filtro en el apartado de destinos para ver los destinos disponibles", Alert.AlertType.INFORMATION);
    }

    /**
     * Actualiza la tabla de destinos con todos los destinos del paquete
     * @param event
     */
    @FXML
    void verDestinosPaquete(ActionEvent event) {
        try {
            //Se muestra en la tableView de destinos, los destinos del paquete
            tableViewDestinos.getItems().clear();
            listaDestinos.addAll(agenciaViajes.obtenerDestinosPaquete(paqueteSeleccion));
            tableViewDestinos.setItems(listaDestinos);
            mostrarMensaje("Agencia", "Gestionar Paquetes", "Se realizo correctamente el filtro en el apartado de destinos para ver los destinos del paquete seleccionado", Alert.AlertType.INFORMATION);
        } catch (AtributosVaciosException e) {
            mostrarMensaje("Agencia", "Gestionar Paquetes", e.getMessage(), Alert.AlertType.WARNING);
        } catch (PaqueteTutisticoNoRegistradoException e) {
            mostrarMensaje("Agencia", "Gestionar Paquetes", e.getMessage(), Alert.AlertType.WARNING);
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
