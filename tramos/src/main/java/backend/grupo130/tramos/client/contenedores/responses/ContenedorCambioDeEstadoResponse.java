package backend.grupo130.tramos.client.contenedores.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContenedorCambioDeEstadoResponse {

    private final Long id;

    private final String estado;

}
