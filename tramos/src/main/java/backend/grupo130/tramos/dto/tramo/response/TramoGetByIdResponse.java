package backend.grupo130.tramos.dto.tramo.response;

import backend.grupo130.tramos.client.camiones.models.Camion;
import backend.grupo130.tramos.client.ubicaciones.models.Ubicacion;
import backend.grupo130.tramos.data.models.RutaTraslado;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TramoGetByIdResponse {

    private final Long idTramo;

    private final String tipoTramo;

    private final String estado;

    private final BigDecimal costoAproximado;

    private final BigDecimal costoReal;

    private LocalDateTime fechaHoraInicioEstimado;

    private LocalDateTime fechaHoraFinEstimado;

    private LocalDateTime fechaHoraInicioReal;

    private LocalDateTime fechaHoraFinReal;

    private Integer orden;

    private Camion dominioCamion;

    private RutaTraslado rutaTraslado;

    private Ubicacion idOrigen;

    private Ubicacion idDestino;

}
