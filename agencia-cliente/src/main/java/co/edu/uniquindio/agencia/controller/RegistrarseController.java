package co.edu.uniquindio.agencia.controller;

import co.edu.uniquindio.agencia.app.AgenciaApp;
import co.edu.uniquindio.agencia.exceptions.AtributosVaciosException;
import co.edu.uniquindio.agencia.exceptions.ClienteYaExistenteException;
import co.edu.uniquindio.agencia.model.AgenciaCliente;
import co.edu.uniquindio.agencia.model.AgenciaViajes;
import co.edu.uniquindio.agencia.model.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegistrarseController implements Initializable {

    @FXML
    private Button btnRegistrarse;

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
    private AgenciaApp agenciaApp;
    private Stage stage;
    private InicioController inicioController;
    private Cliente clienteSesion;

    //Uso de singleton
   // private final AgenciaViajes agenciaViajes = AgenciaViajes.getInstance();
    AgenciaCliente agenciaCliente = new AgenciaCliente();

    public void setAgenciaApp(AgenciaApp agenciaApp) {
        this.agenciaApp = agenciaApp;
    }

    public void init(Stage stage, InicioController inicioController) {
        this.stage = stage;
        this.inicioController = inicioController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Para que en el texField de cedula y telefono solo se puedan colocar numeros
        TextFormatter<Integer> textFormatterCedula = new TextFormatter<>(new IntegerStringConverter(), 0, change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        });
        TextFormatter<Integer> textFormatterTelefono = new TextFormatter<>(new IntegerStringConverter(), 0, change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        });
        txtCedula.setTextFormatter(textFormatterCedula);
        txtTelefono.setTextFormatter(textFormatterTelefono);
    }

    /**
     * Registra un cliente
     * @param event
     */
    @FXML
    void registrarse(ActionEvent event) {
        try {
            clienteSesion = agenciaCliente.registrarCliente(
                    txtCedula.getText(),
                    txtNombre.getText(),
                    txtCorreo.getText(),
                    txtTelefono.getText(),
                    txtResidencia.getText(),
                    txtContrasenia.getText()
            );
            mostrarMensaje("Agencia", "Registrarse", "Su registro ha sido realizado de manera exitosa", Alert.AlertType.INFORMATION);
            //Vuelve a la ventana de inicio pero con el cliente que inicio sesion
            mostrarVentanaInicio();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Muestra la ventana inicial para un cliente ya registrado
     * @throws IOException
     */
    private void mostrarVentanaInicio() throws IOException {
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

