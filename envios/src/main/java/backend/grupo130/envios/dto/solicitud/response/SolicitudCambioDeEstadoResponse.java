package backend.grupo130.envios.dto.solicitud.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SolicitudCambioDeEstadoResponse {

    private final Long idSolicitud;

    private final String estado;

    private final Long idNuevoSeguimiento;

}
