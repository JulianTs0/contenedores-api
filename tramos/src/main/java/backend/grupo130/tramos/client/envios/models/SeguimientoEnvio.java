package backend.grupo130.tramos.client.envios.models;

import backend.grupo130.tramos.config.enums.EstadoSolicitud;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeguimientoEnvio {

    private Long idSeguimiento;

    private LocalDateTime fechaHoraInicio;

    private LocalDateTime fechaHoraFin;

    private EstadoSolicitud estado;

    private String descripcion;

}
