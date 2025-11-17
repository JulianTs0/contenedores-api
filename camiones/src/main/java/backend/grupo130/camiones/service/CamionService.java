package backend.grupo130.camiones.service;

import backend.grupo130.camiones.client.usuarios.models.Usuario;
import backend.grupo130.camiones.config.enums.Errores;
import backend.grupo130.camiones.config.enums.Rol;
import backend.grupo130.camiones.config.exceptions.ServiceError;
import backend.grupo130.camiones.data.models.Camion;
import backend.grupo130.camiones.dto.CamionesMapperDto;
import backend.grupo130.camiones.dto.request.*;
import backend.grupo130.camiones.dto.response.*;
import backend.grupo130.camiones.repository.CamionRepository;
import backend.grupo130.camiones.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@AllArgsConstructor
@Transactional
@Slf4j // Anotación de Lombok para obtener el logger
public class CamionService {

    private final CamionRepository camionRepository;

    private final UsuarioRepository usuarioRepository;

    public GetByIdResponse getById(GetByIdRequest request) throws ServiceError {
        log.info("Iniciando búsqueda de camión por dominio: {}", request.getDominio());
        try {
            Camion camion = this.camionRepository.getById(request.getDominio());

            if (camion == null) {
                log.error("Camión no encontrado con dominio: {}", request.getDominio());
                throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
            }

            Usuario usuario = null;

            if(camion.getIdTransportista() != null){
                log.debug("Camión {} tiene transportista asignado, buscando datos...", camion.getDominio());
                usuario = this.usuarioRepository.getById(camion.getIdTransportista());
            }

            GetByIdResponse response = CamionesMapperDto.toResponseGet(camion, usuario);
            log.info("Búsqueda exitosa para camión con dominio: {}", request.getDominio());
            return response;
        } catch (ServiceError ex) {
            log.warn("Error de servicio al buscar camión {}: {}", request.getDominio(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno inesperado al buscar camión {}: {}", request.getDominio(), ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public GetAllResponse getAll() throws ServiceError {
        log.info("Iniciando obtención de todos los camiones");
        try {
            List<Camion> camiones = this.camionRepository.getAll();
            GetAllResponse response = CamionesMapperDto.toResponseGet(camiones);
            log.info("Se encontraron {} camiones en total", camiones.size());
            return response;
        } catch (ServiceError ex) {
            log.warn("Error de servicio al obtener todos los camiones: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno inesperado al obtener todos los camiones: {}", ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public GetAllResponse getDisponibles() throws ServiceError {
        log.info("Iniciando obtención de camiones disponibles");
        try {
            List<Camion> camiones = this.camionRepository.findDisponibilidad();
            GetAllResponse response = CamionesMapperDto.toResponseGet(camiones);
            log.info("Se encontraron {} camiones disponibles", camiones.size());
            return response;
        } catch (ServiceError ex) {
            log.warn("Error de servicio al obtener camiones disponibles: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno inesperado al obtener camiones disponibles: {}", ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public GetPromedioCostoBaseResponse getCostoAprox(GetOpcionesCamionesRequest request) throws ServiceError {
        log.info("Iniciando cálculo de costo aproximado para peso {} y volumen {}", request.getCapacidadPeso(), request.getCapacidadVolumen());
        try {
            BigDecimal promedio = this.camionRepository.findAverageCostOfTop3(request.getCapacidadPeso(), request.getCapacidadVolumen());
            GetPromedioCostoBaseResponse response = CamionesMapperDto.toResponseAvg(promedio);
            log.info("Costo aproximado calculado: {}", promedio);
            return response;
        } catch (ServiceError ex) {
            log.warn("Error de servicio al calcular costo aproximado: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno inesperado al calcular costo aproximado: {}", ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public GetPromedioCombustibleActualResponse getConsumoPromedio() throws ServiceError {
        log.info("Iniciando cálculo de consumo promedio de combustible total");
        try {
            BigDecimal promedio = this.camionRepository.findAverageConsumoTotal();
            GetPromedioCombustibleActualResponse response = CamionesMapperDto.toResponseAvgTotal(promedio);
            log.info("Consumo promedio total calculado: {}", promedio);
            return response;
        } catch (ServiceError ex) {
            log.warn("Error de servicio al calcular consumo promedio: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno inesperado al calcular consumo promedio: {}", ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public void register(RegisterRequest request) throws ServiceError {
        log.info("Iniciando registro de nuevo camión con dominio: {}", request.getDominio());
        try {
            Camion camion = new Camion();
            camion.setDominio(request.getDominio());
            camion.setCapacidadPeso(request.getCapacidadPeso());
            camion.setCapacidadVolumen(request.getCapacidadVolumen());
            camion.setConsumoCombustible(request.getConsumoCombustible());
            camion.setCostoTrasladoBase(request.getCostoTrasladoBase());
            camion.setEstado(true); // Asumiendo que un camión nuevo siempre está disponible

            this.camionRepository.save(camion);
            log.info("Camión registrado exitosamente con dominio: {}", camion.getDominio());
        } catch (ServiceError ex) {
            log.warn("Error de servicio al registrar camión {}: {}", request.getDominio(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno inesperado al registrar camión {}: {}", request.getDominio(), ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public EditResponse cambiarDisponibilidad(CambiarDisponibilidadRequest request) throws ServiceError {
        log.info("Iniciando cambio de disponibilidad para camión: {}. Nuevo estado: {}", request.getDominio(), request.getEstado());
        try {
            Camion camion = this.camionRepository.getById(request.getDominio());

            if (camion == null) {
                log.error("No se puede cambiar disponibilidad. Camión no encontrado: {}", request.getDominio());
                throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
            }

            camion.setEstado(request.getEstado());
            this.camionRepository.update(camion);

            EditResponse response = CamionesMapperDto.toResponsePatch(camion);
            log.info("Disponibilidad cambiada exitosamente para camión: {}. Nuevo estado: {}", camion.getDominio(), camion.getEstado());
            return response;
        } catch (ServiceError ex) {
            log.warn("Error de servicio al cambiar disponibilidad de camión {}: {}", request.getDominio(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno inesperado al cambiar disponibilidad de camión {}: {}", request.getDominio(), ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public EditResponse edit(EditRequest request) throws ServiceError {
        log.info("Iniciando edición de datos para camión: {}", request.getDominio());
        try {
            Camion camion = this.camionRepository.getById(request.getDominio());

            if (camion == null) {
                log.error("No se puede editar. Camión no encontrado: {}", request.getDominio());
                throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
            }
            if (!camion.getEstado()){
                log.warn("No se puede editar. Camión {} no está disponible.", request.getDominio());
                throw new ServiceError("", Errores.CAMION_NO_DISPONIBLE, 400);
            }

            // Lógica de actualización
            if (request.getCapacidadPeso() != null) {
                camion.setCapacidadPeso(request.getCapacidadPeso());
            }
            if (request.getCapacidadVolumen() != null) {
                camion.setCapacidadVolumen(request.getCapacidadVolumen());
            }
            if (request.getConsumoCombustible() != null) {
                camion.setConsumoCombustible(request.getConsumoCombustible());
            }
            if (request.getCostoTrasladoBase() != null) {
                camion.setCostoTrasladoBase(request.getCostoTrasladoBase());
            }

            this.camionRepository.update(camion);

            EditResponse response = CamionesMapperDto.toResponsePatch(camion);
            log.info("Camión editado exitosamente: {}", camion.getDominio());
            return response;
        } catch (ServiceError ex) {
            log.warn("Error de servicio al editar camión {}: {}", request.getDominio(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno inesperado al editar camión {}: {}", request.getDominio(), ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public void asignarTransportista(AsignarTransportistaRequest request) throws ServiceError {
        log.info("Iniciando asignación de transportista ID {} a camión {}", request.getIdTransportista(), request.getDominio());
        try {
            Camion camion = this.camionRepository.getById(request.getDominio());

            if (camion == null) {
                log.error("No se puede asignar transportista. Camión no encontrado: {}", request.getDominio());
                throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
            }
            if (!camion.getEstado()){
                log.warn("No se puede asignar transportista. Camión {} no está disponible.", request.getDominio());
                throw new ServiceError("", Errores.CAMION_NO_DISPONIBLE, 400);
            }

            Usuario usuario = this.usuarioRepository.getById(request.getIdTransportista());

            if (usuario == null) {
                log.error("No se puede asignar transportista. Usuario no encontrado con ID: {}", request.getIdTransportista());
                // Asumiendo que tienes un error definido para esto, si no, CAMION_NO_ENCONTRADO es incorrecto pero es un placeholder
                throw new ServiceError("", Errores.USUARIO_NO_TRANSPORTISTA, 404); // O un Errores.USUARIO_NO_ENCONTRADO si existe
            }

            if(!(usuario.getRol().equals(Rol.TRANSPORTISTA.name()))){
                log.warn("No se puede asignar transportista. Usuario {} no tiene rol TRANSPORTISTA.", request.getIdTransportista());
                throw new ServiceError("", Errores.USUARIO_NO_TRANSPORTISTA, 400);
            }

            camion.setIdTransportista(usuario.getIdUsuario());
            this.camionRepository.update(camion);
            log.info("Transportista {} asignado exitosamente a camión {}", request.getIdTransportista(), request.getDominio());
        } catch (ServiceError ex) {
            log.warn("Error de servicio al asignar transportista a camión {}: {}", request.getDominio(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno inesperado al asignar transportista a camión {}: {}", request.getDominio(), ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public void delete(DeleteRequest request) throws ServiceError {
        log.info("Iniciando eliminación de camión: {}", request.getDominio());
        try {
            Camion camion = this.camionRepository.getById(request.getDominio());

            if (camion == null) {
                log.error("No se puede eliminar. Camión no encontrado: {}", request.getDominio());
                throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
            }
            if (!camion.getEstado()){
                log.warn("No se puede eliminar. Camión {} no está disponible.", request.getDominio());
                throw new ServiceError("", Errores.CAMION_NO_DISPONIBLE, 400);
            }

            this.camionRepository.delete(camion.getDominio());
            log.info("Camión eliminado exitosamente: {}", request.getDominio());
        } catch (ServiceError ex) {
            log.warn("Error de servicio al eliminar camión {}: {}", request.getDominio(), ex.getMessage());
            throw ex;
        }
        catch (Exception ex) {
            log.error("Error interno inesperado al eliminar camión {}: {}", request.getDominio(), ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }
}
