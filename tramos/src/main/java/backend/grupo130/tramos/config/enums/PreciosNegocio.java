package backend.grupo130.tramos.config.enums;


import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum PreciosNegocio {

    RANGO_PESO_BAJO(new BigDecimal("10000")),
    RANGO_PESO_MEDIO(new BigDecimal("50000")),
    MULTIPLICADOR_BAJO(new BigDecimal("1.0")),
    MULTIPLICADOR_MEDIO(new BigDecimal("1.5")),
    MULTIPLICADOR_ALTO(new BigDecimal("2.0")),
    VALOR_LITRO(new BigDecimal("10")),
    CARGO_GESTION(new BigDecimal("100"));

    private final BigDecimal valor;

    PreciosNegocio(BigDecimal valor) {
        this.valor = valor;
    }

}
