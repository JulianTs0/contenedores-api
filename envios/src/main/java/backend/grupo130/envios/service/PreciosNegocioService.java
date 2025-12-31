package backend.grupo130.envios.service;

import backend.grupo130.envios.config.enums.Errores;
import backend.grupo130.envios.config.exceptions.ServiceError;
import backend.grupo130.envios.data.entity.PreciosNegocio;
import backend.grupo130.envios.dto.precios.PreciosNegocioMapperDto;
import backend.grupo130.envios.dto.precios.request.PreciosNegocioCreateRequest;
import backend.grupo130.envios.dto.precios.request.PreciosNegocioGetByIdRequest;
import backend.grupo130.envios.dto.precios.request.PreciosNegocioUpdateRequest;
import backend.grupo130.envios.dto.precios.response.*;
import backend.grupo130.envios.repository.PreciosNegocioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class PreciosNegocioService {

    private final PreciosNegocioRepository preciosNegocioRepository;

    public PreciosNegocioGetByIdResponse getById(PreciosNegocioGetByIdRequest request) throws ServiceError {
        log.info("Buscando precios de negocio con ID: {}", request.getIdPreciosNegocio());

        PreciosNegocio preciosNegocio = this.preciosNegocioRepository.getById(request.getIdPreciosNegocio());

        if (preciosNegocio == null) {
            throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
        }

        return PreciosNegocioMapperDto.toPreciosNegocioGetByIdResponse(preciosNegocio);
    }

    public PreciosNegocioGetLatestResponse getLatest() throws ServiceError {
        log.info("Buscando los precios de negocio más recientes");

        PreciosNegocio preciosNegocio = this.preciosNegocioRepository.getLatest();

        if (preciosNegocio == null) {
            throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
        }

        return PreciosNegocioMapperDto.toPreciosNegocioGetLatestResponse(preciosNegocio);
    }

    public PreciosNegocioGetAllResponse getAll() {
        log.info("Buscando todos los precios de negocio");

        List<PreciosNegocio> preciosNegocios = this.preciosNegocioRepository.getAll();

        return PreciosNegocioMapperDto.toPreciosNegocioGetAllResponse(preciosNegocios);
    }

    public PreciosNegocioCreateResponse create(PreciosNegocioCreateRequest request) {
        log.info("Creando nuevos precios de negocio");

        PreciosNegocio preciosNegocio = PreciosNegocioMapperDto.toPreciosNegocioCreateRequest(request);

        PreciosNegocio saved = this.preciosNegocioRepository.save(preciosNegocio);

        return PreciosNegocioMapperDto.toPreciosNegocioCreateResponse(saved);
    }

    public PreciosNegocioUpdateResponse update(PreciosNegocioUpdateRequest request) throws ServiceError {
        log.info("Actualizando precios de negocio con ID: {}", request.getIdPreciosNegocio());

        PreciosNegocio existing = this.preciosNegocioRepository.getById(request.getIdPreciosNegocio());

        if (existing == null) {
            throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
        }

        existing.setRangoPesoBajo(request.getRangoPesoBajo());
        existing.setRangoPesoMedio(request.getRangoPesoMedio());
        existing.setMultiplicadorBajo(request.getMultiplicadorBajo());
        existing.setMultiplicadorMedio(request.getMultiplicadorMedio());
        existing.setMultiplicadorAlto(request.getMultiplicadorAlto());
        existing.setValorLitro(request.getValorLitro());
        existing.setCargoGestion(request.getCargoGestion());

        PreciosNegocio updated = this.preciosNegocioRepository.update(existing);
        return PreciosNegocioMapperDto.toPreciosNegocioUpdateResponse(updated);
    }

    public void delete(PreciosNegocioGetByIdRequest request) throws ServiceError {
        log.info("Eliminando precios de negocio con ID: {}", request.getIdPreciosNegocio());

        PreciosNegocio existing = this.preciosNegocioRepository.getById(request.getIdPreciosNegocio());

        if (existing == null) {
            throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
        }

        this.preciosNegocioRepository.delete(request.getIdPreciosNegocio());
    }
}
