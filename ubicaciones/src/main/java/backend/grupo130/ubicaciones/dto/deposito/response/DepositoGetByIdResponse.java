package backend.grupo130.ubicaciones.dto.deposito.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class DepositoGetByIdResponse {

    private final Long idDeposito;

    private final String nombre;

    private final BigDecimal costoEstadiaDiario;

    private final Long idUbicacion;

}
