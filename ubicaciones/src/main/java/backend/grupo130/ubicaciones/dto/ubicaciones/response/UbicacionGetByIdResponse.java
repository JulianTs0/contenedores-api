
package backend.grupo130.ubicaciones.dto.ubicaciones.response;

import backend.grupo130.ubicaciones.data.entity.Deposito;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class UbicacionGetByIdResponse {

    private final Long idUbicacion;

    private final String direccion;

    private final BigDecimal latitud;

    private final BigDecimal longitud;

    private final Deposito deposito;

}
