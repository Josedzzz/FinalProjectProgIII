package co.edu.uniquindio.agencia.model;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@EqualsAndHashCode(of = {"nombre", "ciudad"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Destino {

    private String nombre;
    private String ciudad;
    private String descripcion;
    private ArrayList<String> listaImagenes;
    private TipoClima tipoClima;
    private TipoDestino tipoDestino;
    private ArrayList<CalificacionDestino> calificaciones;

}