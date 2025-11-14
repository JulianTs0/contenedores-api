package backend.grupo130.camiones.service;

import backend.grupo130.camiones.client.usuarios.models.Usuario;
import backend.grupo130.camiones.config.exceptions.ServiceError;
import backend.grupo130.camiones.data.models.Camion;
import backend.grupo130.camiones.dto.request.EditRequest;
import backend.grupo130.camiones.dto.request.GetByIdRequest;
import backend.grupo130.camiones.dto.request.RegisterRequest;
import backend.grupo130.camiones.repository.CamionRepository;
import backend.grupo130.camiones.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class CamionService {

    private final CamionRepository camionRepository;

    private final UsuarioRepository usuarioRepository;

    public Camion getById(GetByIdRequest request) throws ServiceError {
        try {
            Camion camion = this.camionRepository.getById(request.getDominio());

            if (camion == null) {
                throw new ServiceError("Camión no encontrado", 404);
            }

            if(camion.getIdTransportista() != null){

                Usuario usuario = this.usuarioRepository.getById(camion.getIdTransportista());

                camion.setTransportista(usuario);

            }

            return camion;
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public List<Camion> getAll() throws ServiceError {
        try {

            List<Camion> camiones = this.camionRepository.getAll();

            for (Camion camion : camiones){
                if(camion.getIdTransportista() != null){

                    Usuario usuario = this.usuarioRepository.getById(camion.getIdTransportista());

                    camion.setTransportista(usuario);

                }
            }

            return camiones;
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
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
            throw new ServiceError("Error interno", 500);
        }
    }

    public Camion edit(EditRequest request) throws ServiceError {
        try {
            Camion camion = this.camionRepository.getById(request.getDominio());

            if (camion == null) {
                throw new ServiceError("Camión no encontrado", 404);
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
            if (request.getIdTransportista() != null) {
                Usuario usuario = this.usuarioRepository.getById(camion.getIdTransportista());

                camion.setTransportista(usuario);
            }

            this.camionRepository.update(camion);
            return camion;
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }
}
