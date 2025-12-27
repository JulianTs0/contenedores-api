package backend.grupo130.ubicaciones.service;

import backend.grupo130.ubicaciones.data.entity.Ubicacion;
import backend.grupo130.ubicaciones.repository.UbicacionRepository;
import backend.grupo130.ubicaciones.config.enums.Errores;
import backend.grupo130.ubicaciones.config.exceptions.ServiceError;
import backend.grupo130.ubicaciones.dto.ubicaciones.UbicacionesMapperDto;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionEditRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionGetByIdRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionRegisterRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionEditResponse;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionGetAllResponse;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionGetByIdResponse;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionRegisterResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class UbicacionService {

    private final UbicacionRepository ubicacionRepository;

    public UbicacionGetByIdResponse getById(UbicacionGetByIdRequest request) throws ServiceError {

        Ubicacion ubicacion = this.ubicacionRepository.getById(request.getIdUbicacion());

        if (ubicacion == null) {
            throw new ServiceError("", Errores.UBICACION_NO_ENCONTRADA, 404);
        }

        log.debug("Ubicacion encontrada: {}", ubicacion.getIdUbicacion());

        UbicacionGetByIdResponse response = UbicacionesMapperDto.toResponseGetById(ubicacion);

        log.info("Finalizado getById para Ubicacion con ID: {}", request.getIdUbicacion());
        return response;
    }

    public UbicacionGetAllResponse getAll() throws ServiceError {
        log.info("Iniciando getAll para Ubicaciones");

        List<Ubicacion> ubicaciones = this.ubicacionRepository.getAll();
        log.debug("Se encontraron {} ubicaciones", ubicaciones.size());

        UbicacionGetAllResponse response = UbicacionesMapperDto.toResponseGetAll(ubicaciones);

        log.info("Finalizado getAll para Ubicaciones. Total: {}", ubicaciones.size());
        return response;
    }

    public UbicacionRegisterResponse register(UbicacionRegisterRequest request) throws ServiceError {
        log.info("Iniciando register para nueva Ubicacion con direccion: {}", request.getDireccion());

        Ubicacion ubicacion = new Ubicacion();

        ubicacion.setDireccionTextual(request.getDireccion());
        ubicacion.setLatitud(request.getLatitud());
        ubicacion.setLongitud(request.getLongitud());

        log.debug("Datos de nueva ubicacion a persistir: Direccion={}, Lat={}, Lon={}",
            ubicacion.getDireccionTextual(), ubicacion.getLatitud(), ubicacion.getLongitud());

        Ubicacion savedUbicacion = this.ubicacionRepository.save(ubicacion);

        log.info("Ubicacion registrada exitosamente.");
        return UbicacionesMapperDto.toResponsePostRegister(savedUbicacion);
    }

    public UbicacionEditResponse edit(UbicacionEditRequest request) throws ServiceError {
        log.info("Iniciando edit para Ubicacion con ID: {}", request.getIdUbicacion());

        Ubicacion ubicacion = this.ubicacionRepository.getById(request.getIdUbicacion());

        if (ubicacion == null) {
            throw new ServiceError("", Errores.UBICACION_NO_ENCONTRADA, 404);
        }

        log.debug("Ubicacion ANTES de editar: {}", ubicacion);

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
        log.debug("Ubicacion DESPUES de editar: {}", ubicacion);

        UbicacionEditResponse response = UbicacionesMapperDto.toResponsePatchEdit(ubicacion);

        log.info("Finalizado edit para Ubicacion con ID: {}", request.getIdUbicacion());
        return response;
    }

    public void delete(Long id) throws ServiceError {
        log.info("Iniciando delete para Ubicacion con ID: {}", id);

        Ubicacion ubicacion = this.ubicacionRepository.getById(id);

        if (ubicacion == null) {
            throw new ServiceError("", Errores.UBICACION_NO_ENCONTRADA, 404);
        }

        this.ubicacionRepository.delete(id);
        log.info("Finalizado delete para Ubicacion con ID: {}", id);
    }

}

