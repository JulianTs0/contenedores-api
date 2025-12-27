
package backend.grupo130.ubicaciones.dto.deposito;

import backend.grupo130.ubicaciones.data.entity.Deposito;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoEditRequest;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoEditResponse;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoGetAllResponse;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoGetByIdResponse;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoRegisterResponse;

import java.util.List;
import java.util.stream.Collectors;

public class DepositoMapperDto {

    public static DepositoGetByIdResponse toResponseGetById(Deposito deposito, Long idUbicacion) {
        return new DepositoGetByIdResponse(
            deposito.getIdDeposito(),
            deposito.getNombre(),
            deposito.getCostoEstadiaDiario(),
            idUbicacion
        );
    }

    public static DepositoGetAllResponse toResponseGetAll(List<Deposito> depositos) {
        return new DepositoGetAllResponse(
            depositos
        );
    }

    public static DepositoEditResponse toResponsePatchEdit(Deposito deposito) {
        return new DepositoEditResponse(
            deposito.getIdDeposito(),
            deposito.getNombre(),
            deposito.getCostoEstadiaDiario()
        );
    }

    public static DepositoRegisterResponse toResponsePostRegister(Deposito deposito) {
        return new DepositoRegisterResponse(deposito.getIdDeposito());
    }

    public static DepositoEditRequest toRequestPatchEdit(Long id, DepositoEditRequest body) {
        return DepositoEditRequest.builder()
                .idDeposito(id)
                .nombre(body.getNombre())
                .costoEstadiaDiario(body.getCostoEstadiaDiario())
                .build();
    }

}
