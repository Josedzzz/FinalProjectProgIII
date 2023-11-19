package co.edu.uniquindio.agencia.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalificacionDestino {
    private double calificacion;
    private String comentario;
    private Cliente clienteAsociado;
    private Destino destinoAsociado;
}