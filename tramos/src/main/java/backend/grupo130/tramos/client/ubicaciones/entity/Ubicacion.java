package backend.grupo130.tramos.client.ubicaciones.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ubicacion {

    private Long idUbicacion;

    private String direccionTextual;

    private BigDecimal latitud;

    private BigDecimal longitud;

    private Deposito deposito;

}
