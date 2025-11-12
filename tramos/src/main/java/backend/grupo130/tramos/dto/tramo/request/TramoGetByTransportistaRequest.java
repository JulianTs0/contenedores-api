package backend.grupo130.tramos.dto.tramo.request;

import backend.grupo130.tramos.data.models.RutaTraslado;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TramoGetByTransportistaRequest {

    private final String dominioCamion;

}
