package co.edu.uniquindio.agencia.controller;

import co.edu.uniquindio.agencia.app.AgenciaApp;
import co.edu.uniquindio.agencia.exceptions.AdminNoEncontradoException;
import co.edu.uniquindio.agencia.exceptions.AtributosVaciosException;
import co.edu.uniquindio.agencia.exceptions.ClienteNoRegistradoException;
import co.edu.uniquindio.agencia.model.Administrador;
import co.edu.uniquindio.agencia.model.AgenciaViajes;
import co.edu.uniquindio.agencia.model.Cliente;
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
import java.util.ResourceBundle;

public class IniciarSesionController implements Initializable {

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private Button btnRegresar;

    @FXML
    private Hyperlink hiperlinkOlvidasteContrasenia;

    @FXML
    private RadioButton radioButtonAdministrador;

    @FXML
    private RadioButton radioButtonCliente;

    @FXML
    private TextField txtCedula;

    @FXML
    private TextField txtContrasenia;

    //Variables auxiliares
    private AgenciaApp agenciaApp;
    private Stage stage;
    private InicioController inicioController;
    private Cliente clienteSesion;
    private Administrador administradorSesion;

    //Uso de singleton
    private final AgenciaViajes agenciaViajes = AgenciaViajes.getInstance();

    public void setAgenciaApp(AgenciaApp agenciaApp) {
        this.agenciaApp = agenciaApp;
    }

    public void init(Stage stage, InicioController inicioController) {
        this.stage = stage;
        this.inicioController = inicioController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Agrego a un mismo grupo los radioButtoms
        ToggleGroup group = new ToggleGroup();
        radioButtonCliente.setToggleGroup(group);
        radioButtonAdministrador.setToggleGroup(group);
    }

    /**
     * Muestra la ventana para iniciar sesion
     */
    public void show() {
        stage.show();
        //Limpio los campos de la ventana
        txtCedula.setText("");
        txtContrasenia.setText("");
        radioButtonCliente.setSelected(false);
        radioButtonAdministrador.setSelected(false);
    }

