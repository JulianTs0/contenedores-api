package backend.grupo130.tramos.service;

import backend.grupo130.tramos.config.exceptions.ServiceError;
import backend.grupo130.tramos.data.models.Tramo;
import backend.grupo130.tramos.dto.tramo.request.TramoGetByIdRequest;
import backend.grupo130.tramos.repository.RutaRepository;
import backend.grupo130.tramos.repository.TramoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class TramoService {

    private final TramoRepository tramoRepository;

    public Tramo getById(TramoGetByIdRequest request) throws ServiceError {
        try {

            Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

            if (tramo == null) {
                throw new ServiceError("Contenedor no encontrado", 404);
            }

            return tramo;
        } catch (ServiceError ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public List<Tramo> getAll() throws ServiceError {
        try {

            List<Tramo> tramos = this.tramoRepository.getAll();

            /*for(Tramo tramo : tramos){
                Usuario usuario = this.usuarioRepository.getById(contenedor.getIdCliente());
                contenedor.setUsuario(usuario);
            }*/

            return tramos;
        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    /* public void register(TramoRegisterRequest request) throws ServiceError {
        try {

            Tramo tramo = new Tramo();

            TipoTramo tipo = TipoTramo.fromString(request.getTipoTramo());
            if (tipo == null){
                throw new ServiceError("Tipo de tramo no encontrado", 404);
            }

            tramo.setTipoTramo(tipo);
            tramo.setCostoAproximado(request.getCostoAproximado());
            tramo.setFechaHoraInicioEstimado(request.getFechaHoraInicioEstimado());
            tramo.setFechaHoraFinEstimado(request.getFechaHoraFinEstimado());

            tramo.setEstado(Estado.ESTIMADO);

            tramo.setOrden(0);

            this.tramoRepository.save(tramo);
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    } */

    /*public Contenedor edit(EditRequest request) throws ServiceError {
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
                Usuario usuario = this.usuarioRepository.getById(contenedor.getIdCliente());

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
    }*/

}
