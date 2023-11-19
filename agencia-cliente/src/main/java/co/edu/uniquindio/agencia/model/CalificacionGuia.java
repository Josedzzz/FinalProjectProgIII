package co.edu.uniquindio.agencia.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalificacionGuia {
    private double calificacion;
    private String comentario;
    private Cliente clienteAsociado;
    private Guia guiaAsociado;
}