
package backend.grupo130.camiones.dto.response;

import backend.grupo130.camiones.client.usuarios.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetByIdResponse {

    private final String dominio;

    private final BigDecimal capacidadPeso;

    private final BigDecimal capacidadVolumen;

    private final BigDecimal consumoCombustible;

    private final BigDecimal costoTrasladoBase;

    private final Boolean estado;

    private final Usuario transportista;

}
