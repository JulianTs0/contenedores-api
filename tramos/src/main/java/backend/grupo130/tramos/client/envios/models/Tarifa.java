package backend.grupo130.tramos.client.envios.models;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.RoundingMode;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarifa {

    private static final BigDecimal RANGO_PESO_BAJO = new BigDecimal("100");
    private static final BigDecimal RANGO_PESO_MEDIO = new BigDecimal("500");

    private static final BigDecimal MULTIPLICADOR_BAJO = new BigDecimal("1.0");
    private static final BigDecimal MULTIPLICADOR_MEDIO = new BigDecimal("1.5");
    private static final BigDecimal MULTIPLICADOR_ALTO = new BigDecimal("2.0");

    private static final int MONETARY_SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private Long idTarifa;

    private BigDecimal pesoMax;

    private BigDecimal volumenMax;

    private BigDecimal costoBase;

    private BigDecimal valorLitro;

    private BigDecimal consumoAprox;

    private BigDecimal costoEstadia;

    public BigDecimal calcularCostoEstimado(BigDecimal distanciaEnKilometros, BigDecimal cargosFijos) {

        BigDecimal costoBaseAjustado = calcularCostoBaseAjustado();

        BigDecimal consumo = (this.consumoAprox != null) ? this.consumoAprox : BigDecimal.ZERO;
        BigDecimal valor = (this.valorLitro != null) ? this.valorLitro : BigDecimal.ZERO;
        BigDecimal distancia = (distanciaEnKilometros != null) ? distanciaEnKilometros : BigDecimal.ZERO;

        BigDecimal costoCombustible = consumo.multiply(valor).multiply(distancia);

        BigDecimal estadia = (this.costoEstadia != null) ? this.costoEstadia : BigDecimal.ZERO;

         return costoBaseAjustado
            .add(costoCombustible)
            .add(estadia)
            .add(cargosFijos)
            .setScale(MONETARY_SCALE, ROUNDING_MODE);
    }

    private BigDecimal calcularCostoBaseAjustado() {

        BigDecimal base = (this.costoBase != null) ? this.costoBase : BigDecimal.ZERO;
        BigDecimal peso = (this.pesoMax != null) ? this.pesoMax : BigDecimal.ZERO;

        BigDecimal multiplicador;

        if (peso.compareTo(RANGO_PESO_BAJO) <= 0) {
            multiplicador = MULTIPLICADOR_BAJO;
        }
        else if (peso.compareTo(RANGO_PESO_MEDIO) <= 0) {
            multiplicador = MULTIPLICADOR_MEDIO;
        }
        else {
            multiplicador = MULTIPLICADOR_ALTO;
        }

        return base.multiply(multiplicador);
    }

}
