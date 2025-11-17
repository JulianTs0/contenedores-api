package backend.grupo130.ubicaciones.service;

import backend.grupo130.ubicaciones.Repository.DepositoRepository;
import backend.grupo130.ubicaciones.Repository.UbicacionRepository;
import backend.grupo130.ubicaciones.config.enums.Errores;
import backend.grupo130.ubicaciones.config.exceptions.ServiceError;
import backend.grupo130.ubicaciones.data.models.Deposito;
import backend.grupo130.ubicaciones.data.models.Ubicacion;
import backend.grupo130.ubicaciones.dto.ubicaciones.UbicacionesMapperDto;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.*;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionEditResponse;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionGetAllResponse;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionGetByIdResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j // <--- PASO 1: Incluir el Logger (usando Lombok)
public class UbicacionService {

    private final UbicacionRepository ubicacionRepository;

    private final DepositoRepository depositoRepository;

    public UbicacionGetByIdResponse getById(UbicacionGetByIdRequest request) throws ServiceError {
        // PASO 2: Log de nivel INFO para operaciones importantes
        log.info("Iniciando getById para Ubicacion con ID: {}", request.getIdUbicacion());
        try {

            Ubicacion ubicacion = this.ubicacionRepository.getById(request.getIdUbicacion());

            if (ubicacion == null) {
                // PASO 2: Log de nivel WARN para condiciones anómalas
                log.warn("No se encontró Ubicacion con ID: {}", request.getIdUbicacion());
                throw new ServiceError("", Errores.UBICACION_NO_ENCONTRADA, 404);
            }

            // PASO 2: Log de nivel DEBUG para información de diagnóstico
            log.debug("Ubicacion encontrada: {}", ubicacion.getIdUbicacion());

            UbicacionGetByIdResponse response = UbicacionesMapperDto.toResponseGet(ubicacion);

            log.info("Finalizado getById para Ubicacion con ID: {}", request.getIdUbicacion());
            return response;
        } catch (ServiceError ex) {
            // PASO 2: Log de nivel ERROR (preservando la traza)
            log.error("Error de servicio en getById de Ubicacion: {}", ex.getMessage(), ex);
            throw ex;
        }
        catch (Exception ex) {
            // PASO 2: Log de nivel ERROR para excepciones inesperadas
            log.error("Error interno inesperado en getById de Ubicacion", ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public UbicacionGetAllResponse getAll() throws ServiceError {
        log.info("Iniciando getAll para Ubicaciones");
        try {

            List<Ubicacion> ubicaciones = this.ubicacionRepository.getAll();
            log.debug("Se encontraron {} ubicaciones", ubicaciones.size());

            UbicacionGetAllResponse response = UbicacionesMapperDto.toResponseGet(ubicaciones);

            log.info("Finalizado getAll para Ubicaciones. Total: {}", ubicaciones.size());
            return response;
        } catch (ServiceError ex) {
            log.error("Error de servicio en getAll de Ubicaciones: {}", ex.getMessage(), ex);
            throw ex;
        }  catch (Exception ex) {
            log.error("Error interno inesperado en getAll de Ubicaciones", ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public void register(UbicacionRegisterRequest request) throws ServiceError {
        log.info("Iniciando register para nueva Ubicacion con direccion: {}", request.getDireccion());
        try {

            Ubicacion ubicacion = new Ubicacion();

            ubicacion.setDireccionTextual(request.getDireccion());
            ubicacion.setLatitud(request.getLatitud());
            ubicacion.setLongitud(request.getLongitud());

            log.debug("Datos de nueva ubicacion a persistir: Direccion={}, Lat={}, Lon={}",
                ubicacion.getDireccionTextual(), ubicacion.getLatitud(), ubicacion.getLongitud());

            this.ubicacionRepository.save(ubicacion);

            // Asumimos que 'save' no devuelve ID, así que solo logueamos éxito.
            log.info("Ubicacion registrada exitosamente.");
        } catch (ServiceError ex) {
            log.error("Error de servicio en register de Ubicacion: {}", ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno inesperado en register de Ubicacion", ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public UbicacionEditResponse edit(UbicacionEditRequest request) throws ServiceError {
        log.info("Iniciando edit para Ubicacion con ID: {}", request.getIdUbicacion());
        try {

            Ubicacion ubicacion = this.ubicacionRepository.getById(request.getIdUbicacion());

            if (ubicacion == null) {
                log.warn("No se encontró Ubicacion para editar con ID: {}", request.getIdUbicacion());
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

            UbicacionEditResponse response = UbicacionesMapperDto.toResponsePatch(ubicacion);

            log.info("Finalizado edit para Ubicacion con ID: {}", request.getIdUbicacion());
            return response;
        } catch (ServiceError ex) {
            log.error("Error de servicio en edit de Ubicacion: {}", ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno inesperado en edit de Ubicacion", ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public void delete(UbicacionDeleteRequest request) throws ServiceError {
        log.info("Iniciando delete para Ubicacion con ID: {}", request.getIdUbicacion());
        try {

            Ubicacion ubicacion = this.ubicacionRepository.getById(request.getIdUbicacion());

            if (ubicacion == null) {
                log.warn("No se encontró Ubicacion para eliminar con ID: {}", request.getIdUbicacion());
                throw new ServiceError("", Errores.UBICACION_NO_ENCONTRADA, 404);
            }

            this.ubicacionRepository.delete(ubicacion.getIdUbicacion());
            log.info("Finalizado delete para Ubicacion con ID: {}", request.getIdUbicacion());

        } catch (ServiceError ex) {
            log.error("Error de servicio en delete de Ubicacion: {}", ex.getMessage(), ex);
            throw ex;
        }
        catch (Exception ex) {
            log.error("Error interno inesperado en delete de Ubicacion", ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

}
