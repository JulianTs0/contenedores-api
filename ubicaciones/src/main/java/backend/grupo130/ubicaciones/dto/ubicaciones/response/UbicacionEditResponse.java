package backend.grupo130.ubicaciones.dto.ubicaciones.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class UbicacionEditResponse {

    private final Long idUbicacion;

    private final String direccion;

    private final BigDecimal latitud;

    private final BigDecimal longitud;

}
