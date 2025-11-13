
package backend.grupo130.ubicaciones.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterDepositoRequest {
    @NotNull
    private String nombre;
    @NotNull
    private String direccion;
    @NotNull
    private Double latitud;
    @NotNull
    private Double longitud;
    @NotNull
    private Double costoEstadiaDiario;
    private String observaciones;
}
