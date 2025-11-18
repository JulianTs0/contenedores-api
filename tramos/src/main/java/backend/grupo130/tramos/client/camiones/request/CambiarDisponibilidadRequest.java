package backend.grupo130.tramos.client.camiones.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CambiarDisponibilidadRequest {

    private final String dominio;

    private final Boolean estado;

}
