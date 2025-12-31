package backend.grupo130.tramos.dto.tramo.response;

import backend.grupo130.tramos.client.camiones.entity.Camion;
import backend.grupo130.tramos.client.ubicaciones.entity.Ubicacion;
import backend.grupo130.tramos.data.entity.RutaTraslado;
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

    private final LocalDateTime fechaHoraInicioEstimado;

    private final LocalDateTime fechaHoraFinEstimado;

    private final LocalDateTime fechaHoraInicioReal;

    private final LocalDateTime fechaHoraFinReal;

    private final Integer orden;

    private final Camion dominioCamion;

    private final RutaTraslado rutaTraslado;

    private final Ubicacion idOrigen;

    private final Ubicacion idDestino;

}
