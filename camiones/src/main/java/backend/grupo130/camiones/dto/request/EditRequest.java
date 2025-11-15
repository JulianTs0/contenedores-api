
package backend.grupo130.camiones.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class EditRequest {

    @NotBlank(message = "{error.dominio.notBlank}")
    @Size(max = 80, message = "{error.dominio.max}")
    private String dominio;

    @Positive(message = "{error.capacidadPeso.positve}")
    @Digits(integer = 10, fraction = 2, message = "{error.capacidadPeso.digits}")
    private BigDecimal capacidadPeso;

    @Positive(message = "{error.capacidadVolumen.positve}")
    @Digits(integer = 10, fraction = 2, message = "{error.capacidadVolumen.digits}")
    private BigDecimal capacidadVolumen;

    @Positive(message = "{error.consumoCombustible.positve}")
    @Digits(integer = 10, fraction = 2, message = "{error.consumoCombustible.digits}")
    private BigDecimal consumoCombustible;

    @PositiveOrZero(message = "{error.costoTrasladoBase.positiveOrZero}")
    @Digits(integer = 10, fraction = 2, message = "{error.costoTrasladoBase.digits}")
    private BigDecimal costoTrasladoBase;

   // @Positive(message = "{error.idTransportista.positve}")
   // private Integer idTransportista;

}
