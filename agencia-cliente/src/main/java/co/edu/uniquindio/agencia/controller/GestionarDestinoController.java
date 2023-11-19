package co.edu.uniquindio.agencia.controller;

import co.edu.uniquindio.agencia.app.AgenciaApp;
import co.edu.uniquindio.agencia.exceptions.AtributosVaciosException;
import co.edu.uniquindio.agencia.exceptions.CampoObligatorioDestinoException;
import co.edu.uniquindio.agencia.exceptions.DestinoNoRegistradoException;
import co.edu.uniquindio.agencia.exceptions.DestinoYaExistenteException;
import co.edu.uniquindio.agencia.model.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GestionarDestinoController implements Initializable {

    @FXML
    private Button btnActualizarDestino;

    @FXML
    private Button btnAnteriorImagen;

    @FXML
    private Button btnCrearDestino;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnEliminarImagen;

    @FXML
    private Button btnNuevoDestino;

    @FXML
    private Button btnRegresar;

    @FXML
    private Button btnSiguienteImagen;

    @FXML
    private Button btnSuberImagen;

    @FXML
    private ComboBox<TipoClima> cbClima;

    @FXML
    private ComboBox<TipoDestino> cbTipoDestino;

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

    @FXML
    private ImageView imageViewDestino;

    @FXML
    private TextArea textAreaDescripcion;

    @FXML
    private TextField txtCiudad;

    @FXML
    private TextField txtImagen;

    @FXML
    private TextField txtNombre;

    //Declaro variables auxiliares
    private Stage stage;
    private AgenciaApp agenciaApp;
    private GestionarAgenciaController gestionarAgenciaController;
    private Administrador administradorSesion;
    private ObservableList<Destino> listadoDestinos = FXCollections.observableArrayList();
    private Destino destinoSeleccion;
    private int indiceImagen;

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
     * @return la lista de destinos como una observableList para la tableView
     */
    private ObservableList<Destino> getListaDestinos() {
        listadoDestinos.clear();
        listadoDestinos.addAll(agenciaViajes.getListaDestinos());
        return listadoDestinos;
    }

    public void init(Stage stage, GestionarAgenciaController gestionarAgenciaController, Administrador administradorSesion) {
        this.stage = stage;
        this.gestionarAgenciaController = gestionarAgenciaController;
        this.administradorSesion = administradorSesion;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Datos en la tableView de destinos
        this.columnNombre.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getNombre()));
        this.columnCiudad.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getCiudad()));
        this.columnClima.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getTipoClima().toString()));
        this.columnDescripcion.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getDescripcion()));
        //Seleccion de destinos en la tabla
        tableViewDestinos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                destinoSeleccion = newSelection;
                destinoSeleccion = tableViewDestinos.getSelectionModel().getSelectedItem();
                //Se llenan los campos del destino seleccionado en la tabla
                txtImagen.clear();
                imageViewDestino.setImage(null);
                llenarCamposDestino();
                txtNombre.setDisable(true);
                txtCiudad.setDisable(true);
                indiceImagen = 0;
            }
        });
        //Manejo del comboBox del clima
        txtImagen.setDisable(true);
        this.cbClima.getItems().addAll(TipoClima.values());
        //Manejo del comboBox del tipo de destino
        this.cbTipoDestino.getItems().addAll(TipoDestino.values());
    }

    /**
     * Muestra la informacion de un destino seleccionado
     */
    private void llenarCamposDestino() {
        try {
            txtNombre.setText(destinoSeleccion.getNombre());
            txtCiudad.setText(destinoSeleccion.getCiudad());
            cbClima.getSelectionModel().select(destinoSeleccion.getTipoClima());
            cbTipoDestino.getSelectionModel().select(destinoSeleccion.getTipoDestino());
            textAreaDescripcion.setText(destinoSeleccion.getDescripcion());
            //Cargo la primera imagen del destino
            String rutaImagen = agenciaViajes.obtenerImagenDestino(destinoSeleccion.getListaImagenes());
            Image imagenDestinoSeleccion = new Image("file:" + rutaImagen);
            imageViewDestino.setImage(imagenDestinoSeleccion);
            txtImagen.setText(destinoSeleccion.getListaImagenes().get(0));
        } catch (AtributosVaciosException e) {
            mostrarMensaje("Agencia", "Gestinar Destinos", e.getMessage(), Alert.AlertType.INFORMATION);
        }
    }

    /**
     * Limpia todos los campos del destino
     */
    private void limpiarCamposDestino() {
        txtNombre.clear();
        txtCiudad.clear();
        cbClima.setValue(null);
        cbTipoDestino.setValue(null);
        textAreaDescripcion.clear();
        txtImagen.clear();
        imageViewDestino.setImage(null);
    }

    /**
     * Actualiza el clima y la descipcion de un destino
     * @param event
     */
    @FXML
    void actualizarDestino(ActionEvent event) {
        try {
            agenciaViajes.actualizarDestino(
                    txtNombre.getText(),
                    txtCiudad.getText(),
                    textAreaDescripcion.getText(),
                    cbClima.getValue(),
                    cbTipoDestino.getValue()
            );
            tableViewDestinos.getItems().clear();
            tableViewDestinos.setItems(getListaDestinos());
            mostrarMensaje("Agencia", "Gestionar Destinos", "El destino " + txtNombre.getText() + " ha sido actualizado", Alert.AlertType.INFORMATION);
            limpiarCamposDestino();
        } catch (DestinoNoRegistradoException e) {
            mostrarMensaje("Agencia", "Gestionar Destinos", e.getMessage(), Alert.AlertType.WARNING);
        } catch (CampoObligatorioDestinoException e) {
            mostrarMensaje("Agencia", "Gestionar Destinos", e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    /**
     * Crea un destino
     * @param event
     */
    @FXML
    void crearDestino(ActionEvent event) {
        ArrayList<String> imagenesDestinos = new ArrayList<>();
        ArrayList<CalificacionDestino> calificaciones = new ArrayList<>();
        try {
            agenciaViajes.crearDestino(
                    txtNombre.getText(),
                    txtCiudad.getText(),
                    textAreaDescripcion.getText(),
                    imagenesDestinos,
                    cbClima.getValue(),
                    cbTipoDestino.getValue(),
                    calificaciones
            );
            //Se añade el destino creado a la tableView
            tableViewDestinos.getItems().clear();
            tableViewDestinos.setItems(getListaDestinos());
            mostrarMensaje("Agencia", "Gestionar Destinos", "El destino " + txtNombre.getText() + " ha sido registrado", Alert.AlertType.INFORMATION);
            limpiarCamposDestino();
        } catch (CampoObligatorioDestinoException e) {
            mostrarMensaje("Agencia", "Gestionar Destinos", e.getMessage(), Alert.AlertType.WARNING);
        } catch (DestinoYaExistenteException e) {
            mostrarMensaje("Agencia", "Gestionar Destinos", e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    /**
     * Elimina el destino seleccionado en la tableView
     * @param event
     */
    @FXML
    void eliminarDestino(ActionEvent event) {
        try {
            if (destinoSeleccion != null) {
                String nombre = destinoSeleccion.getNombre();
                String ciudad = destinoSeleccion.getCiudad();
                agenciaViajes.eliminarDestino(nombre, ciudad);
                //Elimina el destino en la tableView
                tableViewDestinos.getItems().clear();
                tableViewDestinos.setItems(getListaDestinos());
                mostrarMensaje("Agencia", "Gestionar Destinos", "El destino " + nombre + " ha sido eliminado correctamente", Alert.AlertType.INFORMATION);
                limpiarCamposDestino();
            } else {
                mostrarMensaje("Agencia", "Gestionar Destinos", "Por favor seleccione un destino en la tabla", Alert.AlertType.WARNING);
            }
        } catch (DestinoNoRegistradoException e) {
            mostrarMensaje("Agencia", "Gestionar Destinos", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Limpia los campos del destino seleccionado
     * @param event
     */
    @FXML
    void nuevoDestino(ActionEvent event) {
        limpiarCamposDestino();
        txtNombre.setDisable(false);
        txtCiudad.setDisable(false);
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
     * Sube una imagen a el destino seleccionado en la tabla
     * @param event
     */
    @FXML
    void subirImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg"));
        Stage stage = new Stage();
        File archivoSeleccionado = fileChooser.showOpenDialog(stage);
        try {
            if (archivoSeleccionado != null) {
                String rutaImagen = archivoSeleccionado.getAbsolutePath();
                Image imagenDestino = new Image("file:" + rutaImagen);
                imageViewDestino.setImage(imagenDestino);
                txtImagen.setText(rutaImagen);
                agenciaViajes.subirImagenDestino(destinoSeleccion, rutaImagen);
                mostrarMensaje("Agencia", "Gestionar Destinos", "Se agrego correctamente la imagen al destino " + destinoSeleccion.getNombre(), Alert.AlertType.INFORMATION);
            }
        } catch (DestinoNoRegistradoException e) {
            mostrarMensaje("Agencia", "Gestionar Destinos", e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    /**
     * Elimina una imagen de un destino seleccionado
     * @param event
     */
    @FXML
    void eliminarImagen(ActionEvent event) {
        try {
            agenciaViajes.eliminarImagenDestino(destinoSeleccion, txtImagen.getText());
            limpiarCamposDestino();
            mostrarMensaje("Agencia", "Gestionar Destinos", "Se elimino correctamente la imagen del destino", Alert.AlertType.INFORMATION);
        } catch (DestinoNoRegistradoException e) {
            mostrarMensaje("Agencia", "Gestionar Destinos", e.getMessage(), Alert.AlertType.WARNING);
        } catch (AtributosVaciosException e) {
            mostrarMensaje("Agencia", "Gestionar Destinos", e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    /**
     * Muestra la siguiente imagen correspondiente al destino seleccionado
     * @param event
     */
    @FXML
    void siguienteImagen(ActionEvent event) {
        try {
            indiceImagen = agenciaViajes.siguienteImagenDestino(destinoSeleccion, indiceImagen);
            String rutaImagen = destinoSeleccion.getListaImagenes().get(indiceImagen);
            Image imagenDestino = new Image("file:" + rutaImagen);
            imageViewDestino.setImage(imagenDestino);
            txtImagen.setText(rutaImagen);
        } catch (DestinoNoRegistradoException e) {
            mostrarMensaje("Agencia", "Gestionar Destinos", e.getMessage(), Alert.AlertType.WARNING);
        } catch (AtributosVaciosException e) {
            mostrarMensaje("Agencia", "Gestionar Destinos", e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    /**
     * Muestra la imagen anterior correspondiente al destino seleccionado
     * @param event
     */
    @FXML
    void anteriorImagen(ActionEvent event) {
        try {
            indiceImagen = agenciaViajes.anteriorImagenDestino(destinoSeleccion, indiceImagen);
            String rutaImagen = destinoSeleccion.getListaImagenes().get(indiceImagen);
            Image imagenDestino = new Image("file:" + rutaImagen);
            imageViewDestino.setImage(imagenDestino);
            txtImagen.setText(rutaImagen);
        } catch (DestinoNoRegistradoException e) {
            mostrarMensaje("Agencia", "Gestionar Destinos", e.getMessage(), Alert.AlertType.WARNING);
        } catch (AtributosVaciosException e) {
            mostrarMensaje("Agencia", "Gestionar Destinos", e.getMessage(), Alert.AlertType.WARNING);
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

