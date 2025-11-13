package backend.grupo130.camiones.service;

import backend.grupo130.camiones.config.exceptions.ServiceError;
import backend.grupo130.camiones.data.models.Camion;
import backend.grupo130.camiones.dto.request.EditRequest;
import backend.grupo130.camiones.dto.request.GetByIdRequest;
import backend.grupo130.camiones.dto.request.RegisterRequest;
import backend.grupo130.camiones.repository.CamionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class CamionService {

    private final CamionRepository camionRepository;

    // Obtener un camión por dominio (patente)
    public Camion getById(GetByIdRequest request) throws ServiceError {
        try {
            Camion camion = this.camionRepository.getByDominio(request.getDominio());

            if (camion == null) {
                throw new ServiceError("Camión no encontrado, 404");
            }

            return camion;
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError("Error interno, 500");
        }
    }

    // Obtener todos los camiones
    public List<Camion> getAll() throws ServiceError {
        try {
            return this.camionRepository.getAll();
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError("Error interno, 500");
        }
    }

    // Registrar un nuevo camión
    public void register(RegisterRequest request) throws ServiceError {
        try {
            Camion camion = new Camion();

            camion.setDominio(request.getDominio());
            camion.setNombreTransportista(request.getNombreTransportista());
            camion.setTelefonoContacto(request.getTelefonoContacto());
            camion.setCapacidadPeso(request.getCapacidadPeso());
            camion.setCapacidadVolumen(request.getCapacidadVolumen());
            camion.setConsumoKm(request.getConsumoKm());
            camion.setCostoKm(request.getCostoKm());
            camion.setDisponible(true);
            camion.setObservaciones(request.getObservaciones());

            this.camionRepository.save(camion);
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError("Error interno, 500");
        }
    }

    // Editar camión existente
    public Camion edit(EditRequest request) throws ServiceError {
        try {
            Camion camion = this.camionRepository.getByDominio(request.getDominio());

            if (camion == null) {
                throw new ServiceError("Camión no encontrado, 404");
            }

            if (request.getNombreTransportista() != null) {
                camion.setNombreTransportista(request.getNombreTransportista());
            }
            if (request.getTelefonoContacto() != null) {
                camion.setTelefonoContacto(request.getTelefonoContacto());
            }
            if (request.getCapacidadPeso() != null) {
                camion.setCapacidadPeso(request.getCapacidadPeso());
            }
            if (request.getCapacidadVolumen() != null) {
                camion.setCapacidadVolumen(request.getCapacidadVolumen());
            }
            if (request.getConsumoKm() != null) {
                camion.setConsumoKm(request.getConsumoKm());
            }
            if (request.getCostoKm() != null) {
                camion.setCostoKm(request.getCostoKm());
            }
            if (request.getDisponible() != null) {
                camion.setDisponible(request.getDisponible());
            }
            if (request.getObservaciones() != null) {
                camion.setObservaciones(request.getObservaciones());
            }

            this.camionRepository.save(camion);
            return camion;
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError("Error interno, 500");
        }
    }
}
