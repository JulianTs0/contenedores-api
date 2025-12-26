package backend.grupo130.camiones.dto.response;

import backend.grupo130.camiones.client.usuarios.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class AsignarTransportistaResponse {

    private String dominio;

    private BigDecimal capacidadPeso;

    private BigDecimal capacidadVolumen;

    private BigDecimal consumoCombustible;

    private BigDecimal costoTrasladoBase;

    private Usuario transportista;

    private Boolean estado;

}
