package backend.grupo130.ubicaciones.dto.deposito.response;

import backend.grupo130.ubicaciones.data.models.Deposito;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DepositoGetAllResponse {

    private final List<Deposito> depositos;

}
