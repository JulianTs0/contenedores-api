package backend.grupo130.camiones.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterCamionRequest {
    @NotNull
    private String dominio;
    @NotNull
    private String nombreTransportista;
    @NotNull
    private Double capacidadPeso;
    @NotNull
    private Double capacidadVolumen;
    @NotNull
    private Double consumoKm;
}
