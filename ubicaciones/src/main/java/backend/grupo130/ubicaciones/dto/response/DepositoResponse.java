
package backend.grupo130.ubicaciones.dto.response;

import lombok.Data;

@Data
public class DepositoResponse {
    private Long id;
    private String nombre;
    private String direccion;
    private Double latitud;
    private Double longitud;
    private Double costoEstadiaDiario;
    private String observaciones;
}
