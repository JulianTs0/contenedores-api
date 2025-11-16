package backend.grupo130.envios.dto.seguimiento.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeguimientoGetByIdRequest {

    // El ID del seguimiento que se quiere buscar
    private final Long idSeguimiento;

}
