package backend.grupo130.envios.dto.tarifa;

import backend.grupo130.envios.data.models.Tarifa;
import backend.grupo130.envios.dto.tarifa.response.TarifaEditResponse;
import backend.grupo130.envios.dto.tarifa.response.TarifaGetAllResponse;
import backend.grupo130.envios.dto.tarifa.response.TarifaGetByIdResponse;
import backend.grupo130.envios.dto.tarifa.response.TarifaRegisterResponse;

import java.util.List;
import java.util.stream.Collectors;

public class TarifaMapperDto {

    public static TarifaRegisterResponse toResponsePost(Tarifa tarifa){
        return new TarifaRegisterResponse(
                tarifa.getIdTarifa()
        );
    }

    public static TarifaGetByIdResponse toResponseGetById(Tarifa tarifa) {
        return new TarifaGetByIdResponse(
                tarifa.getIdTarifa(),
                tarifa.getPesoMax(),
                tarifa.getVolumenMax(),
                tarifa.getCostoBase(),
                tarifa.getValorLitro(),
                tarifa.getConsumoAprox(),
                tarifa.getCostoEstadia()
        );
    }

    public static TarifaEditResponse toResponseEdit(Tarifa tarifa) {
        return new TarifaEditResponse(
                tarifa.getIdTarifa(),
                tarifa.getPesoMax(),
                tarifa.getVolumenMax(),
                tarifa.getCostoBase(),
                tarifa.getValorLitro(),
                tarifa.getConsumoAprox(),
                tarifa.getCostoEstadia()
        );
    }

    public static TarifaGetAllResponse toResponseGetAll(List<Tarifa> tarifas) {

        List<TarifaGetByIdResponse> tarifaResponses = tarifas.stream()
                .map(TarifaMapperDto::toResponseGetById)
                .collect(Collectors.toList());
        return new TarifaGetAllResponse(tarifaResponses);
    }

}
