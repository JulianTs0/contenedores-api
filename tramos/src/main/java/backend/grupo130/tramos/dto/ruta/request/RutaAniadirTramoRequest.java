package backend.grupo130.tramos.dto.ruta.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RutaAniadirTramoRequest {

    @NotNull(message = "La id de ruta es obligatoria")
    @Positive(message = "El ID del ruta debe ser un número positivo")
    private Integer idRutaTraslado;

    @NotNull(message = "El tipo de tramo es obligatorio")
    @NotBlank(message = "El tipoTramo no puede estar vacio")
    @Size(max = 20, message = "El nombre no puede exceder los 20 caracteres")
    private String tipoTramo;

    @NotNull(message = "La id de origen es obligatoria")
    @Positive(message = "El ID del origen debe ser un número positivo")
    private Integer idOrigen;

    @NotNull(message = "La id de destino es obligatoria")
    @Positive(message = "El ID del destino debe ser un número positivo")
    private Integer idDestino;

    @NotNull(message = "El orden es obligatorio")
    @Positive(message = "El orden debe ser un número positivo")
    private Integer orden;
}
