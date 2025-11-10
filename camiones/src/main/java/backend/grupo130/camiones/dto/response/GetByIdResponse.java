
package backend.grupo130.camiones.dto.response;

import backend.grupo130.camiones.config.enums.Estado;
import lombok.Data;

@Data
public class GetByIdResponse {
    private Long id;
    private String codigo;
    private Double pesoMaximo;
    private Double volumenMaximo;
    private Estado estado;
    private String ubicacionActual;
    private Boolean disponible;
    private String observaciones;
}
