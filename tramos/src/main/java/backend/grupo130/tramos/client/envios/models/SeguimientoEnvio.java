package backend.grupo130.tramos.client.envios.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeguimientoEnvio {

    private Integer idSeguimiento;

    private LocalDateTime fechaHoraInicio;

    private LocalDateTime fechaHoraFin;

    private String estado;

    private String descripcion;

    private Integer idSolicitud;

}
