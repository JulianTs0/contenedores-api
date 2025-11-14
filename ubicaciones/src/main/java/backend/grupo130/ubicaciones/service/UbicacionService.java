package backend.grupo130.ubicaciones.service;

import backend.grupo130.ubicaciones.Repository.DepositoRepository;
import backend.grupo130.ubicaciones.Repository.UbicacionRepository;
import backend.grupo130.ubicaciones.config.exceptions.ServiceError;
import backend.grupo130.ubicaciones.data.models.Ubicacion;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionEditRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionGetByIdRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionRegisterRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class UbicacionService {

    private final UbicacionRepository ubicacionRepository;

    private final DepositoRepository depositoRepository;

    public Ubicacion getById(UbicacionGetByIdRequest request) throws ServiceError {
        try {

            Ubicacion ubicacion = this.ubicacionRepository.getById(request.getIdUbicacion());

            if (ubicacion == null) {
                throw new ServiceError("Ubicacion no encontrado", 404);
            }


            return ubicacion;
        } catch (ServiceError ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public List<Ubicacion> getAll() throws ServiceError {
        try {

            List<Ubicacion> ubicaciones = this.ubicacionRepository.getAll();

            return ubicaciones;
        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public void register(UbicacionRegisterRequest request) throws ServiceError {
        try {

            Ubicacion ubicacion = new Ubicacion();

            ubicacion.setDireccionTextual(request.getDireccion());

            ubicacion.setLatitud(request.getLatitud());
            ubicacion.setLongitud(request.getLongitud());

            this.ubicacionRepository.save(ubicacion);
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public Ubicacion edit(UbicacionEditRequest request) throws ServiceError {
        try {

            Ubicacion ubicacion = this.ubicacionRepository.getById(request.getUbicacionId());

            if (ubicacion == null) {
                throw new ServiceError("Ubicacion no encontrado", 404);
            }

            if (request.getLatitud() != null) {
                ubicacion.setLatitud(request.getLatitud());
            }
            if (request.getLongitud() != null) {
                ubicacion.setLongitud(request.getLongitud());
            }
            if(request.getDireccion() != null){
                ubicacion.setDireccionTextual(request.getDireccion());
            }

            this.ubicacionRepository.update(ubicacion);

            return ubicacion;
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

}
