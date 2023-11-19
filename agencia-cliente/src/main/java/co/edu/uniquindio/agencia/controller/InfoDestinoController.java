package co.edu.uniquindio.agencia.controller;

import co.edu.uniquindio.agencia.app.AgenciaApp;
import co.edu.uniquindio.agencia.exceptions.AtributosVaciosException;
import co.edu.uniquindio.agencia.exceptions.DestinoNoRegistradoException;
import co.edu.uniquindio.agencia.model.AgenciaViajes;
import co.edu.uniquindio.agencia.model.CalificacionDestino;
import co.edu.uniquindio.agencia.model.Cliente;
import co.edu.uniquindio.agencia.model.Destino;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class InfoDestinoController implements Initializable {

    @FXML
    private ImageView imageViewImagenDestino;

    @FXML
    private Button btnImagenAnterior;

    @FXML
    private Button btnRegresar;

    @FXML
    private Button btnSiguienteImagen;

    @FXML
    private TableColumn<CalificacionDestino, String> columnCalificacion;

    @FXML
    private TableColumn<CalificacionDestino, String> columnComentario;

    @FXML
    private TableColumn<CalificacionDestino, String> columnNombreCliente;

    @FXML
    private TableView<CalificacionDestino> tableViewCalificaciones;

    @FXML
    private TextArea textAreaDescripcion;

    @FXML
    private TextField txtCiudad;

    @FXML
    private TextField txtClima;

    @FXML
    private TextField txtNombreDestino;

    @FXML
    private TextField txtTipoDestino;

    //Variables auxiliares
    private Stage stage;
    private AgenciaApp agenciaApp;
    private BuscadorDestinosController buscadorDestinosController;
    private InfoPaqueteController infoPaqueteController;
    private CalificarDestinosController calificarDestinosController;
    private Cliente clienteSesion;
    private Destino destinoSeleccion;
    private ObservableList<CalificacionDestino> listadoCalificaciones = FXCollections.observableArrayList();
    private int indiceImagen;

    //Uso de singleton
    private final AgenciaViajes agenciaViajes = AgenciaViajes.getInstance();

    public void setAgenciaApp(AgenciaApp agenciaApp) {
        this.agenciaApp = agenciaApp;
    }

    public void init(Stage stage, BuscadorDestinosController buscadorDestinosController, Cliente clienteSesion, Destino destinoSeleccion) {
        this.stage = stage;
        this.buscadorDestinosController = buscadorDestinosController;
        this.clienteSesion = clienteSesion;
        this.destinoSeleccion = destinoSeleccion;
        llenarCamposDestino();
    }

    public void initPaquete(Stage stage, InfoPaqueteController infoPaqueteController, Cliente clienteSesion, Destino destinoSeleccion) {
        this.stage = stage;
        this.infoPaqueteController = infoPaqueteController;
        this.clienteSesion = clienteSesion;
        this.destinoSeleccion = destinoSeleccion;
        llenarCamposDestino();
    }

    public void initCalificacion(Stage stage, CalificarDestinosController calificarDestinosController, Cliente clienteSesion, Destino destinoSeleccion) {
        this.stage = stage;
        this.calificarDestinosController = calificarDestinosController;
        this.clienteSesion = clienteSesion;
        this.destinoSeleccion = destinoSeleccion;
        llenarCamposDestino();
    }

    /**
     * Mustra la informacion del destino
     */
    private void llenarCamposDestino() {
        //Lista de calificaciones del destino a mostrar
        tableViewCalificaciones.getItems().clear();
        tableViewCalificaciones.setItems(getListaCalificacionesDestino());
        //Lleno campos de la informacion del destino
        txtNombreDestino.setText(destinoSeleccion.getNombre());
        txtCiudad.setText(destinoSeleccion.getCiudad());
        txtClima.setText(destinoSeleccion.getTipoClima().toString());
        txtTipoDestino.setText(destinoSeleccion.getTipoDestino().toString());
        textAreaDescripcion.setText(destinoSeleccion.getDescripcion());
        //Cargo la primera imagen del destino
        try {
            String rutaImagen = agenciaViajes.obtenerImagenDestino(destinoSeleccion.getListaImagenes());
            Image imagenDestino = new Image("file:" + rutaImagen);
            imageViewImagenDestino.setImage(imagenDestino);
        } catch (AtributosVaciosException e) {
            mostrarMensaje("Agencia", "Información del Destino", e.getMessage(), Alert.AlertType.INFORMATION);
        }
    }

    /**
     * Obtiene las calificaciones de un destino
     * @return
     */
    private ObservableList<CalificacionDestino> getListaCalificacionesDestino() {
        try {
            listadoCalificaciones.clear();
            listadoCalificaciones.addAll(agenciaViajes.obtenerCalificacionesDestino(destinoSeleccion, 0));
        } catch (AtributosVaciosException e) {
            mostrarMensaje("Agencia", "Información del Destino", e.getMessage(), Alert.AlertType.INFORMATION);
        }
        return listadoCalificaciones;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Se llenan campos en la tableView de calificaciones del destino
        this.columnNombreCliente.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getClienteAsociado().getNombre()));
        this.columnComentario.setCellValueFactory(e -> new ReadOnlyStringWrapper(e.getValue().getComentario()));
        this.columnCalificacion.setCellValueFactory(e -> {
            double calificacion = e.getValue().getCalificacion();
            String calificacionString = String.valueOf(calificacion);
            return new ReadOnlyStringWrapper(calificacionString);
        });
        //Deshabilito campos
        txtNombreDestino.setDisable(true);
        txtCiudad.setDisable(true);
        txtClima.setDisable(true);
        txtTipoDestino.setDisable(true);
    }

    /**
     * Muestra la imagen anterior correspondiente al destino
     * @param event
     */
    @FXML
    void imagenAnterior(ActionEvent event) {
        try {
            indiceImagen = agenciaViajes.anteriorImagenDestino(destinoSeleccion, indiceImagen);
            String rutaImagen = destinoSeleccion.getListaImagenes().get(indiceImagen);
            Image imagenDestino = new Image("file:" + rutaImagen);
            imageViewImagenDestino.setImage(imagenDestino);
        } catch (DestinoNoRegistradoException e) {
            mostrarMensaje("Agencia", "Información del Destino", e.getMessage(), Alert.AlertType.WARNING);
        } catch (AtributosVaciosException e) {
            mostrarMensaje("Agencia", "Información del Destino", e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    /**
     * Muestra la siguiente imagen correspondiente al destino
     * @param event
     */
    @FXML
    void siguienteImagen(ActionEvent event) {
        try {
            indiceImagen = agenciaViajes.siguienteImagenDestino(destinoSeleccion, indiceImagen);
            String rutaImagen = destinoSeleccion.getListaImagenes().get(indiceImagen);
            Image imagenDestino = new Image("file:" + rutaImagen);
            imageViewImagenDestino.setImage(imagenDestino);
        } catch (DestinoNoRegistradoException e) {
            mostrarMensaje("Agencia", "Información del Destino", e.getMessage(), Alert.AlertType.WARNING);
        } catch (AtributosVaciosException e) {
            mostrarMensaje("Agencia", "Información del Destino", e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    /**
     * Regresa a la ventana correspondiente dependiendo del controller anterior
     * @param event
     */
    @FXML
    void regresar(ActionEvent event) {
        this.stage.close();
        if (infoPaqueteController != null) {
            infoPaqueteController.show();
        } else if (buscadorDestinosController != null) {
            buscadorDestinosController.show();
        } else {
            calificarDestinosController.show();
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
