package backend.grupo130.tramos.dto.tramo.request;

import backend.grupo130.tramos.data.models.RutaTraslado;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TramoRegisterRequest {

    @NotNull(message = "La ruta es obligatoria")
    private RutaTraslado rutaTraslado;

    @NotNull(message = "La id de origen es obligatoria")
    @Positive(message = "El ID del origen debe ser un número positivo")
    private Integer idOrigen;

    @NotNull(message = "La id de destino es obligatoria")
    @Positive(message = "El ID del destino debe ser un número positivo")
    private Integer idDestino;

    @NotNull(message = "El orden es obligatorio")
    @Positive(message = "El orden debe ser un número positivo")
    private Integer orden;

    @NotNull(message = "La fecha Hora Inicio es obligatoria")
    private LocalDateTime fechaHoraIncio;

}
