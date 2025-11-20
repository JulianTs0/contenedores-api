package backend.grupo130.envios.dto.seguimiento.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeguimientoRegisterRequest {

    @NotNull(message = "{error.idSolicitud.notNull}")
    @Positive(message = "{error.idSolicitud.positive}")
    private final Long idSolicitud;

    @NotBlank(message = "{error.estado.notBlank}")
    private final String estado;

    @Size(max = 255, message = "{error.descripcion.max}")
    private final String descripcion;

}
