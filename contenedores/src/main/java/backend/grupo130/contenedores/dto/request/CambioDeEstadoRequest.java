package backend.grupo130.contenedores.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CambioDeEstadoRequest {

    @NotNull(message = "La id es obligatoria")
    private final Integer id;

    @NotNull(message = "El estado es obligatorio")
    private final String estado;

}
