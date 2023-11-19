package co.edu.uniquindio.agencia.controller;

import co.edu.uniquindio.agencia.app.AgenciaApp;
import co.edu.uniquindio.agencia.model.AgenciaViajes;
import co.edu.uniquindio.agencia.model.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class InicioController implements Initializable {

    @FXML
    private Button btnCalificarDestinos;

    @FXML
    private Button btnCalificarGuias;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Button btnConocerDestinos;

    @FXML
    private Button btnConocerPaquetes;

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private Button btnModificarPerfil;

    @FXML
    private Button btnRegistrarse;

    @FXML
    private Button btnVerMisReservas;

    //Variables auxiliares
    private AgenciaApp agenciaApp;
    private Stage stage;
    private Cliente clienteSesion;

    //Uso de singleton
    private final AgenciaViajes agenciaViajes = AgenciaViajes.getInstance();

    public void setAgenciaApp(AgenciaApp agenciaApp) {
        this.agenciaApp = agenciaApp;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init(Stage stage, Cliente clienteSesion) {
        this.stage = stage;
        this.clienteSesion = clienteSesion;
        btnIniciarSesion.setVisible(false);
        btnRegistrarse.setVisible(false);
        btnCalificarDestinos.setVisible(true);
        btnCalificarGuias.setVisible(true);
        btnVerMisReservas.setVisible(true);
        btnModificarPerfil.setVisible(true);
        btnCerrarSesion.setVisible(true);
    }

    /**
     * Muestra la ventana de inicio
     */
    public void show() {
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Deshabilita campos que no se pueden ver hasta que se inicie sesion
        btnCalificarDestinos.setVisible(false);
        btnCalificarGuias.setVisible(false);
        btnVerMisReservas.setVisible(false);
        btnModificarPerfil.setVisible(false);
        btnCerrarSesion.setVisible(false);
    }

    /**
     * Lleva a la ventana para calificar destinos
     * @param event
     * @throws IOException
     */
    @FXML
    void calificarDestinos(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AgenciaApp.class.getResource("/views/CalificarDestinosView.fxml"));
        BorderPane borderPane = (BorderPane) loader.load();
        CalificarDestinosController controller = loader.getController();
        controller.setAgenciaApp(agenciaApp);
        Scene scene = new Scene(borderPane);
        Stage stage = new Stage();
        stage.setTitle("Calificar destinos");
        stage.setScene(scene);
        controller.init(stage, this, clienteSesion);
        stage.show();
        this.stage.close();
    }

    /**
     * Lleva a la ventana para calificar guias
     * @param event
     * @throws IOException
     */
    @FXML
    void calificarGuias(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AgenciaApp.class.getResource("/views/CalificarGuiasView.fxml"));
        BorderPane borderPane = (BorderPane) loader.load();
        CalificarGuiasController controller = loader.getController();
        controller.setAgenciaApp(agenciaApp);
        Scene scene = new Scene(borderPane);
        Stage stage = new Stage();
        stage.setTitle("Calificar Guías");
        stage.setScene(scene);
        controller.init(stage, this, clienteSesion);
        stage.show();
        this.stage.close();
    }

    /**
     * Deshabilita campos que solo funcionan cuando se inicio sesion
     * @param event
     */
    @FXML
    void cerrarSesion(ActionEvent event) {
        clienteSesion = null;
        btnCalificarDestinos.setVisible(false);
        btnCalificarGuias.setVisible(false);
        btnVerMisReservas.setVisible(false);
        btnModificarPerfil.setVisible(false);
        btnCerrarSesion.setVisible(false);
        btnIniciarSesion.setVisible(true);
        btnRegistrarse.setVisible(true);
        mostrarMensaje("Agencia", "Inicio", "Se ha cerrado la sesión de manera correcta", Alert.AlertType.INFORMATION);
    }

    /**
     * Lleva a la ventana de buscador de destinos
     * @param event
     * @throws IOException
     */
    @FXML
    void conocerDestinos(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AgenciaApp.class.getResource("/views/BuscadorDestinosView.fxml"));
        BorderPane borderPane = (BorderPane) loader.load();
        BuscadorDestinosController controller = loader.getController();
        controller.setAgenciaApp(agenciaApp);
        Scene scene = new Scene(borderPane);
        Stage stage = new Stage();
        stage.setTitle("Buscador Destinos");
        stage.setScene(scene);
        controller.init(stage, this, clienteSesion);
        stage.show();
        this.stage.close();
    }

    /**
     * Lleva a la ventana de buscador de paquetes
     * @param event
     * @throws IOException
     */
    @FXML
    void conocerPaquetes(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AgenciaApp.class.getResource("/views/BuscadorPaquetesView.fxml"));
        BorderPane borderPane = (BorderPane) loader.load();
        BuscadorPaquetesController controller = loader.getController();
        controller.setAgenciaApp(agenciaApp);
        Scene scene = new Scene(borderPane);
        Stage stage = new Stage();
        stage.setTitle("Buscador Paquetes");
        stage.setScene(scene);
        controller.init(stage, this, clienteSesion);
        stage.show();
        this.stage.close();
    }

    /**
     * Lleva a la ventana iniciarSesion
     * @param event
     */
    @FXML
    void iniciarSesion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AgenciaApp.class.getResource("/views/IniciarSesionView.fxml"));
        BorderPane borderPane = (BorderPane) loader.load();
        IniciarSesionController controller = loader.getController();
        controller.setAgenciaApp(agenciaApp);
        Scene scene = new Scene(borderPane);
        Stage stage = new Stage();
        stage.setTitle("Iniciar sesión");
        stage.setScene(scene);
        controller.init(stage, this);
        stage.show();
        this.stage.close();
    }

    /**
     * Lleva a la ventana de modificar perfil
     * @param event
     */
    @FXML
    void modificarPerfil(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AgenciaApp.class.getResource("/views/ModificarPerfilView.fxml"));
        BorderPane borderPane = (BorderPane) loader.load();
        ModificarPerfilController controller = loader.getController();
        controller.setAgenciaApp(agenciaApp);
        Scene scene = new Scene(borderPane);
        Stage stage = new Stage();
        stage.setTitle("Iniciar sesión");
        stage.setScene(scene);
        controller.init(stage, this, clienteSesion);
        stage.show();
        this.stage.close();
    }

    /**
     * Lleva a la ventana registrarse
     * @param event
     * @throws IOException
     */
    @FXML
    void registrarse(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AgenciaApp.class.getResource("/views/RegistrarseView.fxml"));
        BorderPane borderPane = (BorderPane) loader.load();
        RegistrarseController controller = loader.getController();
        controller.setAgenciaApp(agenciaApp);
        Scene scene = new Scene(borderPane);
        Stage stage = new Stage();
        stage.setTitle("Registrase");
        stage.setScene(scene);
        controller.init(stage, this);
        stage.show();
        this.stage.close();
    }

    /**
     * Lleva a la ventana de reservas
     * @param event
     * @throws IOException
     */
    @FXML
    void verReservas(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AgenciaApp.class.getResource("/views/ReservasView.fxml"));
        BorderPane borderPane = (BorderPane) loader.load();
        ReservasController controller = loader.getController();
        controller.setAgenciaApp(agenciaApp);
        Scene scene = new Scene(borderPane);
        Stage stage = new Stage();
        stage.setTitle("Registrase");
        stage.setScene(scene);
        controller.init(stage, this, clienteSesion);
        stage.show();
        this.stage.close();
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
