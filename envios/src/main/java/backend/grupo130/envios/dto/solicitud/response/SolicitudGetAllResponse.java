package backend.grupo130.envios.dto.solicitud.response;

import backend.grupo130.envios.data.models.SolicitudTraslado;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SolicitudGetAllResponse {
    private final List<SolicitudTraslado> solicitudes;
}