    /**
     * Login de la aplicacion dependiendo del radioButtom.
     * Puede loggear clientes y admins
     *
     * @param event
     */
    @FXML
    void iniciarSesion(ActionEvent event) {
        String cedula = txtCedula.getText();
        String contrasenia = txtContrasenia.getText();
        boolean admin = radioButtonAdministrador.isSelected();
        boolean cliente = radioButtonCliente.isSelected();
        try {
            agenciaViajes.validarDatosInicioSesion(cedula, contrasenia, admin, cliente);
            if (radioButtonAdministrador.isSelected()) {
                iniciarSesionAdmin(cedula, contrasenia);
            } else {
                iniciarSesionCliente(cedula, contrasenia);
            }
        } catch (AtributosVaciosException e) {
            mostrarMensaje("Agencia", "Iniciar Sesión", e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    /**
     * Ingresa sesion como administrador
     *
     * @param cedula      del administrador
     * @param contrasenia del administrador
     */
    private void iniciarSesionAdmin(String cedula, String contrasenia) {
        try {
            administradorSesion = agenciaViajes.iniciarSesionAdmin(cedula, contrasenia, 0);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AgenciaApp.class.getResource("/views/GestionarAgenciaView.fxml"));
            BorderPane borderPane = (BorderPane) loader.load();
            GestionarAgenciaController controller = loader.getController();
            controller.setAgenciaApp(agenciaApp);
            Scene scene = new Scene(borderPane);
            Stage stage = new Stage();
            stage.setTitle("Gestionar Agencia");
            stage.setScene(scene);
            controller.init(stage, this, administradorSesion);
            stage.show();
            this.stage.close();
        } catch (AdminNoEncontradoException e) {
            mostrarMensaje("Agencia", "Iniciar Sesión", e.getMessage(), Alert.AlertType.WARNING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Ingresa la sesion como cliente
     * @param cedula
     * @param contrasenia
     */
    private void iniciarSesionCliente(String cedula, String contrasenia) {
        try {
            clienteSesion = agenciaViajes.iniciarSesionCliente(cedula, contrasenia, 0);
            mostrarMensaje("Agencia", "Iniciar Sesión", "El inicio de sesión ha sido realizado de manera exitosa", Alert.AlertType.INFORMATION);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AgenciaApp.class.getResource("/views/InicioView.fxml"));
            BorderPane borderPane = (BorderPane) loader.load();
            InicioController controller = loader.getController();
            controller.setAgenciaApp(agenciaApp);
            Scene scene = new Scene(borderPane);
            Stage stage = new Stage();
            stage.setTitle("Agencia");
            stage.setScene(scene);
            controller.init(stage, clienteSesion);
            stage.show();
            this.stage.close();
        } catch (ClienteNoRegistradoException e) {
            mostrarMensaje("Agencia", "Iniciar Sesión", e.getMessage(), Alert.AlertType.WARNING);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
     * Recupera la contrasenia de la persona que trata de iniciar sesion
     * @param event
     */
    @FXML
    void recuperarContrasenia(ActionEvent event) {
        String cedula = txtCedula.getText();
        boolean admin = radioButtonAdministrador.isSelected();
        boolean cliente = radioButtonCliente.isSelected();
        try {
            agenciaViajes.validarDatosRecuperarContrasenia(cedula, admin, cliente);
            if (admin) {
                recuperarContraseniaAdmin(cedula);
            } else {
                recuperarContraseniaCliente(cedula);
            }
        } catch (AtributosVaciosException e) {
            mostrarMensaje("Agencia", "Recuperar Contraseña", e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    /**
     * Lleva al admin a la ventana de recuperar contrasenia mientras le envia a su correo un codigo para este proceso
     * @param cedula
     */
    private void recuperarContraseniaAdmin(String cedula) {
        try {
            administradorSesion = agenciaViajes.recuperarContraseniaAdmin(cedula, 0);
            String codigo = agenciaViajes.generarCodigo(4);
            agenciaViajes.enviarCorreoContraseniaAdmin(administradorSesion, codigo);
            mostrarMensaje("Agencia", "Recuperar Contraseña", "Por favor revise su correo, en este se encontrará un código para el cambio de contraseña", Alert.AlertType.INFORMATION);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AgenciaApp.class.getResource("/views/RecuperarContraseniaView.fxml"));
            BorderPane borderPane = (BorderPane) loader.load();
            RecuperarContraseniaController controller = loader.getController();
            controller.setAgenciaApp(agenciaApp);
            Scene scene = new Scene(borderPane);
            Stage stage = new Stage();
            stage.setTitle("Recuperar Contraseña");
            stage.setScene(scene);
            controller.initAdmin(stage, this, administradorSesion, codigo);
            stage.show();
            this.stage.close();
        } catch (AdminNoEncontradoException e) {
            mostrarMensaje("Agencia", "Recuperar Contraseña", e.getMessage(), Alert.AlertType.WARNING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Lleva al cliente a la ventana de recuperar contrasenia mientras le envia a su correo un codigo para este proceso
     * @param cedula
     */
    private void recuperarContraseniaCliente(String cedula) {
        try {
            clienteSesion = agenciaViajes.recuperarContraseniaCliente(cedula, 0);
            String codigo = agenciaViajes.generarCodigo(4);
            agenciaViajes.enviarCorreoContraseniaCliente(clienteSesion, codigo);
            mostrarMensaje("Agencia", "Recuperar Contraseña", "Por favor revise su correo, en este se encontrará un código para el cambio de contraseña", Alert.AlertType.INFORMATION);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AgenciaApp.class.getResource("/views/RecuperarContraseniaView.fxml"));
            BorderPane borderPane = (BorderPane) loader.load();
            RecuperarContraseniaController controller = loader.getController();
            controller.setAgenciaApp(agenciaApp);
            Scene scene = new Scene(borderPane);
            Stage stage = new Stage();
            stage.setTitle("Recuperar Contraseña");
            stage.setScene(scene);
            controller.initCliente(stage, this, clienteSesion, codigo);
            stage.show();
            this.stage.close();
        } catch (ClienteNoRegistradoException e) {
            mostrarMensaje("Agencia", "Recuperar Contraseña", e.getMessage(), Alert.AlertType.WARNING);
        } catch (IOException e) {
            throw new RuntimeException(e);
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