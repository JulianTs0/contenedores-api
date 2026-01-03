package backend.grupo130.tramos.client.envios.entity;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.RoundingMode;

import java.math.BigDecimal;


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

    private BigDecimal costoEstimado;

    private BigDecimal costoFinal;

    public void calcularCostoEstimado(BigDecimal distanciaKm, BigDecimal cargosFijosUnitario, int cantidadTramos, long diasEstadia, PreciosNegocio preciosNegocio) {

        BigDecimal valorFijoUnitario = (cargosFijosUnitario != null) ? cargosFijosUnitario : BigDecimal.ZERO;
        BigDecimal cargosFijosTotales = valorFijoUnitario.multiply(new BigDecimal(cantidadTramos));

        BigDecimal baseAjustada = calcularCostoBaseAjustado(preciosNegocio);

        BigDecimal consumo = (this.consumoAprox != null) ? this.consumoAprox : BigDecimal.ZERO;
        BigDecimal precioLitro = (this.valorLitro != null) ? this.valorLitro : BigDecimal.ZERO;
        BigDecimal distancia = (distanciaKm != null) ? distanciaKm : BigDecimal.ZERO;

        BigDecimal costoCombustible = consumo.multiply(precioLitro).multiply(distancia);

        BigDecimal precioEstadia = (this.costoEstadia != null) ? this.costoEstadia : BigDecimal.ZERO;
        BigDecimal costoTotalEstadia = precioEstadia.multiply(new BigDecimal(diasEstadia));

        BigDecimal costoEstimado = baseAjustada
            .add(costoCombustible)
            .add(costoTotalEstadia)
            .add(cargosFijosTotales)
            .setScale(2, RoundingMode.HALF_UP);

        this.setCostoEstimado(costoEstimado);

    }

    public void calcularCostoFinal(BigDecimal distanciaTotalKm, BigDecimal cargosFijosTotales, BigDecimal costoTotalEstadiasReales, PreciosNegocio preciosNegocio) {

        BigDecimal baseAjustada = calcularCostoBaseAjustado(preciosNegocio);

        BigDecimal consumo = (this.consumoAprox != null) ? this.consumoAprox : BigDecimal.ZERO;
        BigDecimal precioLitro = (this.valorLitro != null) ? this.valorLitro : BigDecimal.ZERO;
        BigDecimal distancia = (distanciaTotalKm != null) ? distanciaTotalKm : BigDecimal.ZERO;

        BigDecimal costoCombustible = consumo.multiply(precioLitro).multiply(distancia);

        BigDecimal costoEstadiaFinal = (costoTotalEstadiasReales != null) ? costoTotalEstadiasReales : BigDecimal.ZERO;
        BigDecimal cargosFijosFinal = (cargosFijosTotales != null) ? cargosFijosTotales : BigDecimal.ZERO;

        BigDecimal costoFinal = baseAjustada
            .add(costoCombustible)
            .add(costoEstadiaFinal)
            .add(cargosFijosFinal)
            .setScale(2, RoundingMode.HALF_UP);

        this.setCostoFinal(costoFinal);

    }

    private BigDecimal calcularCostoBaseAjustado(PreciosNegocio preciosNegocio) {

        if(this.pesoMax == null || this.volumenMax == null || this.costoBase == null){
            return BigDecimal.ZERO;
        }

        BigDecimal comparacion = this.pesoMax.multiply(this.volumenMax);

        BigDecimal multiplicador;

        if (comparacion.compareTo(preciosNegocio.getRangoPesoBajo()) <= 0) {
            multiplicador = preciosNegocio.getMultiplicadorBajo();
        }
        else if (comparacion.compareTo(preciosNegocio.getRangoPesoMedio()) <= 0){
            multiplicador = preciosNegocio.getMultiplicadorMedio();
        }
        else {
            multiplicador = preciosNegocio.getMultiplicadorAlto();
        }

        return this.costoBase.multiply(multiplicador);
    }

}
