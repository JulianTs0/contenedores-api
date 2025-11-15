package backend.grupo130.camiones.service;

import backend.grupo130.camiones.client.usuarios.models.Usuario;
import backend.grupo130.camiones.config.enums.Errores;
import backend.grupo130.camiones.config.enums.Rol;
import backend.grupo130.camiones.config.exceptions.ServiceError;
import backend.grupo130.camiones.data.models.Camion;
import backend.grupo130.camiones.dto.CamionesMapperDto;
import backend.grupo130.camiones.dto.request.*;
import backend.grupo130.camiones.dto.response.EditResponse;
import backend.grupo130.camiones.dto.response.GetAllResponse;
import backend.grupo130.camiones.dto.response.GetByIdResponse;
import backend.grupo130.camiones.repository.CamionRepository;
import backend.grupo130.camiones.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CamionService {

    private final CamionRepository camionRepository;

    private final UsuarioRepository usuarioRepository;

    public GetByIdResponse getById(GetByIdRequest request) throws ServiceError {
        try {
            Camion camion = this.camionRepository.getById(request.getDominio());

            if (camion == null) {
                throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
            }

            Usuario usuario = null;

            if(camion.getIdTransportista() != null){

                usuario = this.usuarioRepository.getById(camion.getIdTransportista());

            }

            GetByIdResponse response = CamionesMapperDto.toResponseGet(camion, usuario);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public GetAllResponse getAll() throws ServiceError {
        try {

            List<Camion> camiones = this.camionRepository.getAll();

            GetAllResponse response = CamionesMapperDto.toResponseGet(camiones);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public GetAllResponse getDisponibles() throws ServiceError {
        try {

            List<Camion> camiones = this.camionRepository.findDisponibilidad();

            GetAllResponse response = CamionesMapperDto.toResponseGet(camiones);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public void register(RegisterRequest request) throws ServiceError {
        try {

            Camion camion = new Camion();

            camion.setDominio(request.getDominio());

            camion.setCapacidadPeso(request.getCapacidadPeso());

            camion.setCapacidadVolumen(request.getCapacidadVolumen());

            camion.setConsumoCombustible(request.getConsumoCombustible());

            camion.setCostoTrasladoBase(request.getCostoTrasladoBase());

            camion.setEstado(true);

            this.camionRepository.save(camion);
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public EditResponse cambiarDisponibilidad(CambiarDisponibilidadRequest request) throws ServiceError {
        try {
            Camion camion = this.camionRepository.getById(request.getDominio());

            if (camion == null) {
                throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
            }

            camion.setEstado(request.getEstado());

            this.camionRepository.update(camion);

            EditResponse response = CamionesMapperDto.toResponsePatch(camion);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public EditResponse edit(EditRequest request) throws ServiceError {
        try {
            Camion camion = this.camionRepository.getById(request.getDominio());

            if (camion == null) {
                throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
            }
            if (!camion.getEstado()){
                throw new ServiceError("", Errores.CAMION_NO_DISPONIBLE, 400);
            }

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

            return response;
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public void asignarTransportista(AsignarTransportistaRequest request) throws ServiceError {
        try {

            Camion camion = this.camionRepository.getById(request.getDominio());

            if (camion == null) {
                throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
            }
            if (!camion.getEstado()){
                throw new ServiceError("", Errores.CAMION_NO_DISPONIBLE, 400);
            }

            Usuario usuario = this.usuarioRepository.getById(request.getIdTransportista());

            if(!(usuario.getRol().equals(Rol.TRANSPORTISTA.name()))){
                throw new ServiceError("", Errores.USUARIO_NO_TRANSPORTISTA, 400);
            }

            camion.setIdTransportista(usuario.getIdUsuario());

            this.camionRepository.update(camion);
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public void delete(DeleteRequest request) throws ServiceError {
        try {

            Camion camion = this.camionRepository.getById(request.getDominio());

            if (camion == null) {
                throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
            }
            if (!camion.getEstado()){
                throw new ServiceError("", Errores.CAMION_NO_DISPONIBLE, 400);
            }

            this.camionRepository.delete(camion.getDominio());
        } catch (ServiceError ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

}
