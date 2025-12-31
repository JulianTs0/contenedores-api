package backend.grupo130.camiones.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CambiarDisponibilidadResponse {

    private final String dominio;

    private final BigDecimal capacidadPeso;

    private final BigDecimal capacidadVolumen;

    private final BigDecimal consumoCombustible;

    private final BigDecimal costoTrasladoBase;

    private final Boolean estado;

}
