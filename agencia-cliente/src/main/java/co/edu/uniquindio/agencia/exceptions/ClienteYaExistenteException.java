package co.edu.uniquindio.agencia.exceptions;

public class ClienteYaExistenteException extends Exception{
    public ClienteYaExistenteException(String mensaje){
        super(mensaje);
    }
}
