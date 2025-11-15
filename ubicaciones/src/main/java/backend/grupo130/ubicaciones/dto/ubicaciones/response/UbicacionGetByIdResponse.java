
package backend.grupo130.ubicaciones.dto.ubicaciones.response;

import backend.grupo130.ubicaciones.data.models.Deposito;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class UbicacionGetByIdResponse {

    private final Integer ubicacionId;

    private final String direccion;

    private final BigDecimal latitud;

    private final BigDecimal longitud;

    private final Integer idDeposito;

}
