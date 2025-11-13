
package backend.grupo130.camiones.dto.request;

import backend.grupo130.camiones.config.enums.Estado;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EditRequest {
    @NotNull
    private Long id;

    private Double pesoMaximo;
    private Double volumenMaximo;
    private String ubicacionActual;
    private String observaciones;
    private Estado estado;
    private Boolean disponible;
}
