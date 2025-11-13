
package backend.grupo130.camiones.dto.request;

import lombok.Data;

@Data
public class EditRequest {
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