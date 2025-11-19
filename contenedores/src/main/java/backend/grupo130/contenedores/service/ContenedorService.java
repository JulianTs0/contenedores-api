package backend.grupo130.contenedores.service;

import backend.grupo130.contenedores.client.usuarios.entity.Usuario;
import backend.grupo130.contenedores.config.enums.Errores;
import backend.grupo130.contenedores.config.enums.EstadoContenedor;
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
@Slf4j
public class ContenedorService {

    private final ContenedorRepository contenedorRepository;

    private final UsuarioRepository usuarioRepository;

    // GET

    public GetByIdResponse getById(GetByIdRequest request) throws ServiceError {
        log.info("Iniciando búsqueda de contenedor por ID: {}", request.getIdContenedor());

        log.debug("Llamando al repositorio para buscar por ID: {}", request.getIdContenedor());

        Contenedor contenedor = this.contenedorRepository.getById(request.getIdContenedor());

        if (contenedor == null) {
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

    }

    public GetByPesoVolumenResponse getByPesoVolumen(GetByPesoVolumenRequest request) throws ServiceError {
        log.info("Iniciando búsqueda de contenedor por peso {} y volumen {}", request.getPeso(), request.getVolumen());

        log.debug("Llamando al repositorio para buscar por peso y volumen.");
        Contenedor contenedor = this.contenedorRepository.findByPesoVolumen(request.getPeso(), request.getVolumen());

        if (contenedor == null) {
            throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
        }

        GetByPesoVolumenResponse response = ContenedorMapperDto.toResponseGet(contenedor);

        log.info("Búsqueda por peso/volumen exitosa. Contenedor encontrado: {}", response.getId());
        return response;
    }

    public GetAllResponse getByEstado(GetByEstado request) throws ServiceError {
        log.info("Iniciando búsqueda de contenedores por estado: {}", request.getEstado());

        log.debug("Validando estado: {}", request.getEstado());
        EstadoContenedor estadoContenedor = EstadoContenedor.fromString(request.getEstado());

        if (estadoContenedor == null) {
            throw new ServiceError("", Errores.ESTADO_INVALIDO, 400);
        }

        log.debug("Llamando al repositorio para buscar por estado: {}", estadoContenedor);
        List<Contenedor> contenedores = this.contenedorRepository.findByEstado(estadoContenedor);

        GetAllResponse response = ContenedorMapperDto.toResponseGet(contenedores);

        log.info("Búsqueda por estado {} completada. Se encontraron {} contenedores.", request.getEstado(), contenedores.size());
        return response;
    }

    public GetAllResponse getAll() throws ServiceError {
        log.info("Iniciando búsqueda de todos los contenedores.");

        log.debug("Llamando al repositorio para obtener todos los contenedores.");
        List<Contenedor> contenedores = this.contenedorRepository.getAll();

        GetAllResponse response = ContenedorMapperDto.toResponseGet(contenedores);

        log.info("Búsqueda de todos los contenedores completada. Se encontraron {} contenedores.", contenedores.size());
        return response;
    }

    // POST

    public RegisterResponse register(RegisterRequest request) throws ServiceError {
        log.info("Iniciando registro de nuevo contenedor (Peso: {}, Volumen: {}).", request.getPeso(), request.getVolumen());

        Contenedor contenedor = new Contenedor();

        contenedor.setPeso(request.getPeso());
        contenedor.setVolumen(request.getVolumen());
        contenedor.setEstado(EstadoContenedor.BORRADOR);

        if (request.getIdCliente() != null){
            log.debug("Registrando con cliente ID: {}. Estado inicial: BORRADOR.", request.getIdCliente());

            try {
                Usuario usuario = this.usuarioRepository.getById(request.getIdCliente());

                contenedor.setIdCliente(usuario.getIdUsuario());

            } catch (ServiceError ex) {

                if (ex.getMessage() != null && ex.getMessage().contains("[404]")){
                    throw new ServiceError(ex.getMessage(), Errores.USUARIO_NO_ENCONTRADO, 404);
                } else {
                    throw ex;
                }

            }
        }

        Contenedor saved = this.contenedorRepository.save(contenedor);

        log.info("Nuevo contenedor registrado exitosamente en el repositorio.");
        return ContenedorMapperDto.toResponsePost(saved);
    }

    // PATCH

    public EditResponse edit(EditRequest request) throws ServiceError {
        log.info("Iniciando edición de contenedor ID: {}", request.getId());

        log.debug("Buscando contenedor ID: {} para editar.", request.getId());
        Contenedor contenedor = this.contenedorRepository.getById(request.getId());

        if (contenedor == null) {
            throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
        }
        if(contenedor.getEstado() != EstadoContenedor.BORRADOR){
            throw new ServiceError("", Errores.CONTENEDOR_NO_DISPONIBLE, 400);

        }

        if (request.getPeso() != null) {
            log.debug("Actualizando peso a: {}", request.getPeso());
            contenedor.setPeso(request.getPeso());
        }
        if (request.getVolumen() != null) {
            log.debug("Actualizando volumen a: {}", request.getVolumen());
            contenedor.setVolumen(request.getVolumen());
        }

        this.contenedorRepository.update(contenedor);
        log.info("Contenedor ID: {} actualizado exitosamente.", request.getId());

        EditResponse response = ContenedorMapperDto.toResponsePatch(contenedor);
        return response;
    }

    public void asignarCliente(AsignarClienteRequest request) throws ServiceError {
        log.info("Iniciando asignación de cliente ID: {} a contenedor ID: {}", request.getIdCliente(), request.getId());

        log.debug("Buscando contenedor ID: {} para asignar cliente.", request.getId());
        Contenedor contenedor = this.contenedorRepository.getById(request.getId());

        if (contenedor == null) {
            throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
        }
        if (contenedor.getIdCliente() != null){
            throw new ServiceError("", Errores.USUARIO_YA_ASIGNADO, 400);
        }

        log.debug("Buscando usuario ID: {} para asignación.", request.getIdCliente());

        Usuario usuario = this.usuarioRepository.getById(request.getIdCliente());

        if(!(usuario.getRol().equals(Rol.CLIENTE.name()))){
            throw new ServiceError("", Errores.USUARIO_NO_CLIENTE, 400);
        }

        log.debug("Asignando cliente y cambiando estado a PROGRAMADO para contenedor ID: {}.", request.getId());
        contenedor.setIdCliente(usuario.getIdUsuario());

        this.contenedorRepository.update(contenedor);
        log.info("Cliente ID: {} asignado exitosamente al contenedor ID: {}.", request.getIdCliente(), request.getId());
    }

    public CambioDeEstadoResponse cambioDeEstado(CambioDeEstadoRequest request) throws ServiceError {
        log.info("Iniciando cambio de estado para contenedor ID: {} al nuevo estado: {}", request.getId(), request.getEstado());

        log.debug("Buscando contenedor ID: {} para cambio de estado.", request.getId());
        Contenedor contenedor = this.contenedorRepository.getById(request.getId());

        if (contenedor == null) {
            throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
        }

        log.debug("Validando nuevo estado: {}", request.getEstado());

        EstadoContenedor estadoContenedor = EstadoContenedor.fromString(request.getEstado());

        if (estadoContenedor == null) {
            throw new ServiceError("", Errores.ESTADO_INVALIDO, 400);
        }

        log.debug("Validando transición de estado: {} -> {}", contenedor.getEstado(), estadoContenedor);
        switch (contenedor.getEstado()){

            case BORRADOR -> {

                if(estadoContenedor != EstadoContenedor.PROGRAMADO){
                    log.warn("Transición de estado inválida de {} a {} para contenedor ID: {}", contenedor.getEstado(), estadoContenedor, request.getId());
                    throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);
                }

            }
            case PROGRAMADO -> {

                if(estadoContenedor != EstadoContenedor.EN_TRANSITO){
                    log.warn("Transición de estado inválida de {} a {} para contenedor ID: {}", contenedor.getEstado(), estadoContenedor, request.getId());
                    throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);
                }

            }
            case EN_TRANSITO -> {

                if(estadoContenedor !=EstadoContenedor.EN_DEPOSITO && estadoContenedor != backend.grupo130.contenedores.config.enums.EstadoContenedor.ENTREGADO){
                    log.warn("Transición de estado inválida de {} a {} para contenedor ID: {}", contenedor.getEstado(), estadoContenedor, request.getId());
                    throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);
                }

            }
            case EN_DEPOSITO -> {

                if(estadoContenedor != backend.grupo130.contenedores.config.enums.EstadoContenedor.EN_TRANSITO && estadoContenedor != backend.grupo130.contenedores.config.enums.EstadoContenedor.ENTREGADO){
                    log.warn("Transición de estado inválida de {} a {} para contenedor ID: {}", contenedor.getEstado(), estadoContenedor, request.getId());
                    throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);
                }

            }
            case ENTREGADO -> {

                log.warn("Intento de cambiar estado en contenedor ID: {}, que ya fue ENTREGADO.", request.getId());
                throw new ServiceError("", Errores.CONTENEDOR_YA_ENTREGADO, 400);

            }
        }

        contenedor.setEstado(estadoContenedor);
        this.contenedorRepository.update(contenedor);

        CambioDeEstadoResponse response = ContenedorMapperDto.toResponsePatch(contenedor.getIdContenedor(), estadoContenedor);

        log.info("Contenedor ID: {} actualizado al estado: {}.", request.getId(), estadoContenedor);
        return response;
    }


    // DELETE

    public void delete(DeleteRequest request) throws ServiceError {
        log.info("Iniciando eliminación de contenedor ID: {}", request.getIdContenedor());

        log.debug("Buscando contenedor ID: {} para eliminar.", request.getIdContenedor());
        Contenedor contenedor = this.contenedorRepository.getById(request.getIdContenedor());

        if (contenedor == null) {
            throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
        }

        this.contenedorRepository.delete(contenedor.getIdContenedor());
        log.info("Contenedor ID: {} eliminado exitosamente.", request.getIdContenedor());
    }

}
