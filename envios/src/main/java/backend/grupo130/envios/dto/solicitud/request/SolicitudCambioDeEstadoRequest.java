package backend.grupo130.envios.dto.solicitud.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SolicitudCambioDeEstadoRequest {

    @NotNull(message = "El ID de solicitud no puede ser nulo")
    @Positive(message = "El ID de solicitud debe ser positivo")
    private final Long idSolicitud;

    @NotBlank(message = "El nuevo estado no puede estar vacío")
    private final String nuevoEstado;

    @NotBlank(message = "La descripción del cambio no puede estar vacía")
    private final String descripcion;
}
