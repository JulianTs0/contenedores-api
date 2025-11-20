package backend.grupo130.ubicaciones.dto.deposito;

import backend.grupo130.ubicaciones.data.models.Deposito;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoEditResponse;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoGetAllResponse;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoGetByIdResponse;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoRegisterResponse;

import java.util.List;

public class DepositoMapperDto {

    public static DepositoGetByIdResponse toResponseGet(Deposito deposito, Long idUbicacion) {
        return new DepositoGetByIdResponse(
            deposito.getIdDeposito(),
            deposito.getNombre(),
            deposito.getCostoEstadiaDiario(),
            idUbicacion
        );
    }

    public static DepositoGetAllResponse toResponseGet(List<Deposito> depositos) {
        return new DepositoGetAllResponse(
            depositos
        );
    }

    public static DepositoEditResponse toResponsePatch(Deposito deposito) {
        return new DepositoEditResponse(
            deposito.getIdDeposito(),
            deposito.getNombre(),
            deposito.getCostoEstadiaDiario()
        );
    }

    public static DepositoRegisterResponse toResponsePost(Deposito deposito) {
        return new DepositoRegisterResponse(deposito.getIdDeposito());
    }

}
