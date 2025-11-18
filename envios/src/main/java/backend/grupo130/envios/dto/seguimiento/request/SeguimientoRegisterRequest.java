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

    @NotNull(message = "El ID de la solicitud no puede ser nulo")
    @Positive(message = "El ID de la solicitud debe ser positivo")
    private final Long idSolicitud;

    @NotBlank(message = "El estado no puede estar vacío")
    private final String estado;

    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
    private final String descripcion;

}
