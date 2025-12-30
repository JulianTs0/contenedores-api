package backend.grupo130.envios.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

// TODO: Revisar los datos inutiles con respecto a la solicitud
// TODO: Cambiar el umbral por una combinacion de peso volumen
// TODO: Cambiar las constantes por llamdas a la bdd

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tarifa {

    private Long idTarifa;

    private BigDecimal pesoMax;

    private BigDecimal volumenMax;

    private BigDecimal costoBase;

    private BigDecimal valorLitro;

    private BigDecimal consumoAprox;

    private BigDecimal costoEstadia;

}
