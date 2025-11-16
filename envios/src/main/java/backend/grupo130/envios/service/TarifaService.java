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
        try {

            Tarifa tarifa = new Tarifa();

            tarifa.setConsumoAprox(request.getConsumoAprox());
            tarifa.setCostoBase(request.getCostoBase());
            tarifa.setVolumenMax(request.getVolumenMax());
            tarifa.setValorLitro(request.getValorLitro());
            tarifa.setConsumoAprox(request.getConsumoAprox());
            tarifa.setCostoEstadia(request.getCostoEstadia());



            this.tarifaRepository.save(tarifa);

            TarifaRegisterResponse response = TarifaMapperDto.toResponsePost(tarifa);

            return response;

        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public TarifaEditResponse edit(TarifaEditRequest request) throws ServiceError {
        try {
            Tarifa tarifa = tarifaRepository.getById(request.getIdTarifa());
            if (tarifa == null){
                throw new ServiceError("", Errores.TARIFA_NO_ENCONTRADA, 404);
            }

            // Actualización parcial de campos (solo si no son nulos)
            if (request.getVolumenMin() != null) {
                tarifa.setVolumenMin(request.getVolumenMin());
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

            // Validar de nuevo que min < max
            if (tarifa.getVolumenMin().compareTo(tarifa.getVolumenMax()) >= 0) {
                throw new ServiceError("El volumen mínimo no puede ser mayor o igual al máximo", Errores.DATOS_INVALIDOS, 400);
            }

            this.tarifaRepository.save(tarifa);
            return TarifaMapperDto.toResponseEdit(tarifa);

        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO, 500);
        }
    }

    public void delete(TarifaDeleteRequest request) throws ServiceError {
        try {
            Tarifa tarifa = tarifaRepository.getById(request.getIdTarifa());
            if (tarifa == null){
                throw new ServiceError("", Errores.TARIFA_NO_ENCONTRADA, 404);
            }

            this.tarifaRepository.delete(request.getIdTarifa());
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError("No se puede eliminar la tarifa, puede estar en uso.", Errores.ERROR_INTERNO, 500);
        }
    }

    public TarifaGetByIdResponse getById(TarifaGetByIdRequest request) throws ServiceError {
        try {
            Tarifa tarifa = tarifaRepository.getById(request.getIdTarifa());
            if (tarifa == null) {
                throw new ServiceError("Tarifa no encontrada", Errores.TARIFA_NO_ENCONTRADA, 404);
            }


            return TarifaMapperDto.toResponseGetById(tarifa);

        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO, 500);
        }
    }

    public TarifaGetAllResponse getAll() throws ServiceError {
        try {
            List<Tarifa> tarifas = this.tarifaRepository.getAll();
            return TarifaMapperDto.toResponseGetAll(tarifas);

        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO, 500);
        }
    }

}
