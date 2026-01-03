package backend.grupo130.tramos.client.envios.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SolicitudCambioDeEstadoRequest {

    private final Long idSolicitud;

    private final String nuevoEstado;

    private final String descripcion;

}
