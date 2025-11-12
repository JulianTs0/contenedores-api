package backend.grupo130.tramos.dto.ruta.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RutaAsignarSolicitudRequest {

    @NotNull(message = "La id de la ruta es obligatoria")
    @Positive(message = "El ID de la ruta debe ser un número positivo")
    private final Integer idRuta;

    @NotNull(message = "La id de la solicitud es obligatoria")
    @NotBlank(message = "El ID de la solicitud debe ser un número positivo")
    private final Integer idSolicitud;

}
