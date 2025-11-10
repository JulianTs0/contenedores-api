package backend.grupo130.camiones.dto.response;

import lombok.Data;

@Data
public class CamionResponse {
    private String dominio;
    private String nombreTransportista;
    private Double capacidadPeso;
    private Double capacidadVolumen;
    private Double consumoKm;
    private Boolean disponible;
    private String telefonoContacto;
    private Double costoKm;
}
