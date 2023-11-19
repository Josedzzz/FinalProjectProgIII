package co.edu.uniquindio.agencia.socket;

import co.edu.uniquindio.agencia.exceptions.AtributosVaciosException;
import co.edu.uniquindio.agencia.exceptions.ClienteYaExistenteException;
import co.edu.uniquindio.agencia.functions.RegistrarCliente;
import co.edu.uniquindio.agencia.model.AgenciaViajes;
import co.edu.uniquindio.agencia.model.Cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class HiloCliente implements Runnable {

    private final Socket socket;
    private final AgenciaViajes agencia;

    public HiloCliente(Socket socket, AgenciaViajes agencia) {
        this.socket = socket;
        this.agencia = agencia;
    }

    @Override
    public void run() {
        try {
            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            //Se lee el mensaje enviado por el cliente
            Mensaje mensaje = (Mensaje) in.readObject();
            //Se captura el tipo de mensaje
            String tipo = mensaje.getTipo();
            //Se captura el contenido del mensaje
            Object contenido = mensaje.getContenido();
            //Segun el tipo de mensaje se invoca el metodo correspondiente
            switch (tipo) {
                case "agregarCliente":
                    agregarCliente((RegistrarCliente) contenido, out);
                    break;
            }
            //Se cierra la conexion del socket
            socket.close();
        } catch (IOException | ClassNotFoundException | ClienteYaExistenteException | AtributosVaciosException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Llama a la funcion agregar cliente de la agencia
     * @param contenido
     * @param out
     * @throws ClienteYaExistenteException
     * @throws AtributosVaciosException
     */
    private void agregarCliente(RegistrarCliente contenido, ObjectOutputStream out) throws ClienteYaExistenteException, AtributosVaciosException {
        try {
            out.writeObject(agencia.crearCliente(
                    contenido.getId(),
                    contenido.getNombre(),
                    contenido.getCorreo(),
                    contenido.getTelefono(),
                    contenido.getResidencia(),
                    contenido.getContrasenia()
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
