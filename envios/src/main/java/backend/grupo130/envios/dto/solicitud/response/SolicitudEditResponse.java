package backend.grupo130.envios.dto.solicitud.response;

import backend.grupo130.envios.data.models.Tarifa;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SolicitudEditResponse {

    // Devolvemos todos los campos que se pudieron editar
    private final Long idSolicitud;
    private final String estado;
    private final LocalDateTime fechaInicio;
    private final LocalDateTime fechaFin;
    private final BigDecimal costoEstimado;
    private final BigDecimal costoFinal;
    private final BigDecimal tiempoEstimadoHoras;
    private final BigDecimal tiempoRealHoras;
    private final Tarifa tarifa; // Devolvemos el objeto Tarifa completo (o un DTO)
}
