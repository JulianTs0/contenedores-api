package backend.grupo130.tramos.dto.ruta.response;

import backend.grupo130.tramos.client.envios.entity.Tarifa;
import backend.grupo130.tramos.data.models.RutaTraslado;
import backend.grupo130.tramos.data.models.Tramo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class RutaGetOpcionesResponse {

    private final Tarifa tarifaAprox;

    private final BigDecimal tiempoEstimado;

    private final RutaTraslado ruta;

    private final List<Tramo> tramos;

}
