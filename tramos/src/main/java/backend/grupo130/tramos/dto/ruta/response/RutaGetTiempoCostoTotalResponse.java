package backend.grupo130.tramos.dto.ruta.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RutaGetTiempoCostoTotalResponse {

    private final Double tiempoTotal;

    private final Double costoTotal;

}
