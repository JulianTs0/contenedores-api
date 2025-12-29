package backend.grupo130.envios.dto.solicitud.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SolicitudCambioDeEstadoRequest {

    private final Long idSolicitud;

    @NotBlank(message = "{error.estado.notBlank}")
    private final String nuevoEstado;

    @NotBlank(message = "{error.descripcion.notBlank}")
    private final String descripcion;
}
