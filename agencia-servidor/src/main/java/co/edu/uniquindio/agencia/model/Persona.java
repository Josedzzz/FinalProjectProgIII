package co.edu.uniquindio.agencia.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Persona {
    protected String id;
    protected String nombre;
    protected String correo;
    protected String telefono;
    protected String residencia;
    protected String contrasenia;

}