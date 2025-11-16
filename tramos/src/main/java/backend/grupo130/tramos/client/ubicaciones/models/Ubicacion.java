package backend.grupo130.tramos.client.ubicaciones.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ubicacion {

    private Long idUbicacion;

    private String direccionTextual;

    private BigDecimal latitud;

    private BigDecimal longitud;

    private Deposito deposito;

}
