package co.edu.uniquindio.agencia.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder(builderMethodName = "clienteBuilder")
public class Cliente extends Persona implements Serializable {

    private String id;
    private String nombre;
    private String correo;
    private String telefono;
    private String residencia;
    private String contrasenia;

}