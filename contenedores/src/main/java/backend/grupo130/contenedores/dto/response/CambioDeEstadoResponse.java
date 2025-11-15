package backend.grupo130.contenedores.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CambioDeEstadoResponse {

    private final Long id;

    private final String estado;

}
