package co.edu.uniquindio.agencia.functions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class RegistrarCliente implements Serializable {
    private String id;
    private String nombre;
    private String correo;
    private String telefono;
    private String residencia;
    private String contrasenia;
}
