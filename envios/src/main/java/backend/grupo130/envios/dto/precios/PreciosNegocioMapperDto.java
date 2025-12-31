package backend.grupo130.envios.dto.precios;

import backend.grupo130.envios.data.entity.PreciosNegocio;
import backend.grupo130.envios.dto.precios.request.PreciosNegocioCreateRequest;
import backend.grupo130.envios.dto.precios.request.PreciosNegocioUpdateRequest;
import backend.grupo130.envios.dto.precios.response.*;

import java.util.List;

public class PreciosNegocioMapperDto {

    public static PreciosNegocioGetByIdResponse toPreciosNegocioGetByIdResponse(PreciosNegocio entity) {
        if (entity == null) {
            return null;
        }
        return PreciosNegocioGetByIdResponse.builder()
                .idPreciosNegocio(entity.getIdPreciosNegocio())
                .rangoPesoBajo(entity.getRangoPesoBajo())
                .rangoPesoMedio(entity.getRangoPesoMedio())
                .multiplicadorBajo(entity.getMultiplicadorBajo())
                .multiplicadorMedio(entity.getMultiplicadorMedio())
                .multiplicadorAlto(entity.getMultiplicadorAlto())
                .valorLitro(entity.getValorLitro())
                .cargoGestion(entity.getCargoGestion())
                .build();
    }

    public static PreciosNegocioGetLatestResponse toPreciosNegocioGetLatestResponse(PreciosNegocio entity) {
        if (entity == null) {
            return null;
        }
        return PreciosNegocioGetLatestResponse.builder()
                .idPreciosNegocio(entity.getIdPreciosNegocio())
                .rangoPesoBajo(entity.getRangoPesoBajo())
                .rangoPesoMedio(entity.getRangoPesoMedio())
                .multiplicadorBajo(entity.getMultiplicadorBajo())
                .multiplicadorMedio(entity.getMultiplicadorMedio())
                .multiplicadorAlto(entity.getMultiplicadorAlto())
                .valorLitro(entity.getValorLitro())
                .cargoGestion(entity.getCargoGestion())
                .build();
    }

    public static PreciosNegocioCreateResponse toPreciosNegocioCreateResponse(PreciosNegocio entity) {
        if (entity == null) {
            return null;
        }
        return PreciosNegocioCreateResponse.builder()
                .idPreciosNegocio(entity.getIdPreciosNegocio())
                .build();
    }

    public static PreciosNegocioUpdateResponse toPreciosNegocioUpdateResponse(PreciosNegocio entity) {
        if (entity == null) {
            return null;
        }
        return PreciosNegocioUpdateResponse.builder()
                .idPreciosNegocio(entity.getIdPreciosNegocio())
                .rangoPesoBajo(entity.getRangoPesoBajo())
                .rangoPesoMedio(entity.getRangoPesoMedio())
                .multiplicadorBajo(entity.getMultiplicadorBajo())
                .multiplicadorMedio(entity.getMultiplicadorMedio())
                .multiplicadorAlto(entity.getMultiplicadorAlto())
                .valorLitro(entity.getValorLitro())
                .cargoGestion(entity.getCargoGestion())
                .build();
    }

    public static PreciosNegocioGetAllResponse toPreciosNegocioGetAllResponse(List<PreciosNegocio> entities) {
        return new PreciosNegocioGetAllResponse(entities);
    }

    public static PreciosNegocio toPreciosNegocioCreateRequest(PreciosNegocioCreateRequest request) {
        if (request == null) {
            return null;
        }
        PreciosNegocio entity = new PreciosNegocio();
        entity.setRangoPesoBajo(request.getRangoPesoBajo());
        entity.setRangoPesoMedio(request.getRangoPesoMedio());
        entity.setMultiplicadorBajo(request.getMultiplicadorBajo());
        entity.setMultiplicadorMedio(request.getMultiplicadorMedio());
        entity.setMultiplicadorAlto(request.getMultiplicadorAlto());
        entity.setValorLitro(request.getValorLitro());
        entity.setCargoGestion(request.getCargoGestion());
        return entity;
    }

    public static PreciosNegocioUpdateRequest toPreciosNegocioUpdateRequest(Long id, PreciosNegocioUpdateRequest request) {
        if (request == null) {
            return null;
        }
        return PreciosNegocioUpdateRequest.builder()
                .idPreciosNegocio(id)
                .rangoPesoBajo(request.getRangoPesoBajo())
                .rangoPesoMedio(request.getRangoPesoMedio())
                .multiplicadorBajo(request.getMultiplicadorBajo())
                .multiplicadorMedio(request.getMultiplicadorMedio())
                .multiplicadorAlto(request.getMultiplicadorAlto())
                .valorLitro(request.getValorLitro())
                .cargoGestion(request.getCargoGestion())
                .build();
    }
}
