
package backend.grupo130.camiones.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotNull
    @Size(min = 1, max = 50)
    private String codigo;

    @NotNull
    private Double pesoMaximo;

    @NotNull
    private Double volumenMaximo;

    private String ubicacionActual;
    private String observaciones;
}
