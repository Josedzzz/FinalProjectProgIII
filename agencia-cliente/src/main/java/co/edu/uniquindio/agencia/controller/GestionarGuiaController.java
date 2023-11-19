package co.edu.uniquindio.agencia.controller;

import co.edu.uniquindio.agencia.app.AgenciaApp;
import co.edu.uniquindio.agencia.exceptions.CampoObligatorioGuiaException;
import co.edu.uniquindio.agencia.exceptions.GuiaNoRegistradoException;
import co.edu.uniquindio.agencia.exceptions.GuiaYaExistenteException;
import co.edu.uniquindio.agencia.model.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GestionarGuiaController implements Initializable {

    @FXML
    private Button btnActualizarGuia;

    @FXML
    private Button btnCrearGuia;

    @FXML
    private Button btnEliminarGuia;

    @FXML
    private Button btnNuevoGuia;

    @FXML
    private Button btnRegresar;

    @FXML
    private CheckBox checkBoxEspaniol;

    @FXML
    private CheckBox checkBoxFrances;

    @FXML
    private CheckBox checkBoxIngles;

    @FXML
    private TableColumn<Guia, String> columnAniosExperiencia;

    @FXML
    private TableColumn<Guia, String> columnCorreo;

    @FXML
    private TableColumn<Guia, String> columnNombre;

    @FXML
    private TableColumn<Guia, String> columnTelefono;

    @FXML
    private TableView<Guia> tableViewGuia;

    @FXML
    private TextField txtAniosExperiencia;

    @FXML
    private TextField txtCedula;

    @FXML
    private TextField txtContrasenia;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtResidencia;

    @FXML
    private TextField txtTelefono;

    //Variables auxiliares
    private AgenciaApp agenciaApp;
    private Stage stage;
    private GestionarAgenciaController gestionarAgenciaController;
    private Administrador administradorSesion;
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
     * @return la lista de guias como una observableList para la tableView
     */
    private ObservableList<Guia> getListaGuias() {
        listadoGuias.clear();
        listadoGuias.addAll(agenciaViajes.getListaGuiasTuristicos());
        return listadoGuias;
    }

    public void init(Stage stage, GestionarAgenciaController gestionarAgenciaController, Administrador administradorSesion) {
        this.stage = stage;
        this.gestionarAgenciaController = gestionarAgenciaController;
        this.administradorSesion = administradorSesion;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Datos en la tableView de guias
        this.columnNombre.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getNombre()));
        this.columnCorreo.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getCorreo()));
        this.columnTelefono.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getTelefono()));
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
                //Lleno los campos del guiaSeleccionado en la interfaz y descativo campos
                txtCedula.setDisable(true);
                llenarCamposGuia(guiaSeleccion);
            }
        });
        //Para que en el textField de experiencia solo se puedan colocar numeros
        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), 0, change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        });
        txtAniosExperiencia.setTextFormatter(textFormatter);
    }

    /**
     * Llena los campos de los texfields con la info del guia pasado como parametro
     * @param guiaSeleccion
     */
    private void llenarCamposGuia(Guia guiaSeleccion) {
        txtCedula.setText(guiaSeleccion.getId());
        txtNombre.setText(guiaSeleccion.getNombre());
        txtCorreo.setText(guiaSeleccion.getCorreo());
        txtTelefono.setText(guiaSeleccion.getTelefono());
        txtContrasenia.setText(guiaSeleccion.getContrasenia());
        txtResidencia.setText(guiaSeleccion.getResidencia());
        txtAniosExperiencia.setText("" + guiaSeleccion.getAniosExperiencia());
        //Verifica que idiomas habla el guia para llenar las checkBox
        checkBoxEspaniol.setSelected(agenciaViajes.hablaIdiomaGuia(guiaSeleccion, Lenguaje.ESPANIOL, 0));
        checkBoxIngles.setSelected(agenciaViajes.hablaIdiomaGuia(guiaSeleccion, Lenguaje.INGLES, 0));
        checkBoxFrances.setSelected(agenciaViajes.hablaIdiomaGuia(guiaSeleccion, Lenguaje.FRANCES, 0));
    }

    /**
     * Limpia los textFields y checkBox de la interfaz
     */
    private void limpiarCamposGuia() {
        txtCedula.clear();
        txtNombre.clear();
        txtCorreo.clear();
        txtTelefono.clear();
        txtContrasenia.clear();
        txtResidencia.clear();
        txtAniosExperiencia.clear();
        checkBoxEspaniol.setSelected(false);
        checkBoxIngles.setSelected(false);
        checkBoxFrances.setSelected(false);
    }

    /**
     * Actualiza los datos de un guia
     * @param event
     */
    @FXML
    void actualizarGuia(ActionEvent event) {
        try {
            agenciaViajes.actulizarGuia(
                    txtCedula.getText(),
                    txtNombre.getText(),
                    txtCorreo.getText(),
                    txtTelefono.getText(),
                    txtResidencia.getText(),
                    txtContrasenia.getText(),
                    Integer.valueOf(txtAniosExperiencia.getText()),
                    agenciaViajes.obtenerArrayIdiomasGuia(checkBoxEspaniol.isSelected(), checkBoxIngles.isSelected(), checkBoxFrances.isSelected())
            );
            tableViewGuia.getItems().clear();
            tableViewGuia.setItems(getListaGuias());
            mostrarMensaje("Agencia", "Gestionar Guías", "El guía " + txtCedula.getText() + " ha sido actualizado", Alert.AlertType.INFORMATION);
            limpiarCamposGuia();
        } catch (GuiaNoRegistradoException e) {
            mostrarMensaje("Agencia", "Gestionar Guías", e.getMessage(), Alert.AlertType.WARNING);
        } catch (CampoObligatorioGuiaException e) {
            mostrarMensaje("Agencia", "Gestionar Guías", e.getMessage(), Alert.AlertType.WARNING);
        } catch (NumberFormatException e) {
            mostrarMensaje("Agencia", "Gestionar Guías", "Asegurese de ingresar un número en los años de experiencia del guía", Alert.AlertType.WARNING);
        }
    }

    /**
     * Crea un guia turistico
     * @param event
     */
    @FXML
    void crearGuia(ActionEvent event) {
        ArrayList<CalificacionGuia> calificaciones = new ArrayList<>();
        try {
            agenciaViajes.crearGuia(
                    txtCedula.getText(),
                    txtNombre.getText(),
                    txtCorreo.getText(),
                    txtTelefono.getText(),
                    txtResidencia.getText(),
                    txtContrasenia.getText(),
                    Integer.valueOf(txtAniosExperiencia.getText()),
                    agenciaViajes.obtenerArrayIdiomasGuia(checkBoxEspaniol.isSelected(), checkBoxIngles.isSelected(), checkBoxFrances.isSelected()),
                    calificaciones
            );
            //Se añade el guia creado a la tableView
            tableViewGuia.getItems().clear();
            tableViewGuia.setItems(getListaGuias());
            mostrarMensaje("Agencia", "Gestionar Guías", "El guia " + txtNombre.getText() + " ha sido registrado", Alert.AlertType.INFORMATION);
            limpiarCamposGuia();
        } catch (GuiaYaExistenteException e) {
            mostrarMensaje("Agencia", "Gestionar Guías", e.getMessage(), Alert.AlertType.WARNING);
        } catch (CampoObligatorioGuiaException e) {
            mostrarMensaje("Agencia", "Gestionar Guías", e.getMessage(), Alert.AlertType.WARNING);
        } catch (NumberFormatException e) {
            mostrarMensaje("Agencia", "Gestionar Guías", "Asegurese de ingresar un número en los años de experiencia del guía", Alert.AlertType.WARNING);
        }
    }

    /**
     * Elimina un guia seleccionado
     * @param event
     */
    @FXML
    void eliminarGuia(ActionEvent event) {
        txtCedula.setDisable(false);
        try {
            if (guiaSeleccion != null) {
                String cedula = guiaSeleccion.getId();
                agenciaViajes.eliminarGuia(cedula);
                //Elimina el cliente en la tableView
                tableViewGuia.getItems().clear();
                tableViewGuia.setItems(getListaGuias());
                mostrarMensaje("Agencia", "Gestionar Guías", "El guia con cédula " + cedula + " ha sido eliminado correctamente", Alert.AlertType.INFORMATION);
                limpiarCamposGuia();
            } else {
                mostrarMensaje("Agencia", "Gestionar Guías", "Por favor seleccione un guía en la tabla", Alert.AlertType.WARNING);
            }
        } catch (GuiaNoRegistradoException e) {
            mostrarMensaje("Agencia", "Gestionar Guías", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Limpia los campos de la interfaz
     * @param event
     */
    @FXML
    void nuevoGuia(ActionEvent event) {
        limpiarCamposGuia();
        txtCedula.setDisable(false);
    }

    /**
     * Regresa a la ventana de gestionar agencia
     * @param event
     */
    @FXML
    void regresar(ActionEvent event) {
        this.stage.close();
        gestionarAgenciaController.show();
        //Actualiza la lista de guias en la tabla antes de cerrar la ventana
        tableViewGuia.getItems().setAll(getListaGuias());
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
