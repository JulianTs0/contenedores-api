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
@Slf4j
public class ContenedorService {

    private final ContenedorRepository contenedorRepository;

    private final UsuarioRepository usuarioRepository;

    // GET

    public GetByIdResponse getById(GetByIdRequest request) throws ServiceError {
        try {

            Contenedor contenedor = this.contenedorRepository.getById(request.getIdContenedor());

            if (contenedor == null) {
                throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
            }

            Usuario usuario = null;

            if(contenedor.getIdCliente() != null){

                usuario = this.usuarioRepository.getById(contenedor.getIdCliente());

            }

            GetByIdResponse response = ContenedorMapperDto.toResponseGet(contenedor, usuario);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public GetByPesoVolumenResponse getByPesoVolumen(GetByPesoVolumenRequest request) throws ServiceError {
        try {

            Contenedor contenedor = this.contenedorRepository.findByPesoVolumen(request.getPeso(), request.getVolumen());

            if (contenedor == null) {
                throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
            }

            GetByPesoVolumenResponse response = ContenedorMapperDto.toResponseGet(contenedor);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public GetAllResponse getByEstado(GetByEstado request) throws ServiceError {
        try {

            Estado estado = Estado.fromString(request.getEstado());
            if (estado == null) {
                throw new ServiceError("", Errores.ESTADO_INVALIDO, 400);
            }

            List<Contenedor> contenedores = this.contenedorRepository.findByEstado(estado);


            GetAllResponse response = ContenedorMapperDto.toResponseGet(contenedores);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public GetAllResponse getAll() throws ServiceError {
        try {

            List<Contenedor> contenedores = this.contenedorRepository.getAll();

            GetAllResponse response = ContenedorMapperDto.toResponseGet(contenedores);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    // POST

    public void register(RegisterRequest request) throws ServiceError {
        try {

            Contenedor contenedor = new Contenedor();

            contenedor.setPeso(request.getPeso());
            contenedor.setVolumen(request.getVolumen());

            if (request.getIdCliente() != null){
                try {

                    Usuario usuario = this.usuarioRepository.getById(request.getIdCliente());

                    contenedor.setIdCliente(usuario.getIdUsuario());

                    contenedor.setEstado(Estado.BORRADOR);

                } catch (ServiceError ex) {

                    if (ex.getMessage() != null && ex.getMessage().contains("[404]")){
                        throw new ServiceError(ex.getMessage(), Errores.USUARIO_NO_ENCONTRADO, 404);
                    } else {
                        throw ex;
                    }

                }
            } else {
                contenedor.setEstado(Estado.BORRADOR);
            }

            this.contenedorRepository.save(contenedor);
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    // PATCH

    public EditResponse edit(EditRequest request) throws ServiceError {
        try {

            Contenedor contenedor = this.contenedorRepository.getById(request.getId());

            if (contenedor == null) {
                throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
            }

            if (request.getPeso() != null) {
                contenedor.setPeso(request.getPeso());
            }
            if (request.getVolumen() != null) {
                contenedor.setVolumen(request.getVolumen());
            }
            if (request.getIdCliente() != null) {
                Usuario usuario = this.usuarioRepository.getById(request.getIdCliente());

                contenedor.setIdCliente(usuario.getIdUsuario());
            }

            this.contenedorRepository.update(contenedor);

            EditResponse response = ContenedorMapperDto.toResponsePatch(contenedor);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public void asignarCliente(AsignarClienteRequest request) throws ServiceError {
        try {

            Contenedor contenedor = this.contenedorRepository.getById(request.getId());

            if (contenedor == null) {
                throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
            }
            if (contenedor.getIdCliente() != null){
                throw new ServiceError("", Errores.USUARIO_YA_ASIGNADO, 400);
            }

            Usuario usuario = this.usuarioRepository.getById(request.getIdCliente());

            if(!(usuario.getRol().equals(Rol.CLIENTE.name()))){
                throw new ServiceError("", Errores.USUARIO_NO_CLIENTE, 400);
            }

            contenedor.setIdCliente(usuario.getIdUsuario());
            contenedor.setEstado(Estado.PROGRAMADO);

            this.contenedorRepository.update(contenedor);
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public CambioDeEstadoResponse cambioDeEstado(CambioDeEstadoRequest request) throws ServiceError {
        try {

            Contenedor contenedor = this.contenedorRepository.getById(request.getId());

            if (contenedor == null) {
                throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
            }

            Estado estado = Estado.fromString(request.getEstado());
            if (estado == null) {
                throw new ServiceError("", Errores.ESTADO_INVALIDO, 400);
            }

            switch (contenedor.getEstado()){
                case BORRADOR -> {
                    if(estado != Estado.PROGRAMADO){
                        throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);
                    }
                }
                case PROGRAMADO -> {
                    if(estado != Estado.EN_TRANSITO){
                        throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);
                    }
                }
                case EN_TRANSITO -> {
                    if(estado != Estado.EN_DEPOSITO && estado != Estado.ENTREGADO){
                        throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);
                    }
                }
                case EN_DEPOSITO -> {
                    if(estado != Estado.EN_TRANSITO && estado != Estado.ENTREGADO){
                        throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);
                    }
                }
                case ENTREGADO -> {
                    throw new ServiceError("", Errores.CONTENEDOR_YA_ENTREGADO, 400);
                }
            }

            contenedor.setEstado(estado);

            this.contenedorRepository.update(contenedor);

            CambioDeEstadoResponse response = ContenedorMapperDto.toResponsePatch(contenedor.getIdContenedor(), estado);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }


    // DELETE

    public void delete(DeleteRequest request) throws ServiceError {
        try {

            Contenedor contenedor = this.contenedorRepository.getById(request.getIdContenedor());

            if (contenedor == null) {
                throw new ServiceError("", Errores.CONTENEDOR_NO_ENCONTRADO, 404);
            }

            this.contenedorRepository.delete(contenedor.getIdContenedor());
        } catch (ServiceError ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

}
