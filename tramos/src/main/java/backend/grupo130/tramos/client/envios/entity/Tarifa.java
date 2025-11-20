package backend.grupo130.tramos.client.envios.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.RoundingMode;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarifa {

    private static final BigDecimal RANGO_PESO_BAJO = new BigDecimal("10000");
    private static final BigDecimal RANGO_PESO_MEDIO = new BigDecimal("50000");

    private static final BigDecimal MULTIPLICADOR_BAJO = new BigDecimal("1.0");
    private static final BigDecimal MULTIPLICADOR_MEDIO = new BigDecimal("1.5");
    private static final BigDecimal MULTIPLICADOR_ALTO = new BigDecimal("2.0");

    private static final int MONETARY_SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private Long idTarifa;

    private BigDecimal pesoMax;

    private BigDecimal volumenMax;

    private BigDecimal costoBase;

    private BigDecimal valorLitro = new BigDecimal("10");

    private BigDecimal consumoAprox;

    private BigDecimal costoEstadia;

    public BigDecimal calcularCostoEstimado(BigDecimal distanciaKm, BigDecimal cargosFijosUnitario, int cantidadTramos, long diasEstadia) {

        BigDecimal valorFijoUnitario = (cargosFijosUnitario != null) ? cargosFijosUnitario : BigDecimal.ZERO;
        BigDecimal cargosFijosTotales = valorFijoUnitario.multiply(new BigDecimal(cantidadTramos));

        BigDecimal baseAjustada = calcularCostoBaseAjustado();

        BigDecimal consumo = (this.consumoAprox != null) ? this.consumoAprox : BigDecimal.ZERO;
        BigDecimal precioLitro = (this.valorLitro != null) ? this.valorLitro : BigDecimal.ZERO;
        BigDecimal distancia = (distanciaKm != null) ? distanciaKm : BigDecimal.ZERO;

        BigDecimal costoCombustible = consumo.multiply(precioLitro).multiply(distancia);

        BigDecimal precioEstadia = (this.costoEstadia != null) ? this.costoEstadia : BigDecimal.ZERO;
        BigDecimal costoTotalEstadia = precioEstadia.multiply(new BigDecimal(diasEstadia));

        return baseAjustada
            .add(costoCombustible)
            .add(costoTotalEstadia)
            .add(cargosFijosTotales)
            .setScale(MONETARY_SCALE, ROUNDING_MODE);
    }

    public BigDecimal calcularCostoFinal(BigDecimal distanciaTotalKm, BigDecimal cargosFijosTotales, BigDecimal costoTotalEstadiasReales) {

        BigDecimal baseAjustada = calcularCostoBaseAjustado();

        BigDecimal consumo = (this.consumoAprox != null) ? this.consumoAprox : BigDecimal.ZERO;
        BigDecimal precioLitro = (this.valorLitro != null) ? this.valorLitro : BigDecimal.ZERO;
        BigDecimal distancia = (distanciaTotalKm != null) ? distanciaTotalKm : BigDecimal.ZERO;

        BigDecimal costoCombustible = consumo.multiply(precioLitro).multiply(distancia);

        BigDecimal costoEstadiaFinal = (costoTotalEstadiasReales != null) ? costoTotalEstadiasReales : BigDecimal.ZERO;
        BigDecimal cargosFijosFinal = (cargosFijosTotales != null) ? cargosFijosTotales : BigDecimal.ZERO;

        return baseAjustada
            .add(costoCombustible)
            .add(costoEstadiaFinal)
            .add(cargosFijosFinal)
            .setScale(MONETARY_SCALE, ROUNDING_MODE);
    }

    private BigDecimal calcularCostoBaseAjustado() {

        BigDecimal base = (this.costoBase != null) ? this.costoBase : BigDecimal.ZERO;
        BigDecimal peso = (this.pesoMax != null) ? this.pesoMax : BigDecimal.ZERO;
        BigDecimal multiplicador;

        if (peso.compareTo(RANGO_PESO_BAJO) <= 0) {
            multiplicador = MULTIPLICADOR_BAJO;

        }
        else if (peso.compareTo(RANGO_PESO_MEDIO) <= 0){
            multiplicador = MULTIPLICADOR_MEDIO;
        }
        else {
            multiplicador = MULTIPLICADOR_ALTO;
        }

        return base.multiply(multiplicador);
    }

}
