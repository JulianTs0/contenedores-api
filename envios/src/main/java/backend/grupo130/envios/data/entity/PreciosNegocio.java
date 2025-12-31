package backend.grupo130.envios.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
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

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;

}
