package backend.grupo130.contenedores.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CambioDeEstadoRequest {

    private final Long id;

    @NotNull(message = "{error.estado.notNull}")
    private final String estado;

}
