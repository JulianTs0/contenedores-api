package backend.grupo130.tramos.client.envios.request;

import backend.grupo130.tramos.client.envios.models.Tarifa;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudEditRequest {

    private Long idSolicitud;

    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;

    private BigDecimal costoEstimado;

    private BigDecimal costoFinal;

    private BigDecimal tiempoEstimadoHoras;

    private BigDecimal tiempoRealHoras;

    private Tarifa tarifa;

    private Long idOrigen;

    private Long idDestino;
}
