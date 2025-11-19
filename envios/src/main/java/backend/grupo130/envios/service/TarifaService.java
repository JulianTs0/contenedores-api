package backend.grupo130.envios.service;

import backend.grupo130.envios.config.enums.Errores;
import backend.grupo130.envios.config.exceptions.ServiceError;
import backend.grupo130.envios.data.models.Tarifa;
import backend.grupo130.envios.dto.tarifa.TarifaMapperDto;
import backend.grupo130.envios.dto.tarifa.request.TarifaDeleteRequest;
import backend.grupo130.envios.dto.tarifa.request.TarifaEditRequest;
import backend.grupo130.envios.dto.tarifa.request.TarifaGetByIdRequest;
import backend.grupo130.envios.dto.tarifa.request.TarifaRegisterRequest;
import backend.grupo130.envios.dto.tarifa.response.TarifaEditResponse;
import backend.grupo130.envios.dto.tarifa.response.TarifaGetAllResponse;
import backend.grupo130.envios.dto.tarifa.response.TarifaGetByIdResponse;
import backend.grupo130.envios.dto.tarifa.response.TarifaRegisterResponse;
import backend.grupo130.envios.repository.TarifaRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class TarifaService {

    private final TarifaRepository tarifaRepository;

    public TarifaRegisterResponse register(TarifaRegisterRequest request) throws ServiceError {
        log.info("Inicio register. Registrando nueva tarifa.");
        try {
            Tarifa tarifa = new Tarifa();
            tarifa.setConsumoAprox(request.getConsumoAprox());
            tarifa.setCostoBase(request.getCostoBase());
            tarifa.setVolumenMax(request.getVolumenMax());
            tarifa.setValorLitro(request.getValorLitro());
            tarifa.setConsumoAprox(request.getConsumoAprox());
            tarifa.setCostoEstadia(request.getCostoEstadia());

            Tarifa saved = this.tarifaRepository.save(tarifa);
            log.info("Tarifa registrada exitosamente. Nueva ID: {}", tarifa.getIdTarifa());

            TarifaRegisterResponse response = TarifaMapperDto.toResponsePost(saved);
            return response;

        } catch (ServiceError ex) {
            log.warn("ServiceError en register (Tarifa): {} - {}", ex.getHttpCode(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno al registrar tarifa: {}", ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public TarifaEditResponse edit(TarifaEditRequest request) throws ServiceError {
        log.info("Inicio edit. Editando tarifa ID: {}", request.getIdTarifa());
        try {
            Tarifa tarifa = tarifaRepository.getById(request.getIdTarifa());
            if (tarifa == null){
                log.warn("Tarifa no encontrada para editar. ID: {}", request.getIdTarifa());
                throw new ServiceError("", Errores.TARIFA_NO_ENCONTRADA, 404);
            }

            if (request.getPesoMax() != null) {
                tarifa.setPesoMax(request.getPesoMax());
            }
            if (request.getVolumenMax() != null) {
                tarifa.setVolumenMax(request.getVolumenMax());
            }
            if (request.getCostoBase() != null) {
                tarifa.setCostoBase(request.getCostoBase());
            }
            if (request.getValorLitro() != null) {
                tarifa.setValorLitro(request.getValorLitro());
            }
            if (request.getConsumoAprox() != null) {
                tarifa.setConsumoAprox(request.getConsumoAprox());
            }
            if (request.getCostoEstadia() != null) {
                tarifa.setCostoEstadia(request.getCostoEstadia());
            }

            this.tarifaRepository.save(tarifa);
            log.info("Tarifa editada exitosamente. ID: {}", tarifa.getIdTarifa());
            return TarifaMapperDto.toResponseEdit(tarifa);

        } catch (ServiceError ex) {
            log.warn("ServiceError en edit (Tarifa ID: {}): {} - {}", request.getIdTarifa(), ex.getHttpCode(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno al editar tarifa ID {}: {}", request.getIdTarifa(), ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO, 500);
        }
    }

    public void delete(TarifaDeleteRequest request) throws ServiceError {
        log.info("Inicio delete. Eliminando tarifa ID: {}", request.getIdTarifa());
        try {
            Tarifa tarifa = tarifaRepository.getById(request.getIdTarifa());
            if (tarifa == null){
                log.warn("Tarifa no encontrada para eliminar. ID: {}", request.getIdTarifa());
                throw new ServiceError("", Errores.TARIFA_NO_ENCONTRADA, 404);
            }

            this.tarifaRepository.delete(request.getIdTarifa());
            log.info("Tarifa eliminada exitosamente. ID: {}", request.getIdTarifa());
        } catch (ServiceError ex) {
            log.warn("ServiceError en delete (Tarifa ID: {}): {} - {}", request.getIdTarifa(), ex.getHttpCode(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno al eliminar tarifa ID {}: {}", request.getIdTarifa(), ex.getMessage(), ex);
            throw new ServiceError("No se puede eliminar la tarifa, puede estar en uso.", Errores.ERROR_INTERNO, 500);
        }
    }

    public TarifaGetByIdResponse getById(TarifaGetByIdRequest request) throws ServiceError {
        log.info("Inicio getById. Buscando tarifa ID: {}", request.getIdTarifa());
        try {
            Tarifa tarifa = tarifaRepository.getById(request.getIdTarifa());
            if (tarifa == null) {
                log.warn("Tarifa no encontrada. ID: {}", request.getIdTarifa());
                throw new ServiceError("Tarifa no encontrada", Errores.TARIFA_NO_ENCONTRADA, 404);
            }

            log.info("Tarifa encontrada. ID: {}", request.getIdTarifa());
            return TarifaMapperDto.toResponseGetById(tarifa);

        } catch (ServiceError ex) {
            log.warn("ServiceError en getById (Tarifa ID: {}): {} - {}", request.getIdTarifa(), ex.getHttpCode(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno al buscar tarifa ID {}: {}", request.getIdTarifa(), ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO, 500);
        }
    }

    public TarifaGetAllResponse getAll() throws ServiceError {
        log.info("Inicio getAll. Buscando todas las tarifas.");
        try {
            List<Tarifa> tarifas = this.tarifaRepository.getAll();
            log.info("Se encontraron {} tarifas.", tarifas.size());
            return TarifaMapperDto.toResponseGetAll(tarifas);

        } catch (Exception ex) {
            log.error("Error interno al buscar todas las tarifas: {}", ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO, 500);
        }
    }

}
