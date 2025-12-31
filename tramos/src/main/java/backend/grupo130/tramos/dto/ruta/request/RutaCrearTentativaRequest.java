package backend.grupo130.tramos.dto.ruta.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class RutaCrearTentativaRequest {

    @NotNull(message = "{error.idSolicitud.notNull}")
    @Positive(message = "{error.idSolicitud.positive}")
    private final Long idSolicitud;

    @NotNull(message = "{error.ubicaciones.notNull}")
    @Size(min = 2, message = "{error.ubicaciones.sizeMin}")
    private final List<Long> ubicaciones;

    @NotNull(message = "{error.fechaHoraInicio.notNull}")
    private final LocalDateTime fechaHoraInicioEstimado;

    private final Integer alternativa;

}
