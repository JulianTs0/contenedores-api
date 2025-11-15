package backend.grupo130.tramos.client.camiones.models;

import backend.grupo130.tramos.config.enums.Estado;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Camion {

    private String dominio;

    private BigDecimal capacidadPeso;

    private BigDecimal capacidadVolumen;

    private BigDecimal consumoCombustible;

    private BigDecimal costoTrasladoBase;

    private Boolean estado;

    private Integer idTransportista;

}
