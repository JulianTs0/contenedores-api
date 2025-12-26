package backend.grupo130.camiones.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CambiarDisponibilidadRequest {

    private final String dominio;

    @NotNull(message = "{error.estado.notNull}")
    private final Boolean estado;

}
