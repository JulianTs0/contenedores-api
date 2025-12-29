package backend.grupo130.envios.data.entity;

import backend.grupo130.envios.config.enums.EstadoSolicitud;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeguimientoEnvio {

    private Long idSeguimiento;

    private LocalDateTime fechaHoraInicio;

    private LocalDateTime fechaHoraFin;

    private EstadoSolicitud estado;

    private String descripcion;

}
