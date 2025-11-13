
package backend.grupo130.camiones.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetByIdRequest {

    @NotNull
    private String dominio;  // <-- CAMBIA el tipo y el nombre del campo
}