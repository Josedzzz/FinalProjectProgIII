package co.edu.uniquindio.agencia.controller;

import co.edu.uniquindio.agencia.app.AgenciaApp;
import co.edu.uniquindio.agencia.exceptions.AtributoIncorrectoException;
import co.edu.uniquindio.agencia.exceptions.AtributosVaciosException;
import co.edu.uniquindio.agencia.model.Administrador;
import co.edu.uniquindio.agencia.model.AgenciaViajes;
import co.edu.uniquindio.agencia.model.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RecuperarContraseniaController {

    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnRegresar;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtContrasenia;

    //Variables auxiliares
    private AgenciaApp agenciaApp;
    private Stage stage;
    private IniciarSesionController iniciarSesionController;
    private Cliente clienteSesion;
    private Administrador administradorSesion;
    private String codigo; //Es el codigo para la recuperacion de la contrasenia

    //Uso de singleton
    private final AgenciaViajes agenciaViajes = AgenciaViajes.getInstance();

    public void setAgenciaApp(AgenciaApp agenciaApp) {
        this.agenciaApp = agenciaApp;
    }

    public void initCliente(Stage stage, IniciarSesionController iniciarSesionController, Cliente clienteSesion, String codigo) {
        this.stage = stage;
        this.iniciarSesionController = iniciarSesionController;
        this.clienteSesion = clienteSesion;
        this.codigo = codigo;
        this.administradorSesion = null;
    }

    public void initAdmin(Stage stage, IniciarSesionController iniciarSesionController, Administrador administradorSesion, String codigo) {
        this.stage = stage;
        this.iniciarSesionController = iniciarSesionController;
        this.administradorSesion = administradorSesion;
        this.codigo = codigo;
        this.clienteSesion = null;
    }

    /**
     * Hace el cambio de contrasenia dependiendo de si se inicio como admin o cliente
     * @param event
     */
    @FXML
    void aceptarCambioContrasenia(ActionEvent event) {
        if (administradorSesion != null) {
            try {
                agenciaViajes.hacerCambioContraseniaAdmin(administradorSesion, codigo, txtCodigo.getText(), txtContrasenia.getText());
                mostrarMensaje("Agencia", "Recuperar Contraseña", "El cambio de contraseña se realizo de manera éxitosa. Por favor regresa a la ventana anterior para iniciar sesión", Alert.AlertType.INFORMATION);
            } catch (AtributoIncorrectoException e) {
                mostrarMensaje("Agencia", "Recuperar Contraseña", e.getMessage(), Alert.AlertType.WARNING);
            } catch (AtributosVaciosException e) {
                mostrarMensaje("Agencia", "Recuperar Contraseña", e.getMessage(), Alert.AlertType.WARNING);
            }
        } else {
            try {
                agenciaViajes.hacerCambioContraseniaCliente(clienteSesion, codigo, txtCodigo.getText(), txtContrasenia.getText());
                mostrarMensaje("Agencia", "Recuperar Contraseña", "El cambio de contraseña se realizo de manera éxitosa. Por favor regresa a la ventana anterior para iniciar sesión", Alert.AlertType.INFORMATION);
            } catch (AtributoIncorrectoException e) {
                mostrarMensaje("Agencia", "Recuperar Contraseña", e.getMessage(), Alert.AlertType.WARNING);
            } catch (AtributosVaciosException e) {
                mostrarMensaje("Agencia", "Recuperar Contraseña", e.getMessage(), Alert.AlertType.WARNING);
            }
        }
    }

    /**
     * Se regresa a la ventana de iniciar sesion
     * @param event
     */
    @FXML
    void regresar(ActionEvent event) {
        this.stage.close();
        iniciarSesionController.show();
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

