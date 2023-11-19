package co.edu.uniquindio.agencia.socket;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class Mensaje implements Serializable {
    private static final long serialVersionUID = 1L;
    private String tipo;
    private Object contenido;
}
