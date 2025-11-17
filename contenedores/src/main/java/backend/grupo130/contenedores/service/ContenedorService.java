package backend.grupo130.contenedores.service;

import backend.grupo130.contenedores.client.usuarios.models.Usuario;
import backend.grupo130.contenedores.config.enums.Errores;
import backend.grupo130.contenedores.config.enums.Estado;
import backend.grupo130.contenedores.config.enums.Rol;
import backend.grupo130.contenedores.config.exceptions.ServiceError;
import backend.grupo130.contenedores.data.models.Contenedor;
import backend.grupo130.contenedores.dto.ContenedorMapperDto;
import backend.grupo130.contenedores.dto.request.*;
import backend.grupo130.contenedores.dto.response.*;
import backend.grupo130.contenedores.repository.ContenedorRepository;
import backend.grupo130.contenedores.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j // Ya tenías esta anotación, que nos da el objeto 'log'
public class ContenedorService {

    private final ContenedorRepository contenedorRepository;

    private final UsuarioRepository usuarioRepository;

    // GET

    public GetByIdResponse getById(GetByIdRequest request) throws ServiceError {
        log.info("Iniciando búsqueda de contenedor por ID: {}", request.getIdContenedor());
        try {
            log.debug("Llamando al repositorio para buscar por ID: {}", request.getIdContenedor());
            Contenedor contenedor = this.contenedorRepository.getById(request.getIdContenedor());

            if (contenedor == null) {
                log.warn("No se encontró el contenedor con ID: {}", request.getIdContenedor());
                throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
            }

            Usuario usuario = null;

            if(contenedor.getIdCliente() != null){
                log.debug("Contenedor encontrado. Buscando datos del cliente ID: {}", contenedor.getIdCliente());
                usuario = this.usuarioRepository.getById(contenedor.getIdCliente());
            }

            GetByIdResponse response = ContenedorMapperDto.toResponseGet(contenedor, usuario);

            log.info("Búsqueda exitosa del contenedor ID: {}", request.getIdContenedor());
            return response;
        } catch (ServiceError ex) {
            log.warn("Error de servicio al buscar contenedor por ID: {} - Código: {} - Mensaje: {}", request.getIdContenedor(), ex.getHttpCode(), ex.getMessage());
            throw ex;
        }
        catch (Exception ex) {
            // Error inesperado, incluimos la traza completa
            log.error("Error interno inesperado al buscar contenedor por ID: {}", request.getIdContenedor(), ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public GetByPesoVolumenResponse getByPesoVolumen(GetByPesoVolumenRequest request) throws ServiceError {
        log.info("Iniciando búsqueda de contenedor por peso {} y volumen {}", request.getPeso(), request.getVolumen());
        try {
            log.debug("Llamando al repositorio para buscar por peso y volumen.");
            Contenedor contenedor = this.contenedorRepository.findByPesoVolumen(request.getPeso(), request.getVolumen());

            if (contenedor == null) {
                log.warn("No se encontró un contenedor disponible para peso {} y volumen {}", request.getPeso(), request.getVolumen());
                throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
            }

            GetByPesoVolumenResponse response = ContenedorMapperDto.toResponseGet(contenedor);

            log.info("Búsqueda por peso/volumen exitosa. Contenedor encontrado: {}", response.getId());
            return response;
        } catch (ServiceError ex) {
            log.warn("Error de servicio al buscar por peso/volumen - Código: {} - Mensaje: {}", ex.getHttpCode(), ex.getMessage());
            throw ex;
        }
        catch (Exception ex) {
            log.error("Error interno inesperado al buscar por peso/volumen.", ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public GetAllResponse getByEstado(GetByEstado request) throws ServiceError {
        log.info("Iniciando búsqueda de contenedores por estado: {}", request.getEstado());
        try {
            log.debug("Validando estado: {}", request.getEstado());
            Estado estado = Estado.fromString(request.getEstado());
            if (estado == null) {
                log.warn("El estado '{}' proporcionado no es válido.", request.getEstado());
                throw new ServiceError("", Errores.ESTADO_INVALIDO, 400);
            }

            log.debug("Llamando al repositorio para buscar por estado: {}", estado);
            List<Contenedor> contenedores = this.contenedorRepository.findByEstado(estado);

            GetAllResponse response = ContenedorMapperDto.toResponseGet(contenedores);

            log.info("Búsqueda por estado {} completada. Se encontraron {} contenedores.", request.getEstado(), contenedores.size());
            return response;
        } catch (ServiceError ex) {
            log.warn("Error de servicio al buscar por estado: {} - Código: {} - Mensaje: {}", request.getEstado(), ex.getHttpCode(), ex.getMessage());
            throw ex;
        }
        catch (Exception ex) {
            log.error("Error interno inesperado al buscar por estado: {}", request.getEstado(), ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public GetAllResponse getAll() throws ServiceError {
        log.info("Iniciando búsqueda de todos los contenedores.");
        try {
            log.debug("Llamando al repositorio para obtener todos los contenedores.");
            List<Contenedor> contenedores = this.contenedorRepository.getAll();

            GetAllResponse response = ContenedorMapperDto.toResponseGet(contenedores);

            log.info("Búsqueda de todos los contenedores completada. Se encontraron {} contenedores.", contenedores.size());
            return response;
        } catch (ServiceError ex) {
            log.warn("Error de servicio al buscar todos los contenedores - Código: {} - Mensaje: {}", ex.getHttpCode(), ex.getMessage());
            throw ex;
        }  catch (Exception ex) {
            log.error("Error interno inesperado al buscar todos los contenedores.", ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    // POST

    public void register(RegisterRequest request) throws ServiceError {
        log.info("Iniciando registro de nuevo contenedor (Peso: {}, Volumen: {}).", request.getPeso(), request.getVolumen());
        try {

            Contenedor contenedor = new Contenedor();
            contenedor.setPeso(request.getPeso());
            contenedor.setVolumen(request.getVolumen());

            if (request.getIdCliente() != null){
                log.debug("Registrando con cliente ID: {}. Estado inicial: BORRADOR.", request.getIdCliente());
                try {
                    Usuario usuario = this.usuarioRepository.getById(request.getIdCliente());
                    contenedor.setIdCliente(usuario.getIdUsuario());
                    contenedor.setEstado(Estado.BORRADOR);
                } catch (ServiceError ex) {
                    if (ex.getMessage() != null && ex.getMessage().contains("[404]")){
                        log.warn("No se pudo asignar el cliente ID {} al nuevo contenedor: Usuario no encontrado.", request.getIdCliente());
                        throw new ServiceError(ex.getMessage(), Errores.USUARIO_NO_ENCONTRADO, 404);
                    } else {
                        throw ex;
                    }
                }
            } else {
                log.debug("Registrando sin cliente. Estado inicial: BORRADOR.");
                contenedor.setEstado(Estado.BORRADOR);
            }

            this.contenedorRepository.save(contenedor);
            log.info("Nuevo contenedor registrado exitosamente en el repositorio.");
        } catch (ServiceError ex) {
            log.warn("Error de servicio al registrar contenedor - Código: {} - Mensaje: {}", ex.getHttpCode(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno inesperado al registrar contenedor.", ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    // PATCH

    public EditResponse edit(EditRequest request) throws ServiceError {
        log.info("Iniciando edición de contenedor ID: {}", request.getId());
        try {
            log.debug("Buscando contenedor ID: {} para editar.", request.getId());
            Contenedor contenedor = this.contenedorRepository.getById(request.getId());

            if (contenedor == null) {
                log.warn("No se encontró el contenedor ID: {} para editar.", request.getId());
                throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
            }

            if (request.getPeso() != null) {
                log.debug("Actualizando peso a: {}", request.getPeso());
                contenedor.setPeso(request.getPeso());
            }
            if (request.getVolumen() != null) {
                log.debug("Actualizando volumen a: {}", request.getVolumen());
                contenedor.setVolumen(request.getVolumen());
            }
            if (request.getIdCliente() != null) {
                log.debug("Actualizando cliente a ID: {}.", request.getIdCliente());
                Usuario usuario = this.usuarioRepository.getById(request.getIdCliente());
                contenedor.setIdCliente(usuario.getIdUsuario());
            }

            this.contenedorRepository.update(contenedor);
            log.info("Contenedor ID: {} actualizado exitosamente.", request.getId());

            EditResponse response = ContenedorMapperDto.toResponsePatch(contenedor);
            return response;
        } catch (ServiceError ex) {
            log.warn("Error de servicio al editar contenedor ID: {} - Código: {} - Mensaje: {}", request.getId(), ex.getHttpCode(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno inesperado al editar contenedor ID: {}", request.getId(), ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public void asignarCliente(AsignarClienteRequest request) throws ServiceError {
        log.info("Iniciando asignación de cliente ID: {} a contenedor ID: {}", request.getIdCliente(), request.getId());
        try {
            log.debug("Buscando contenedor ID: {} para asignar cliente.", request.getId());
            Contenedor contenedor = this.contenedorRepository.getById(request.getId());

            if (contenedor == null) {
                log.warn("No se encontró el contenedor ID: {} para asignar cliente.", request.getId());
                throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
            }
            if (contenedor.getIdCliente() != null){
                log.warn("Intento de asignar cliente al contenedor ID: {}, que ya tiene un cliente asignado (ID: {}).", request.getId(), contenedor.getIdCliente());
                throw new ServiceError("", Errores.USUARIO_YA_ASIGNADO, 400);
            }

            log.debug("Buscando usuario ID: {} para asignación.", request.getIdCliente());
            Usuario usuario = this.usuarioRepository.getById(request.getIdCliente());

            if(!(usuario.getRol().equals(Rol.CLIENTE.name()))){
                log.warn("El usuario ID: {} no es un CLIENTE (Rol: {}). No se puede asignar.", request.getIdCliente(), usuario.getRol());
                throw new ServiceError("", Errores.USUARIO_NO_CLIENTE, 400);
            }

            log.debug("Asignando cliente y cambiando estado a PROGRAMADO para contenedor ID: {}.", request.getId());
            contenedor.setIdCliente(usuario.getIdUsuario());
            contenedor.setEstado(Estado.PROGRAMADO);

            this.contenedorRepository.update(contenedor);
            log.info("Cliente ID: {} asignado exitosamente al contenedor ID: {}.", request.getIdCliente(), request.getId());
        } catch (ServiceError ex) {
            log.warn("Error de servicio al asignar cliente (Contenedor ID: {}, Cliente ID: {}) - Código: {} - Mensaje: {}", request.getId(), request.getIdCliente(), ex.getHttpCode(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno inesperado al asignar cliente al contenedor ID: {}", request.getId(), ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public CambioDeEstadoResponse cambioDeEstado(CambioDeEstadoRequest request) throws ServiceError {
        log.info("Iniciando cambio de estado para contenedor ID: {} al nuevo estado: {}", request.getId(), request.getEstado());
        try {
            log.debug("Buscando contenedor ID: {} para cambio de estado.", request.getId());
            Contenedor contenedor = this.contenedorRepository.getById(request.getId());

            if (contenedor == null) {
                log.warn("No se encontró el contenedor ID: {} para cambio de estado.", request.getId());
                throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
            }

            log.debug("Validando nuevo estado: {}", request.getEstado());
            Estado estado = Estado.fromString(request.getEstado());
            if (estado == null) {
                log.warn("El estado '{}' proporcionado no es válido para el contenedor ID: {}.", request.getEstado(), request.getId());
                throw new ServiceError("", Errores.ESTADO_INVALIDO, 400);
            }

            log.debug("Validando transición de estado: {} -> {}", contenedor.getEstado(), estado);
            switch (contenedor.getEstado()){
                case BORRADOR -> {
                    if(estado != Estado.PROGRAMADO){
                        log.warn("Transición de estado inválida de {} a {} para contenedor ID: {}", contenedor.getEstado(), estado, request.getId());
                        throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);
                    }
                }
                case PROGRAMADO -> {
                    if(estado != Estado.EN_TRANSITO){
                        log.warn("Transición de estado inválida de {} a {} para contenedor ID: {}", contenedor.getEstado(), estado, request.getId());
                        throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);
                    }
                }
                case EN_TRANSITO -> {
                    if(estado != Estado.EN_DEPOSITO && estado != Estado.ENTREGADO){
                        log.warn("Transición de estado inválida de {} a {} para contenedor ID: {}", contenedor.getEstado(), estado, request.getId());
                        throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);
                    }
                }
                case EN_DEPOSITO -> {
                    if(estado != Estado.EN_TRANSITO && estado != Estado.ENTREGADO){
                        log.warn("Transición de estado inválida de {} a {} para contenedor ID: {}", contenedor.getEstado(), estado, request.getId());
                        throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);
                    }
                }
                case ENTREGADO -> {
                    log.warn("Intento de cambiar estado en contenedor ID: {}, que ya fue ENTREGADO.", request.getId());
                    throw new ServiceError("", Errores.CONTENEDOR_YA_ENTREGADO, 400);
                }
            }

            contenedor.setEstado(estado);
            this.contenedorRepository.update(contenedor);
            log.info("Contenedor ID: {} actualizado al estado: {}.", request.getId(), estado);

            CambioDeEstadoResponse response = ContenedorMapperDto.toResponsePatch(contenedor.getIdContenedor(), estado);
            return response;
        } catch (ServiceError ex) {
            log.warn("Error de servicio al cambiar estado (Contenedor ID: {}, Nuevo Estado: {}) - Código: {} - Mensaje: {}", request.getId(), request.getEstado(), ex.getHttpCode(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno inesperado al cambiar estado del contenedor ID: {}", request.getId(), ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }


    // DELETE

    public void delete(DeleteRequest request) throws ServiceError {
        log.info("Iniciando eliminación de contenedor ID: {}", request.getIdContenedor());
        try {
            log.debug("Buscando contenedor ID: {} para eliminar.", request.getIdContenedor());
            Contenedor contenedor = this.contenedorRepository.getById(request.getIdContenedor());

            if (contenedor == null) {
                log.warn("No se encontró el contenedor ID: {} para eliminar.", request.getIdContenedor());
                throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
            }

            this.contenedorRepository.delete(contenedor.getIdContenedor());
            log.info("Contenedor ID: {} eliminado exitosamente.", request.getIdContenedor());
        } catch (ServiceError ex) {
            log.warn("Error de servicio al eliminar contenedor ID: {} - Código: {} - Mensaje: {}", request.getIdContenedor(), ex.getHttpCode(), ex.getMessage());
            throw ex;
        }
        catch (Exception ex) {
            log.error("Error interno inesperado al eliminar contenedor ID: {}", request.getIdContenedor(), ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

}
