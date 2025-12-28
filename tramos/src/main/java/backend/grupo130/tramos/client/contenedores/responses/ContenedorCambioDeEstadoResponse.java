package backend.grupo130.tramos.client.contenedores.responses;

import backend.grupo130.tramos.client.usuarios.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ContenedorCambioDeEstadoResponse {

    private final Long idContenedor;

    private final BigDecimal peso;

    private final BigDecimal volumen;

    private final Usuario cliente;

    private final String estado;

}
