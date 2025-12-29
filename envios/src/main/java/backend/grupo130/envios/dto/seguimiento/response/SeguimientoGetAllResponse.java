package backend.grupo130.envios.dto.seguimiento.response;

import backend.grupo130.envios.data.entity.SeguimientoEnvio;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SeguimientoGetAllResponse {

    private final List<SeguimientoEnvio> seguimientos;

}
