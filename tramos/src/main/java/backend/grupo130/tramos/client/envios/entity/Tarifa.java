package backend.grupo130.tramos.client.envios.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.RoundingMode;

import java.math.BigDecimal;

// TODO: Revisar los datos inutiles con respecto a la solicitud

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarifa {

    private BigDecimal valorLitro;

    private Long idTarifa;

    private BigDecimal pesoMax;

    private BigDecimal volumenMax;

    private BigDecimal costoBase;

    private BigDecimal consumoAprox;

    private BigDecimal costoEstadia;

    public BigDecimal calcularCostoEstimado(BigDecimal distanciaKm, BigDecimal cargosFijosUnitario, int cantidadTramos, long diasEstadia, PreciosNegocio preciosNegocio) {

        BigDecimal valorFijoUnitario = (cargosFijosUnitario != null) ? cargosFijosUnitario : BigDecimal.ZERO;
        BigDecimal cargosFijosTotales = valorFijoUnitario.multiply(new BigDecimal(cantidadTramos));

        BigDecimal baseAjustada = calcularCostoBaseAjustado(preciosNegocio);

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

    public BigDecimal calcularCostoFinal(BigDecimal distanciaTotalKm, BigDecimal cargosFijosTotales, BigDecimal costoTotalEstadiasReales, PreciosNegocio preciosNegocio) {

        BigDecimal baseAjustada = calcularCostoBaseAjustado(preciosNegocio);

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

    private BigDecimal calcularCostoBaseAjustado(PreciosNegocio preciosNegocio) {

        BigDecimal base = (this.costoBase != null) ? this.costoBase : BigDecimal.ZERO;
        BigDecimal peso = (this.pesoMax != null) ? this.pesoMax : BigDecimal.ZERO;
        BigDecimal multiplicador;

        if (peso.compareTo(preciosNegocio.getRangoPesoBajo()) <= 0) {
            multiplicador = preciosNegocio.getMultiplicadorBajo();

        }
        else if (peso.compareTo(preciosNegocio.getRangoPesoMedio()) <= 0){
            multiplicador = preciosNegocio.getMultiplicadorMedio();
        }
        else {
            multiplicador = preciosNegocio.getMultiplicadorAlto();
        }

        return base.multiply(multiplicador);
    }

}
