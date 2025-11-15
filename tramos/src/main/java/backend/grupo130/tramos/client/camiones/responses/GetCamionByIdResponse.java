package backend.grupo130.tramos.client.camiones.responses;

import backend.grupo130.tramos.client.usuarios.models.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetCamionByIdResponse {

    private String dominio;

    private BigDecimal capacidadPeso;

    private BigDecimal capacidadVolumen;

    private BigDecimal consumoCombustible;

    private BigDecimal costoTrasladoBase;

    private Boolean estado;

    private Integer idTransportista;

}
