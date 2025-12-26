package backend.grupo130.camiones.dto;

import backend.grupo130.camiones.client.usuarios.entity.Usuario;
import backend.grupo130.camiones.data.entity.Camion;
import backend.grupo130.camiones.dto.request.AsignarTransportistaRequest;
import backend.grupo130.camiones.dto.request.CambiarDisponibilidadRequest;
import backend.grupo130.camiones.dto.request.EditRequest;
import backend.grupo130.camiones.dto.response.*;

import java.math.BigDecimal;
import java.util.List;

public class CamionesMapperDto {

    public static GetByIdResponse toResponseGetId(Camion camion) {
        return new GetByIdResponse(
            camion.getDominio(),
            camion.getCapacidadPeso(),
            camion.getCapacidadVolumen(),
            camion.getConsumoCombustible(),
            camion.getCostoTrasladoBase(),
            camion.getEstado(),
            camion.getTransportista()
        );
    }

    public static GetAllResponse toResponseGetAll(List<Camion> camiones) {
        return new GetAllResponse(
            camiones.stream().map(CamionesMapperDto::toResponseGetId).toList()
        );
    }

    public static EditRequest toRequestPatchEdit(String dominio, EditRequest body) {
        return EditRequest.builder()
            .dominio(dominio)
            .capacidadPeso(body.getCapacidadPeso())
            .capacidadVolumen(body.getCapacidadVolumen())
            .consumoCombustible(body.getConsumoCombustible())
            .costoTrasladoBase(body.getCostoTrasladoBase())
            .build();
    }

    public static EditResponse toResponsePatchEdit(Camion camion) {
        return new EditResponse(
            camion.getDominio(),
            camion.getCapacidadPeso(),
            camion.getCapacidadVolumen(),
            camion.getConsumoCombustible(),
            camion.getCostoTrasladoBase(),
            camion.getEstado()
        );
    }

    public static CambiarDisponibilidadRequest toRequestPatchDispo(String dominio, CambiarDisponibilidadRequest body) {
        return CambiarDisponibilidadRequest.builder()
            .dominio(dominio)
            .estado(body.getEstado())
            .build();
    }

    public static CambiarDisponibilidadResponse toResponsePatchDispo(Camion camion) {
        return new CambiarDisponibilidadResponse(
            camion.getDominio(),
            camion.getCapacidadPeso(),
            camion.getCapacidadVolumen(),
            camion.getConsumoCombustible(),
            camion.getCostoTrasladoBase(),
            camion.getEstado()
        );
    }

    public static AsignarTransportistaRequest toRequestPatchTrans(String dominio, AsignarTransportistaRequest body) {
        return AsignarTransportistaRequest.builder()
            .dominio(dominio)
            .idTransportista(body.getIdTransportista())
            .build();
    }

    public static AsignarTransportistaResponse toResponsePatchTrans(Camion camion, Usuario usuario) {
        return new AsignarTransportistaResponse(
            camion.getDominio(),
            camion.getCapacidadPeso(),
            camion.getCapacidadVolumen(),
            camion.getConsumoCombustible(),
            camion.getCostoTrasladoBase(),
            usuario,
            camion.getEstado()
        );
    }

    public static GetPromedioCostoBaseResponse toResponsePromedioCostoBase(BigDecimal promedio){
        return new GetPromedioCostoBaseResponse(
            promedio
        );
    }

    public static GetPromedioCombustibleActualResponse toResponsePromedioCombustible(BigDecimal promedio){
        return new GetPromedioCombustibleActualResponse(
            promedio
        );
    }

    public static RegisterResponse toResponsePostRegister(Camion camion) {
        return new RegisterResponse(camion.getDominio());
    }

}
