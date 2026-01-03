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

import static net.logstash.logback.argument.StructuredArguments.kv;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ContenedorService {

    private final ContenedorRepository contenedorRepository;

    private final UsuarioClient usuarioClient;

    // GET

    public GetByIdResponse getById(Long id) throws ServiceError {
        log.info("Iniciando búsqueda de contenedor por ID", 
            kv("evento", "busqueda_contenedor_id"), 
            kv("id", id)
        );

        log.debug("Llamando al repositorio para buscar por ID", 
            kv("evento", "busqueda_contenedor_id_repo"), 
            kv("id", id)
        );

        Contenedor contenedor = this.contenedorRepository.getById(id);

        if (contenedor == null) {
            throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
        }

        Usuario usuario = null;

        if(contenedor.getCliente() != null && contenedor.getCliente().getIdUsuario() != null){
            log.debug("Contenedor encontrado. Buscando datos del cliente", 
                kv("evento", "busqueda_cliente_contenedor"), 
                kv("cliente_id", contenedor.getCliente().getIdUsuario())
            );
            usuario = this.usuarioClient.getById(contenedor.getCliente().getIdUsuario());
        }

        contenedor.setCliente(usuario);

        GetByIdResponse response = ContenedorMapperDto.toResponseGetById(contenedor);

        log.info("Búsqueda exitosa del contenedor", 
            kv("evento", "busqueda_contenedor_id_exito"), 
            kv("id", id)
        );
        return response;

    }

    public GetByPesoVolumenResponse getByPesoVolumen(BigDecimal peso, BigDecimal volumen) throws ServiceError {
        log.info("Iniciando búsqueda de contenedor por peso y volumen", 
            kv("evento", "busqueda_contenedor_peso_volumen"), 
            kv("peso", peso), 
            kv("volumen", volumen)
        );

        log.debug("Llamando al repositorio para buscar por peso y volumen", 
            kv("evento", "busqueda_contenedor_peso_volumen_repo")
        );
        Contenedor contenedor = this.contenedorRepository.findByPesoVolumen(peso, volumen);

        if (contenedor == null) {
            throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
        }

        GetByPesoVolumenResponse response = ContenedorMapperDto.toResponseGetByPesoVolumen(contenedor);

        log.info("Búsqueda por peso/volumen exitosa. Contenedor encontrado", 
            kv("evento", "busqueda_contenedor_peso_volumen_exito"), 
            kv("contenedor_id", response.getIdContenedor())
        );
        return response;
    }

    public GetAllResponse getByEstado(String estado) throws ServiceError {
        log.info("Iniciando búsqueda de contenedores por estado", 
            kv("evento", "busqueda_contenedores_estado"), 
            kv("estado", estado)
        );

        log.debug("Validando estado", 
            kv("evento", "validacion_estado"), 
            kv("estado", estado)
        );
        EstadoContenedor estadoContenedor = EstadoContenedor.fromString(estado);

        if (estadoContenedor == null) {
            throw new ServiceError("", Errores.ESTADO_INVALIDO, 400);
        }

        log.debug("Llamando al repositorio para buscar por estado", 
            kv("evento", "busqueda_contenedores_estado_repo"), 
            kv("estado_enum", estadoContenedor)
        );
        List<Contenedor> contenedores = this.contenedorRepository.findByEstado(estadoContenedor);

        GetAllResponse response = ContenedorMapperDto.toResponseGetAll(contenedores);

        log.info("Búsqueda por estado completada", 
            kv("evento", "busqueda_contenedores_estado_exito"), 
            kv("estado", estado), 
            kv("cantidad", contenedores.size())
        );
        return response;
    }

    public GetAllResponse getAll() throws ServiceError {
        log.info("Iniciando búsqueda de todos los contenedores", 
            kv("evento", "busqueda_todos_contenedores")
        );

        log.debug("Llamando al repositorio para obtener todos los contenedores", 
            kv("evento", "busqueda_todos_contenedores_repo")
        );
        List<Contenedor> contenedores = this.contenedorRepository.getAll();

        GetAllResponse response = ContenedorMapperDto.toResponseGetAll(contenedores);

        log.info("Búsqueda de todos los contenedores completada", 
            kv("evento", "busqueda_todos_contenedores_exito"), 
            kv("cantidad", contenedores.size())
        );
        return response;
    }

    // POST

    public RegisterResponse register(RegisterRequest request) throws ServiceError {
        log.info("Iniciando registro de nuevo contenedor", 
            kv("evento", "registro_contenedor"), 
            kv("peso", request.getPeso()), 
            kv("volumen", request.getVolumen())
        );

        Contenedor contenedor = new Contenedor();

        contenedor.setPeso(request.getPeso());
        contenedor.setVolumen(request.getVolumen());
        contenedor.setEstado(EstadoContenedor.BORRADOR);

        if (request.getIdCliente() != null){
            log.debug("Registrando con cliente", 
                kv("evento", "registro_contenedor_cliente"), 
                kv("cliente_id", request.getIdCliente()), 
                kv("estado_inicial", "BORRADOR")
            );

            Usuario usuario = this.usuarioClient.getById(request.getIdCliente());

            contenedor.setCliente(usuario);
        }

        Contenedor saved = this.contenedorRepository.save(contenedor);

        log.info("Nuevo contenedor registrado exitosamente en el repositorio", 
            kv("evento", "registro_contenedor_exito")
        );
        return ContenedorMapperDto.toResponsePostRegister(saved);
    }

    // PATCH

    public EditResponse edit(EditRequest request) throws ServiceError {
        log.info("Iniciando edición de contenedor", 
            kv("evento", "edicion_contenedor"), 
            kv("id", request.getId())
        );

        log.debug("Buscando contenedor para editar", 
            kv("evento", "edicion_contenedor_busqueda"), 
            kv("id", request.getId())
        );
        Contenedor contenedor = this.contenedorRepository.getById(request.getId());

        if (contenedor == null) {
            throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
        }
        if(!contenedor.esBorrador()){
            throw new ServiceError("", Errores.CONTENEDOR_NO_DISPONIBLE, 400);
        }

        if (request.getPeso() != null) {
            log.debug("Actualizando peso", 
                kv("evento", "edicion_contenedor_peso"), 
                kv("peso", request.getPeso())
            );
            contenedor.setPeso(request.getPeso());
        }
        if (request.getVolumen() != null) {
            log.debug("Actualizando volumen", 
                kv("evento", "edicion_contenedor_volumen"), 
                kv("volumen", request.getVolumen())
            );
            contenedor.setVolumen(request.getVolumen());
        }

        this.contenedorRepository.update(contenedor);
        log.info("Contenedor actualizado exitosamente", 
            kv("evento", "edicion_contenedor_exito"), 
            kv("id", request.getId())
        );

        EditResponse response = ContenedorMapperDto.toResponsePatchEdit(contenedor);
        return response;
    }

    public AsignarClienteResponse asignarCliente(AsignarClienteRequest request) throws ServiceError {
        log.info("Iniciando asignación de cliente a contenedor", 
            kv("evento", "asignacion_cliente"), 
            kv("cliente_id", request.getIdCliente()), 
            kv("contenedor_id", request.getId())
        );

        log.debug("Buscando contenedor para asignar cliente", 
            kv("evento", "asignacion_cliente_busqueda_contenedor"), 
            kv("contenedor_id", request.getId())
        );
        Contenedor contenedor = this.contenedorRepository.getById(request.getId());

        if (contenedor == null) {
            throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
        }
        if (contenedor.getCliente() != null){
            throw new ServiceError("", Errores.USUARIO_YA_ASIGNADO, 400);
        }

        log.debug("Buscando usuario para asignación", 
            kv("evento", "asignacion_cliente_busqueda_usuario"), 
            kv("cliente_id", request.getIdCliente())
        );

        Usuario usuario = this.usuarioClient.getById(request.getIdCliente());

        if(!usuario.getRoles().contains(Rol.CLIENTE)){
            throw new ServiceError("", Errores.USUARIO_NO_CLIENTE, 400);
        }

        log.debug("Asignando cliente y cambiando estado a PROGRAMADO", 
            kv("evento", "asignacion_cliente_actualizacion"), 
            kv("contenedor_id", request.getId())
        );
        contenedor.setCliente(usuario);
        contenedor.setEstado(EstadoContenedor.PROGRAMADO);

        this.contenedorRepository.update(contenedor);
        log.info("Cliente asignado exitosamente al contenedor", 
            kv("evento", "asignacion_cliente_exito"), 
            kv("cliente_id", request.getIdCliente()), 
            kv("contenedor_id", request.getId())
        );

        return ContenedorMapperDto.toResponsePatchCliente(contenedor, usuario);
    }

    public CambioDeEstadoResponse cambioDeEstado(CambioDeEstadoRequest request) throws ServiceError {
        log.info("Iniciando cambio de estado para contenedor", 
            kv("evento", "cambio_estado"), 
            kv("contenedor_id", request.getId()), 
            kv("nuevo_estado", request.getEstado())
        );

        log.debug("Buscando contenedor para cambio de estado", 
            kv("evento", "cambio_estado_busqueda"), 
            kv("contenedor_id", request.getId())
        );
        Contenedor contenedor = this.contenedorRepository.getById(request.getId());

        if (contenedor == null) {
            throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
        }

        log.debug("Validando nuevo estado", 
            kv("evento", "cambio_estado_validacion"), 
            kv("nuevo_estado", request.getEstado())
        );

        EstadoContenedor estadoContenedor = EstadoContenedor.fromString(request.getEstado());

        if (estadoContenedor == null) {
            throw new ServiceError("", Errores.ESTADO_INVALIDO, 400);
        }

        log.debug("Validando transición de estado", 
            kv("evento", "cambio_estado_transicion"), 
            kv("estado_actual", contenedor.getEstado()), 
            kv("estado_nuevo", estadoContenedor)
        );

        contenedor.transicionarEstado(estadoContenedor);

        this.contenedorRepository.update(contenedor);

        CambioDeEstadoResponse response = ContenedorMapperDto.toResponsePatchEstado(contenedor);

        log.info("Contenedor actualizado al estado", 
            kv("evento", "cambio_estado_exito"), 
            kv("contenedor_id", request.getId()), 
            kv("estado", estadoContenedor)
        );
        return response;
    }

    // DELETE

    public void delete(Long id) throws ServiceError {
        log.info("Iniciando eliminación de contenedor", 
            kv("evento", "eliminacion_contenedor"), 
            kv("id", id)
        );

        log.debug("Buscando contenedor para eliminar", 
            kv("evento", "eliminacion_contenedor_busqueda"), 
            kv("id", id)
        );
        Contenedor contenedor = this.contenedorRepository.getById(id);

        if (contenedor == null) {
            throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
        }

        this.contenedorRepository.delete(id);
        log.info("Contenedor eliminado exitosamente", 
            kv("evento", "eliminacion_contenedor_exito"), 
            kv("id", id)
        );
    }

}
