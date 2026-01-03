package backend.grupo130.tramos.client.camiones.responses;

import backend.grupo130.tramos.config.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class CamionGetCamionByIdResponse {

    private final String dominio;

    private final BigDecimal capacidadPeso;

    private final BigDecimal capacidadVolumen;

    private final BigDecimal consumoCombustible;

    private final BigDecimal costoTrasladoBase;

    private final Boolean estado;

    private final Usuario transportista;

}
