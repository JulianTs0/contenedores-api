package backend.grupo130.envios.dto.solicitud.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SolicitudConfirmarRuta {

    private final Long idSolicitud;

}
