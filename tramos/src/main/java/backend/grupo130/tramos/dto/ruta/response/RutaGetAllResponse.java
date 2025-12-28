package backend.grupo130.tramos.dto.ruta.response;

import backend.grupo130.tramos.data.entity.RutaTraslado;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RutaGetAllResponse {

    private final List<RutaTraslado> rutas;

}
