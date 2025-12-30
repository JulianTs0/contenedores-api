package backend.grupo130.tramos.client.envios.entity;

import backend.grupo130.tramos.config.enums.PreciosNegocio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.RoundingMode;

import java.math.BigDecimal;

// TODO: Revisar los datos inutiles con respecto a la solicitud
// TODO: Cambiar el umbral por una combinacion de peso volumen
// TODO: Cambiar las constantes por llamdas a la bdd

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarifa {

    private static final BigDecimal RANGO_PESO_BAJO = PreciosNegocio.RANGO_PESO_BAJO.getValor();

    private static final BigDecimal RANGO_PESO_MEDIO = PreciosNegocio.RANGO_PESO_MEDIO.getValor();

    private static final BigDecimal MULTIPLICADOR_BAJO = PreciosNegocio.MULTIPLICADOR_BAJO.getValor();

    private static final BigDecimal MULTIPLICADOR_MEDIO = PreciosNegocio.MULTIPLICADOR_MEDIO.getValor();

    private static final BigDecimal MULTIPLICADOR_ALTO = PreciosNegocio.MULTIPLICADOR_ALTO.getValor();

    private Long idTarifa;

    private BigDecimal pesoMax;

    private BigDecimal volumenMax;

    private BigDecimal costoBase;

    private BigDecimal valorLitro = PreciosNegocio.VALOR_LITRO.getValor();

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
            .setScale(2, RoundingMode.HALF_UP);
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
            .setScale(2, RoundingMode.HALF_UP);
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
