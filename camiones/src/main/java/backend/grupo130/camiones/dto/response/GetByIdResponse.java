
package backend.grupo130.camiones.dto.response;

import backend.grupo130.camiones.client.usuarios.models.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetByIdResponse {

    private String dominio;

    private BigDecimal capacidadPeso;

    private BigDecimal capacidadVolumen;

    private BigDecimal consumoCombustible;

    private BigDecimal costoTrasladoBase;

    private Boolean estado;

    private Integer idTransportista;

}
