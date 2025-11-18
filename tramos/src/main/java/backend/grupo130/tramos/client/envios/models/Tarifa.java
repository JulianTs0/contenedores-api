package backend.grupo130.tramos.client.envios.models;

import backend.grupo130.tramos.client.camiones.models.Camion;
import backend.grupo130.tramos.config.enums.TipoTramo;
import backend.grupo130.tramos.data.models.Tramo;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.RoundingMode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

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

    // A cargar segun el contenedor
    private BigDecimal pesoMax;

    // A cargar segun el contenedor
    private BigDecimal volumenMax;

    // A calcular del costo base de los camiones
    private BigDecimal costoBase;

    // valor preconfigurado
    private BigDecimal valorLitro = new BigDecimal("10");

    // Sacar el promedio con la BDD
    private BigDecimal consumoAprox;

    // A calcular
    private BigDecimal costoEstadia;

    public BigDecimal calcularCostoEstimado(BigDecimal distanciaEnKilometros, BigDecimal cargosFijos, int cantidadTramos) {

        BigDecimal costoBaseAjustado = calcularCostoBaseAjustado();

        BigDecimal cargosFijosTotales = cargosFijos.add(new BigDecimal(cantidadTramos));

        BigDecimal consumo = (this.consumoAprox != null) ? this.consumoAprox : BigDecimal.ZERO;
        BigDecimal valor = (this.valorLitro != null) ? this.valorLitro : BigDecimal.ZERO;
        BigDecimal distancia = (distanciaEnKilometros != null) ? distanciaEnKilometros : BigDecimal.ZERO;

        BigDecimal costoCombustible = consumo.multiply(valor).multiply(distancia);

        BigDecimal estadia = (this.costoEstadia != null) ? this.costoEstadia : BigDecimal.ZERO;

         return costoBaseAjustado
            .add(costoCombustible)
            .add(estadia)
            .add(cargosFijosTotales)
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

    public BigDecimal calcularCostoFinal(BigDecimal costoAprox, List<Tramo> tramos) {
        BigDecimal costoTotal = BigDecimal.ZERO;

        if (costoAprox != null) {
            costoTotal = costoAprox;
        }

        for (int i = 0; i < tramos.size() - 1; i++) {
            Tramo actual = tramos.get(i);

            boolean esTramoDeposito = actual.getTipoTramo().equals(TipoTramo.ORIGEN_DEPOSITO) ||
                actual.getTipoTramo().equals(TipoTramo.DEPOSITO_DEPOSITO);

            if (esTramoDeposito) {
                Tramo siguiente = tramos.get(i + 1);

                LocalDateTime finEstadia = actual.getFechaHoraFinReal();
                LocalDateTime inicioSiguiente = siguiente.getFechaHoraInicioReal();

                if (finEstadia != null && inicioSiguiente != null && this.costoEstadia != null) {

                    long horasEstadia = ChronoUnit.HOURS.between(finEstadia, inicioSiguiente);

                    if (horasEstadia > 0) {
                        long diasEstadia = (long) Math.ceil(horasEstadia / 24.0);
                        BigDecimal costoEstadiaCalc = this.costoEstadia.multiply(new BigDecimal(diasEstadia));
                        costoTotal = costoTotal.add(costoEstadiaCalc);
                    }
                }
            }
        }
        return costoTotal;
    }

}
