package backend.grupo130.tramos.dto.ruta.response;

import backend.grupo130.tramos.client.envios.entity.SolicitudTraslado;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class RutaGetByIdResponse {

    private final Long idRuta;

    private final Integer cantidadTramos;

    private final Integer cantidadDepositos;

    private final BigDecimal cargosGestionFijo;

    private final SolicitudTraslado solicitud;
    
}
