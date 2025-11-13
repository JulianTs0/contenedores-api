
package backend.grupo130.camiones.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditResponse {
    private String dominio;
    private String nombreTransportista;
    private String telefonoContacto;
    private Double capacidadPeso;
    private Double capacidadVolumen;
    private Double consumoKm;
    private Double costoKm;
    private Boolean disponible;
    private String observaciones;
}
