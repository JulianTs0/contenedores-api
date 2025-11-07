package backend.grupo130.contenedores.service;

import backend.grupo130.contenedores.client.usuarios.models.Usuario;
import backend.grupo130.contenedores.config.enums.Estado;
import backend.grupo130.contenedores.config.exceptions.ServiceError;
import backend.grupo130.contenedores.data.models.Contenedor;
import backend.grupo130.contenedores.dto.request.CambioDeEstadoRequest;
import backend.grupo130.contenedores.dto.request.EditRequest;
import backend.grupo130.contenedores.dto.request.GetByIdRequest;
import backend.grupo130.contenedores.dto.request.RegisterRequest;
import backend.grupo130.contenedores.repository.ContenedorRepository;
import backend.grupo130.contenedores.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.print.attribute.EnumSyntax;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class ContenedorService {

    private final ContenedorRepository contenedorRepository;

    private final UsuarioRepository usuarioRepository;

    public Contenedor getById(GetByIdRequest request) throws ServiceError {
        try {

            Contenedor contenedor = this.contenedorRepository.getById(request.getIdContenedor());

            if (contenedor == null) {
                throw new ServiceError("Contenedor no encontrado", 404);
            }

            Usuario usuario = this.usuarioRepository.getById(contenedor.getIdCliente());

            contenedor.setUsuario(usuario);

            return contenedor;
        } catch (ServiceError ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public List<Contenedor> getAll() throws ServiceError {
        try {

            List<Contenedor> contenedores = this.contenedorRepository.getAll();

            for(Contenedor contenedor : contenedores){
                Usuario usuario = this.usuarioRepository.getById(contenedor.getIdCliente());
                contenedor.setUsuario(usuario);
            }

            return contenedores;
        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public void register(RegisterRequest request) throws ServiceError {
        try {

            Contenedor contenedor = new Contenedor();

            contenedor.setPeso(request.getPeso());
            contenedor.setVolumen(request.getVolumen());

            Usuario usuario = this.usuarioRepository.getById(request.getIdCliente());

            contenedor.setUsuario(usuario);
            contenedor.setIdCliente(request.getIdCliente());

            contenedor.setEstado(Estado.BORRADOR);

            this.contenedorRepository.save(contenedor);
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public Contenedor edit(EditRequest request) throws ServiceError {
        try {

            Contenedor contenedor = this.contenedorRepository.getById(request.getId());

            if (contenedor == null) {
                throw new ServiceError("Contenedor no encontrado", 404);
            }

            if (request.getPeso() != null) {
                contenedor.setPeso(request.getPeso());
            }
            if (request.getVolumen() != null) {
                contenedor.setVolumen(request.getVolumen());
            }
            if (request.getIdCliente() != null) {
                Usuario usuario = this.usuarioRepository.getById(request.getIdCliente());

                contenedor.setUsuario(usuario);
                contenedor.setIdCliente(request.getIdCliente());
            }

            this.contenedorRepository.update(contenedor);

            return contenedor;
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public void cambioDeEstado(CambioDeEstadoRequest request) throws ServiceError {
        try {

            Contenedor contenedor = this.contenedorRepository.getById(request.getId());

            if (contenedor == null) {
                throw new ServiceError("Contenedor no encontrado", 404);
            }

            Estado estado = Estado.fromString(request.getEstado());
            if (estado == null) {
                throw new ServiceError("El estado no es valido", 400);
            }

            switch (contenedor.getEstado()){
                case BORRADOR -> {
                    if(estado != Estado.PROGRAMADO){
                        throw new ServiceError("Transicion no valida", 400);
                    }
                }
                case PROGRAMADO -> {
                    if(estado != Estado.EN_TRANSITO){
                        throw new ServiceError("Transicion no valida", 400);
                    }
                }
                case EN_TRANSITO -> {
                    if(estado != Estado.EN_DEPOSITO && estado != Estado.ENTREGADO){
                        throw new ServiceError("Transicion no valida", 400);
                    }
                }
                case EN_DEPOSITO -> {
                    if(estado != Estado.EN_TRANSITO && estado != Estado.ENTREGADO){
                        throw new ServiceError("Transicion no valida", 400);
                    }
                }
                case ENTREGADO -> {
                    if(estado != Estado.EN_TRANSITO && estado != Estado.ENTREGADO){
                        throw new ServiceError("El container ya fue entregado", 400);
                    }
                }
            }

            this.contenedorRepository.update(contenedor);
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

}
