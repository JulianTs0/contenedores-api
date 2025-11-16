package backend.grupo130.envios.dto.tarifa.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TarifaGetAllResponse {

    private final List<TarifaGetByIdResponse> tarifas;

}