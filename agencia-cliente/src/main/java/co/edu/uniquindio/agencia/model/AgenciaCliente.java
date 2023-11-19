package co.edu.uniquindio.agencia.model;

import co.edu.uniquindio.agencia.functions.RegistrarCliente;
import co.edu.uniquindio.agencia.socket.Mensaje;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

@Log
public class AgenciaCliente {
    private static final String HOST = "localhost";
    private static final int PUERTO = 1234;

    /**
     * Le envia al servidor una peticion para registrar un cliente
     * @param id
     * @param nombre
     * @param correo
     * @param telefono
     * @param residencia
     * @param contrasenia
     * @return
     */
    public Cliente registrarCliente(String id, String nombre, String correo, String telefono, String residencia, String contrasenia) {
        try (Socket socket = new Socket(HOST, PUERTO)) {
            //Se crean flujos de entrada y salida para comunicarse a traves del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            //Se crea un objeto de la funcion con los datos
            RegistrarCliente registrarCliente = RegistrarCliente.builder()
                    .nombre(nombre)
                    .id(id)
                    .correo(correo)
                    .telefono(telefono)
                    .residencia(residencia)
                    .contrasenia(contrasenia)
                    .build();
            //Se envia el mensaje al servidor con los datos de la peticion
            out.writeObject(Mensaje.builder()
                    .tipo("agregarCliente")
                    .contenido(registrarCliente)
                    .build());
            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();
            //Casteamos la respuesta del servidor
            Cliente cliente = (Cliente) respuesta;
            in.close();
            out.close();
            //Se retorna el cliente
            return cliente;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
