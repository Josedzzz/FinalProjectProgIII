package co.edu.uniquindio.agencia.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Builder(builderMethodName = "guiaBuilder")
public class Guia extends Persona {

    private String id;
    private String nombre;
    private String correo;
    private String telefono;
    private String residencia;
    private String contrasenia;
    private int aniosExperiencia;
    private ArrayList<Lenguaje> listaLenguajes;
    private ArrayList<CalificacionGuia> calificaciones;

}