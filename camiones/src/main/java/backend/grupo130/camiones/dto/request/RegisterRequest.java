
package backend.grupo130.camiones.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    private String dominio;

    @NotBlank
    private String nombreTransportista;

    private String telefonoContacto;

    @NotNull
    private Double capacidadPeso;

    @NotNull
    private Double capacidadVolumen;

    @NotNull
    private Double consumoKm;

    @NotNull
    private Double costoKm;

    private String observaciones;
}
