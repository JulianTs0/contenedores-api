package backend.grupo130.tramos.dto.ruta.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RutaAsignarSolicitudRequest {

    @NotNull(message = "{error.idRuta.notNull}")
    @Positive(message = "{error.idRuta.positive}")
    private final Long idRuta;

    @NotNull(message = "{error.idSolicitud.notNull}")
    @Positive(message = "{error.idSolicitud.positive}")
    private final Long idSolicitud;

}
