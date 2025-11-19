package backend.grupo130.tramos.client.camiones.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CamionCambiarDisponibilidadRequest {

    private final String dominio;

    private final Boolean estado;

}
