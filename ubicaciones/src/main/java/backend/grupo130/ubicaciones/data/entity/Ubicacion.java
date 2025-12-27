package backend.grupo130.ubicaciones.data.entity;

import backend.grupo130.ubicaciones.data.entity.Deposito;
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
