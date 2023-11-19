package co.edu.uniquindio.agencia.exceptions;

public class ClienteNoRegistradoException extends Exception{
    public ClienteNoRegistradoException(String mensaje){
        super(mensaje);
    }
}