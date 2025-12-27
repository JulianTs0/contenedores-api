package backend.grupo130.contenedores.service;

import backend.grupo130.contenedores.client.usuarios.UsuarioClient;
import backend.grupo130.contenedores.client.usuarios.entity.Usuario;
import backend.grupo130.contenedores.config.enums.Errores;
import backend.grupo130.contenedores.config.enums.EstadoContenedor;
import backend.grupo130.contenedores.config.enums.Rol;
import backend.grupo130.contenedores.config.exceptions.ServiceError;
import backend.grupo130.contenedores.data.entity.Contenedor;
import backend.grupo130.contenedores.dto.ContenedorMapperDto;
import backend.grupo130.contenedores.dto.request.AsignarClienteRequest;
import backend.grupo130.contenedores.dto.request.CambioDeEstadoRequest;
import backend.grupo130.contenedores.dto.request.EditRequest;
import backend.grupo130.contenedores.dto.request.RegisterRequest;
import backend.grupo130.contenedores.dto.response.AsignarClienteResponse;
import backend.grupo130.contenedores.dto.response.CambioDeEstadoResponse;
import backend.grupo130.contenedores.dto.response.EditResponse;
import backend.grupo130.contenedores.dto.response.GetAllResponse;
import backend.grupo130.contenedores.dto.response.GetByIdResponse;
import backend.grupo130.contenedores.dto.response.GetByPesoVolumenResponse;
import backend.grupo130.contenedores.dto.response.RegisterResponse;
import backend.grupo130.contenedores.repository.ContenedorRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ContenedorService {

    private final ContenedorRepository contenedorRepository;

    private final UsuarioClient usuarioClient;

    // GET

    public GetByIdResponse getById(Long id) throws ServiceError {
        log.info("Iniciando búsqueda de contenedor por ID: {}", id);

        log.debug("Llamando al repositorio para buscar por ID: {}", id);

        Contenedor contenedor = this.contenedorRepository.getById(id);

        if (contenedor == null) {
            throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
        }

        Usuario usuario = null;

        if(contenedor.getCliente() != null && contenedor.getCliente().getIdUsuario() != null){
            log.debug("Contenedor encontrado. Buscando datos del cliente ID: {}", contenedor.getCliente().getIdUsuario());
            usuario = this.usuarioClient.getById(contenedor.getCliente().getIdUsuario());
        }

        contenedor.setCliente(usuario);

        GetByIdResponse response = ContenedorMapperDto.toResponseGetById(contenedor);

        log.info("Búsqueda exitosa del contenedor ID: {}", id);
        return response;

    }

    public GetByPesoVolumenResponse getByPesoVolumen(BigDecimal peso, BigDecimal volumen) throws ServiceError {
        log.info("Iniciando búsqueda de contenedor por peso {} y volumen {}", peso, volumen);

        log.debug("Llamando al repositorio para buscar por peso y volumen.");
        Contenedor contenedor = this.contenedorRepository.findByPesoVolumen(peso, volumen);

        if (contenedor == null) {
            throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
        }

        GetByPesoVolumenResponse response = ContenedorMapperDto.toResponseGetByPesoVolumen(contenedor);

        log.info("Búsqueda por peso/volumen exitosa. Contenedor encontrado: {}", response.getId());
        return response;
    }

    public GetAllResponse getByEstado(String estado) throws ServiceError {
        log.info("Iniciando búsqueda de contenedores por estado: {}", estado);

        log.debug("Validando estado: {}", estado);
        EstadoContenedor estadoContenedor = EstadoContenedor.fromString(estado);

        if (estadoContenedor == null) {
            throw new ServiceError("", Errores.ESTADO_INVALIDO, 400);
        }

        log.debug("Llamando al repositorio para buscar por estado: {}", estadoContenedor);
        List<Contenedor> contenedores = this.contenedorRepository.findByEstado(estadoContenedor);

        GetAllResponse response = ContenedorMapperDto.toResponseGetAll(contenedores);

        log.info("Búsqueda por estado {} completada. Se encontraron {} contenedores.", estado, contenedores.size());
        return response;
    }

    public GetAllResponse getAll() throws ServiceError {
        log.info("Iniciando búsqueda de todos los contenedores.");

        log.debug("Llamando al repositorio para obtener todos los contenedores.");
        List<Contenedor> contenedores = this.contenedorRepository.getAll();

        GetAllResponse response = ContenedorMapperDto.toResponseGetAll(contenedores);

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
                Usuario usuario = this.usuarioClient.getById(request.getIdCliente());

                contenedor.setCliente(usuario);

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
        return ContenedorMapperDto.toResponsePostRegister(saved);
    }

    // PATCH

    public EditResponse edit(EditRequest request) throws ServiceError {
        log.info("Iniciando edición de contenedor ID: {}", request.getId());

        log.debug("Buscando contenedor ID: {} para editar.", request.getId());
        Contenedor contenedor = this.contenedorRepository.getById(request.getId());

        if (contenedor == null) {
            throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
        }
        if(!contenedor.esBorrador()){
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

        EditResponse response = ContenedorMapperDto.toResponsePatchEdit(contenedor);
        return response;
    }

    public AsignarClienteResponse asignarCliente(AsignarClienteRequest request) throws ServiceError {
        log.info("Iniciando asignación de cliente ID: {} a contenedor ID: {}", request.getIdCliente(), request.getId());

        log.debug("Buscando contenedor ID: {} para asignar cliente.", request.getId());
        Contenedor contenedor = this.contenedorRepository.getById(request.getId());

        if (contenedor == null) {
            throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
        }
        if (contenedor.getCliente() != null){
            throw new ServiceError("", Errores.USUARIO_YA_ASIGNADO, 400);
        }

        log.debug("Buscando usuario ID: {} para asignación.", request.getIdCliente());

        Usuario usuario = this.usuarioClient.getById(request.getIdCliente());

        if(!(usuario.getRol().equals(Rol.CLIENTE.name()))){
            throw new ServiceError("", Errores.USUARIO_NO_CLIENTE, 400);
        }

        log.debug("Asignando cliente y cambiando estado a PROGRAMADO para contenedor ID: {}.", request.getId());
        contenedor.setCliente(usuario);
        contenedor.setEstado(EstadoContenedor.PROGRAMADO);

        this.contenedorRepository.update(contenedor);
        log.info("Cliente ID: {} asignado exitosamente al contenedor ID: {}.", request.getIdCliente(), request.getId());

        return ContenedorMapperDto.toResponsePatchCliente(contenedor, usuario);
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

                if(estadoContenedor !=EstadoContenedor.EN_DEPOSITO && estadoContenedor != EstadoContenedor.ENTREGADO){
                    log.warn("Transición de estado inválida de {} a {} para contenedor ID: {}", contenedor.getEstado(), estadoContenedor, request.getId());
                    throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);
                }

            }
            case EN_DEPOSITO -> {

                if(estadoContenedor != EstadoContenedor.EN_TRANSITO && estadoContenedor != EstadoContenedor.ENTREGADO){
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

        CambioDeEstadoResponse response = ContenedorMapperDto.toResponsePatchEstado(contenedor);

        log.info("Contenedor ID: {} actualizado al estado: {}.", request.getId(), estadoContenedor);
        return response;
    }


    // DELETE

    public void delete(Long id) throws ServiceError {
        log.info("Iniciando eliminación de contenedor ID: {}", id);

        log.debug("Buscando contenedor ID: {} para eliminar.", id);
        Contenedor contenedor = this.contenedorRepository.getById(id);

        if (contenedor == null) {
            throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
        }

        this.contenedorRepository.delete(id);
        log.info("Contenedor ID: {} eliminado exitosamente.", id);
    }

}
