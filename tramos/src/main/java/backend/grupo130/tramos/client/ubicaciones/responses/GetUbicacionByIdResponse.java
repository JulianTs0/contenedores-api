package backend.grupo130.tramos.client.ubicaciones.responses;

import backend.grupo130.tramos.client.ubicaciones.models.Deposito;
import backend.grupo130.tramos.client.ubicaciones.models.Ubicacion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetUbicacionByIdResponse {

    private final Long idUbicacion;

    private final String direccion;

    private final BigDecimal latitud;

    private final BigDecimal longitud;

    private final Deposito deposito;


}
