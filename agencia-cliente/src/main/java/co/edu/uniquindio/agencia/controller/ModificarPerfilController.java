package co.edu.uniquindio.agencia.controller;

import co.edu.uniquindio.agencia.app.AgenciaApp;
import co.edu.uniquindio.agencia.exceptions.AtributosVaciosException;
import co.edu.uniquindio.agencia.exceptions.ClienteNoRegistradoException;
import co.edu.uniquindio.agencia.model.AgenciaViajes;
import co.edu.uniquindio.agencia.model.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class ModificarPerfilController implements Initializable {

    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnRegresar;

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
    private Stage stage;
    private AgenciaApp agenciaApp;
    private InicioController inicioController;
    private Cliente clienteSesion;

    //Uso de singleton
    private final AgenciaViajes agenciaViajes = AgenciaViajes.getInstance();

    public void setAgenciaApp(AgenciaApp agenciaApp) {
        this.agenciaApp = agenciaApp;
    }

    public void init(Stage stage, InicioController inicioController, Cliente clienteSesion) {
        this.stage = stage;
        this.inicioController = inicioController;
        this.clienteSesion = clienteSesion;
        //Lleno campos de la interfaz
        txtCedula.setText(clienteSesion.getId());
        txtCedula.setDisable(true);
        txtNombre.setText(clienteSesion.getNombre());
        txtCorreo.setText(clienteSesion.getCorreo());
        txtTelefono.setText(clienteSesion.getTelefono());
        txtContrasenia.setText(clienteSesion.getContrasenia());
        txtResidencia.setText(clienteSesion.getResidencia());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Para que en el texField de telefono solo se puedan colocar numeros
        TextFormatter<Integer> textFormatterTelefono = new TextFormatter<>(new IntegerStringConverter(), 0, change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        });
        txtTelefono.setTextFormatter(textFormatterTelefono);
    }

    /**
     * Actualiza los datos del cliente que inicia sesion
     * @param event
     */
    @FXML
    void actualizar(ActionEvent event) {
        try {
            agenciaViajes.actualizarCliente(
                    txtCedula.getText(),
                    txtNombre.getText(),
                    txtCorreo.getText(),
                    txtTelefono.getText(),
                    txtResidencia.getText(),
                    txtContrasenia.getText()
            );
            mostrarMensaje("Agencia", "Modificar Perfíl", "Tú perfíl ha sido actualizado de manera correcta", Alert.AlertType.INFORMATION);
        } catch (ClienteNoRegistradoException e) {
            mostrarMensaje("Agencia", "Modificar Perfíl", e.getMessage(), Alert.AlertType.WARNING);
        } catch (AtributosVaciosException e) {
            mostrarMensaje("Agencia", "Modificar Perfíl", e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    /**
     * Regresa a la ventana de inicio
     * @param event
     */
    @FXML
    void regresar(ActionEvent event) {
        this.stage.close();
        inicioController.show();
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
