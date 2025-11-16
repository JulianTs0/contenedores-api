package backend.grupo130.envios.dto.seguimiento.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SeguimientoGetByIdResponse {

    private final Long idSeguimiento;

    private final LocalDateTime fechaHoraInicio;

    private final LocalDateTime fechaHoraFin;

    private final String estado;

    private final String descripcion;

}
