package backend.grupo130.tramos.client.envios.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SolicitudCambioDeEstadoResponse {

    private final Long idSolicitud;

    private final String estado;

    private final Long idNuevoSeguimiento;

}
