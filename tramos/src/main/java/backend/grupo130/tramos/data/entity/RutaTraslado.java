package backend.grupo130.tramos.data.entity;

import backend.grupo130.tramos.client.envios.entity.SolicitudTraslado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RutaTraslado {

    private Long idRuta;

    private Integer cantidadTramos;

    private Integer cantidadDepositos;

    private BigDecimal cargosGestionFijo;

    private SolicitudTraslado solicitud;

}
