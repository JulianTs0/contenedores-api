package backend.grupo130.tramos.client.ubicaciones.responses;

import backend.grupo130.tramos.client.ubicaciones.models.Deposito;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetDepositoByIdResponse {

    private final Integer idDeposito;

    private final String nombre;

    private final BigDecimal costoEstadiaDiario;

    private final Integer idUbicacion;

}
