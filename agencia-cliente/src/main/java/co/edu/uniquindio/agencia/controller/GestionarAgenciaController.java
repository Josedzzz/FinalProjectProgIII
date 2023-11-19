package co.edu.uniquindio.agencia.controller;

import co.edu.uniquindio.agencia.app.AgenciaApp;
import co.edu.uniquindio.agencia.model.Administrador;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GestionarAgenciaController {

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Button btnDestinos;

    @FXML
    private Button btnGuias;

    @FXML
    private Button btnPaquetes;

    @FXML
    private Button btnVerEstadisticas;

    //Variables auxiliares
    private Stage stage;
    private AgenciaApp agenciaApp;
    private IniciarSesionController iniciarSesionController;
    private Administrador administradorSesion;

    public void setAgenciaApp(AgenciaApp agenciaApp) {
        this.agenciaApp = agenciaApp;
    }

    public void init(Stage stage, IniciarSesionController iniciarSesionController, Administrador administradorSesion) {
        this.stage = stage;
        this.iniciarSesionController = iniciarSesionController;
        this.administradorSesion = administradorSesion;
    }

    /**
     * Muestra la ventana gestionar agencia
     */
    public void show() {
        stage.show();
    }

    /**
     * Regresa a la ventana de iniciar sesion
     * @param event
     */
    @FXML
    void cerrarSesion(ActionEvent event) {
        this.stage.close();
        iniciarSesionController.show();
    }

    /**
     * Abre la ventana para gestionar los destinos
     * @param event
     * @throws IOException
     */
    @FXML
    void gestionarDestinos(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AgenciaApp.class.getResource("/views/GestionarDestinoView.fxml"));
        BorderPane borderPane = (BorderPane) loader.load();
        GestionarDestinoController controller = loader.getController();
        controller.setAgenciaApp(agenciaApp);
        Scene scene = new Scene(borderPane);
        Stage stage = new Stage();
        stage.setTitle("Gestionar Destinos");
        stage.setScene(scene);
        controller.init(stage, this, administradorSesion);
        stage.show();
        this.stage.close();
    }

    /**
     * Abre la ventana para gestionar los guias
     * @param event
     * @throws IOException
     */
    @FXML
    void gestionarGuias(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AgenciaApp.class.getResource("/views/GestionarGuiaView.fxml"));
        BorderPane borderPane = (BorderPane) loader.load();
        GestionarGuiaController controller = loader.getController();
        controller.setAgenciaApp(agenciaApp);
        Scene scene = new Scene(borderPane);
        Stage stage = new Stage();
        stage.setTitle("Gestionar Guías");
        stage.setScene(scene);
        controller.init(stage, this, administradorSesion);
        stage.show();
        this.stage.close();
    }

    /**
     * Abre la ventana para gestionar paquetes
     * @param event
     * @throws IOException
     */
    @FXML
    void gestionarPaquetes(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AgenciaApp.class.getResource("/views/GestionarPaqueteView.fxml"));
        BorderPane borderPane = (BorderPane) loader.load();
        GestionarPaqueteController controller = loader.getController();
        controller.setAgenciaApp(agenciaApp);
        Scene scene = new Scene(borderPane);
        Stage stage = new Stage();
        stage.setTitle("Gestionar Paquetes");
        stage.setScene(scene);
        controller.init(stage, this, administradorSesion);
        stage.show();
        this.stage.close();
    }

    @FXML
    void verEstadisticas(ActionEvent event) {
        mostrarMensaje("Agencia", "Gestionar Agencia", "Esta función estará disponible más adelante", Alert.AlertType.INFORMATION);
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
