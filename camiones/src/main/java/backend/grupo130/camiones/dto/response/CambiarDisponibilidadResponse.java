package backend.grupo130.camiones.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CambiarDisponibilidadResponse {

    private String dominio;

    private BigDecimal capacidadPeso;

    private BigDecimal capacidadVolumen;

    private BigDecimal consumoCombustible;

    private BigDecimal costoTrasladoBase;

    private Boolean estado;

}
