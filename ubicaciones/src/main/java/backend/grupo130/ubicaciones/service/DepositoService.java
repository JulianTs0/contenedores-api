
package backend.grupo130.ubicaciones.service;

import backend.grupo130.ubicaciones.Repository.DepositoRepository;
import backend.grupo130.ubicaciones.Repository.UbicacionRepository;
import backend.grupo130.ubicaciones.config.enums.Errores;
import backend.grupo130.ubicaciones.config.exceptions.ServiceError;
import backend.grupo130.ubicaciones.data.models.Deposito;
import backend.grupo130.ubicaciones.data.models.Ubicacion;
import backend.grupo130.ubicaciones.dto.deposito.DepositoMapperDto;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoDeleteRequest;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoEditRequest;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoGetByIdRequest;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoRegisterRequest;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoEditResponse;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoGetAllResponse;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoGetByIdResponse;
import backend.grupo130.ubicaciones.dto.ubicaciones.UbicacionesMapperDto;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionDeleteRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionGetByIdResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class DepositoService {

    private final UbicacionRepository ubicacionRepository;

    private final DepositoRepository depositoRepository;

    public DepositoGetByIdResponse getById(DepositoGetByIdRequest request) throws ServiceError {
        try {

            Deposito deposito = this.depositoRepository.getById(request.getIdDeposito());

            if (deposito == null) {
                throw new ServiceError("", Errores.DEPOSITO_NO_ENCONTRADO, 404);
            }

            Long idUbicacion = this.ubicacionRepository.findByDepositoId(deposito.getIdDeposito());

            DepositoGetByIdResponse response = DepositoMapperDto.toResponseGet(deposito, idUbicacion);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public DepositoGetAllResponse getAll() throws ServiceError {
        try {

            List<Deposito> depositos = this.depositoRepository.getAll();

            DepositoGetAllResponse response = DepositoMapperDto.toResponseGet(depositos);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public void register(DepositoRegisterRequest request) throws ServiceError {
        try {

            Ubicacion ubicacion = this.ubicacionRepository.getById(request.getIdUbicacion());

            if (ubicacion == null) {
                throw new ServiceError("", Errores.UBICACION_NO_ENCONTRADA, 404);
            }
            if (ubicacion.getDeposito() != null){
                throw new ServiceError("", Errores.UBICACION_CON_DEPOSTIO, 400);
            }

            Deposito deposito = new Deposito();

            deposito.setNombre(request.getNombre());

            deposito.setCostoEstadiaDiario(request.getCostoEstadiaDiario());

            ubicacion.setDeposito(deposito);

            this.depositoRepository.save(deposito);

            this.ubicacionRepository.update(ubicacion);
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public DepositoEditResponse edit(DepositoEditRequest request) throws ServiceError {
        try {

            Deposito deposito = this.depositoRepository.getById(request.getIdDeposito());

            if (deposito == null) {
                throw new ServiceError("", Errores.DEPOSITO_NO_ENCONTRADO, 404);
            }

            if (request.getNombre() != null) {
                deposito.setNombre(request.getNombre());
            }
            if (request.getCostoEstadiaDiario() != null) {
                deposito.setCostoEstadiaDiario(request.getCostoEstadiaDiario());
            }

            this.depositoRepository.update(deposito);

            DepositoEditResponse response = DepositoMapperDto.toResponsePatch(deposito);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public void delete(DepositoDeleteRequest request) throws ServiceError {
        try {

            Deposito deposito = this.depositoRepository.getById(request.getIdDeposito());

            if (deposito == null) {
                throw new ServiceError("", Errores.DEPOSITO_NO_ENCONTRADO, 404);
            }

            Ubicacion ubicacion = this.ubicacionRepository.findUbicacionByDepositoId(deposito.getIdDeposito());

            ubicacion.setDeposito(null);

            this.depositoRepository.delete(deposito.getIdDeposito());
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

}
