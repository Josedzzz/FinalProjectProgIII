package co.edu.uniquindio.agencia.app;

import co.edu.uniquindio.agencia.model.AgenciaViajes;
import co.edu.uniquindio.agencia.socket.HiloCliente;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AplicacionServidor {

    public static void main(String[] args) {
        System.out.println("hola mundo");
        int puerto = 1234;
        //Se crea la instancia de la clase principal
        AgenciaViajes agenciaViajes = AgenciaViajes.getInstance();
        //Se crea la instancia de ExecutorService con el máximo de hilos permitidos
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //Se crea el server socket en el puerto 1234
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Esperando conexión...");
            while (true) {
                //Se obtiene la conexión del cliente
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Cliente conectado");
                //Se crea un hilo para la conexion del cliente
                executorService.execute(new HiloCliente(clienteSocket, agenciaViajes));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }
    }

}
