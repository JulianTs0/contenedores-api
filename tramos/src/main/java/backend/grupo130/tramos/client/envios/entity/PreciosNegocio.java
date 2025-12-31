package backend.grupo130.tramos.client.envios.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreciosNegocio {

    private Long idPreciosNegocio;

    private BigDecimal rangoPesoBajo;

    private BigDecimal rangoPesoMedio;

    private BigDecimal multiplicadorBajo;

    private BigDecimal multiplicadorMedio;

    private BigDecimal multiplicadorAlto;

    private BigDecimal valorLitro;

    private BigDecimal cargoGestion;

}
