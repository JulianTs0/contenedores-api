package backend.grupo130.tramos.dto.ruta.response;

import backend.grupo130.tramos.data.models.Tramo;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class RutaAniadirTramoResponse {

    private final List<Tramo> tramos;
}
