
package backend.grupo130.ubicaciones.dto.request;

import lombok.Data;

@Data
public class EditDepositoRequest {
    private Long id;
    private String nombre;
    private String direccion;
    private Double latitud;
    private Double longitud;
    private Double costoEstadiaDiario;
    private String observaciones;
}
