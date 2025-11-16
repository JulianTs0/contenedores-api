package backend.grupo130.tramos.dto.ruta.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class RutaRegisterRequest {

    @NotNull(message = "{error.idSolicitud.notNull}")
    @Positive(message = "{error.idSolicitud.positive}")
    private final Long idSolicitud;

    @NotNull(message = "{error.idRuta.notNull}")
    @Positive(message = "{error.idRuta.positive}")
    private final Long idRuta;

}
