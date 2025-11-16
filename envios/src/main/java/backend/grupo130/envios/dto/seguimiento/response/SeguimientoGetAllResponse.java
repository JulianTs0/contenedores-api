package backend.grupo130.envios.dto.seguimiento.response;

import backend.grupo130.envios.data.models.SeguimientoEnvio;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SeguimientoGetAllResponse {

    // Siguiendo el ejemplo de Contenedor, devolvemos la lista de entidades
    private final List<SeguimientoEnvio> seguimientos;

}
