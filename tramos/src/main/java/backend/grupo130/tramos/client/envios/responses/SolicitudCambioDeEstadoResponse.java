package backend.grupo130.tramos.client.envios.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudCambioDeEstadoResponse {
    private Long idSolicitud;
    private String estado;
    private Long idNuevoSeguimiento;
}
