package backend.grupo130.tramos.client.envios.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudCambioDeEstadoRequest {
    private Long idSolicitud;
    private String nuevoEstado;
    private String descripcion;
}
