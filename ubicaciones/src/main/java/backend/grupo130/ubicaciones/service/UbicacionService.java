package backend.grupo130.ubicaciones.service;

import backend.grupo130.ubicaciones.Repository.DepositoRepository;
import backend.grupo130.ubicaciones.Repository.UbicacionRepository;
import backend.grupo130.ubicaciones.config.enums.Errores;
import backend.grupo130.ubicaciones.config.exceptions.ServiceError;
import backend.grupo130.ubicaciones.data.models.Ubicacion;
import backend.grupo130.ubicaciones.dto.ubicaciones.UbicacionesMapperDto;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionDeleteRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionEditRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionGetByIdRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionRegisterRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionEditResponse;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionGetAllResponse;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionGetByIdResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class UbicacionService {

    private final UbicacionRepository ubicacionRepository;

    private final DepositoRepository depositoRepository;

    public UbicacionGetByIdResponse getById(UbicacionGetByIdRequest request) throws ServiceError {
        try {

            Ubicacion ubicacion = this.ubicacionRepository.getById(request.getIdUbicacion());

            if (ubicacion == null) {
                throw new ServiceError("", Errores.UBICACION_NO_ENCONTRADA, 404);
            }

           UbicacionGetByIdResponse response = UbicacionesMapperDto.toResponseGet(ubicacion);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public UbicacionGetAllResponse getAll() throws ServiceError {
        try {

            List<Ubicacion> ubicaciones = this.ubicacionRepository.getAll();

            UbicacionGetAllResponse response = UbicacionesMapperDto.toResponseGet(ubicaciones);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public void register(UbicacionRegisterRequest request) throws ServiceError {
        try {

            Ubicacion ubicacion = new Ubicacion();

            ubicacion.setDireccionTextual(request.getDireccion());

            ubicacion.setLatitud(request.getLatitud());

            ubicacion.setLongitud(request.getLongitud());

            this.ubicacionRepository.save(ubicacion);
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public UbicacionEditResponse edit(UbicacionEditRequest request) throws ServiceError {
        try {

            Ubicacion ubicacion = this.ubicacionRepository.getById(request.getIdUbicacion());

            if (ubicacion == null) {
                throw new ServiceError("", Errores.UBICACION_NO_ENCONTRADA, 404);
            }

            if (request.getLatitud() != null) {
                ubicacion.setLatitud(request.getLatitud());
            }
            if (request.getLongitud() != null) {
                ubicacion.setLongitud(request.getLongitud());
            }
            if(request.getDireccion() != null){
                ubicacion.setDireccionTextual(request.getDireccion());
            }

            this.ubicacionRepository.update(ubicacion);

            UbicacionEditResponse response = UbicacionesMapperDto.toResponsePatch(ubicacion);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public void delete(UbicacionDeleteRequest request) throws ServiceError {
        try {

            Ubicacion ubicacion = this.ubicacionRepository.getById(request.getIdUbicacion());

            if (ubicacion == null) {
                throw new ServiceError("", Errores.UBICACION_NO_ENCONTRADA, 404);
            }

            this.ubicacionRepository.delete(ubicacion.getIdUbicacion());
        } catch (ServiceError ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

}
