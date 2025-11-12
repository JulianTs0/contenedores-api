package backend.grupo130.tramos.service;

import backend.grupo130.tramos.client.camiones.models.Camion;
import backend.grupo130.tramos.client.ubicaciones.models.Ubicacion;
import backend.grupo130.tramos.config.enums.Estado;
import backend.grupo130.tramos.config.exceptions.ServiceError;
import backend.grupo130.tramos.data.models.RutaTraslado;
import backend.grupo130.tramos.data.models.Tramo;
import backend.grupo130.tramos.dto.tramo.request.*;
import backend.grupo130.tramos.repository.CamionesRepository;
import backend.grupo130.tramos.repository.RutaRepository;
import backend.grupo130.tramos.repository.TramoRepository;
import backend.grupo130.tramos.repository.UbicacionesRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Transactional
public class TramoService {

    private final TramoRepository tramoRepository;

    private final CamionesRepository camionesRepository;

    private final UbicacionesRepository ubicacionesRepository;

    private final RutaRepository rutaRepository;

    public Tramo getById(TramoGetByIdRequest request) throws ServiceError {
        try {

            Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

            if (tramo == null) {
                throw new ServiceError("Tramo no encontrado", 404);
            }

            Camion camion =  this.camionesRepository.getById(tramo.getDominioCamion());
            tramo.setCamion(camion);

            Ubicacion origen = this.ubicacionesRepository.getUbicacionById(tramo.getIdOrigen());
            Ubicacion destino = this.ubicacionesRepository.getUbicacionById(tramo.getIdDestino());
            tramo.setOrigen(origen);
            tramo.setDestino(destino);

            return tramo;
        } catch (ServiceError ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public List<Tramo> getByTransportista(TramoGetByTransportistaRequest request) throws ServiceError {
        try {

            List<Tramo> tramos = this.tramoRepository.getAll()
                .stream()
                .filter(tramo -> tramo.getDominioCamion().equals(request.getDominioCamion()))
                .toList();

            for(Tramo tramo : tramos){
                Camion camion =  this.camionesRepository.getById(tramo.getDominioCamion());
                Ubicacion origen = this.ubicacionesRepository.getUbicacionById(tramo.getIdOrigen());
                Ubicacion destino = this.ubicacionesRepository.getUbicacionById(tramo.getIdDestino());

                tramo.setCamion(camion);
                tramo.setOrigen(origen);
                tramo.setDestino(destino);
            }

            return tramos;
        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public List<Tramo> getAll() throws ServiceError {
        try {

            List<Tramo> tramos = this.tramoRepository.getAll();

            for(Tramo tramo : tramos){
                Camion camion =  this.camionesRepository.getById(tramo.getDominioCamion());
                Ubicacion origen = this.ubicacionesRepository.getUbicacionById(tramo.getIdOrigen());
                Ubicacion destino = this.ubicacionesRepository.getUbicacionById(tramo.getIdDestino());

                tramo.setCamion(camion);
                tramo.setOrigen(origen);
                tramo.setDestino(destino);
            }

            return tramos;
        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public void asignarCamion(TramoAsignacionCamionRequest request) throws ServiceError {
        try {

            Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

            if (tramo == null) {
                throw new ServiceError("Tramo no encontrado", 404);
            }

            if (!tramo.esEstimado()) {
                throw new ServiceError("El tramo ya fue asignado", 400);
            }

            Camion camion =  this.camionesRepository.getById(request.getDominioCamion());

            if (camion == null) {
                throw new ServiceError("Camion no encontrado", 404);
            }

            if (!camion.estaDisponible()){
                throw new ServiceError("El camion no esta disponible", 400);
            }

            tramo.setDominioCamion(camion.getDominio());

            tramo.setEstado(Estado.ASIGNADO);

            this.tramoRepository.update(tramo);
        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public void registrarInicioTramo(TramoInicioTramoRequest request) throws ServiceError {
        try {

            Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

            if (tramo == null) {
                throw new ServiceError("Tramo no encontrado", 404);
            }

            if (!tramo.esAsignado()) {
                throw new ServiceError("No se puede registar el inicio de un tramo que no esta asignado", 400);
            }

            if (!tramo.getDominioCamion().equals(request.getDominioCamion())){
                throw new ServiceError("No esta autorizado a registrar esta accion", 401);
            }

            Camion camion =  this.camionesRepository.getById(request.getDominioCamion());

            if (camion == null) {
                throw new ServiceError("Camion no encontrado", 404);
            }

            if (!camion.estaDisponible()){
                throw new ServiceError("El camion no esta disponible", 400);
            }

            tramo.setFechaHoraInicioReal(LocalDateTime.now());

            tramo.setEstado(Estado.INICIADO);

            this.tramoRepository.update(tramo);
        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public void registrarFinTramo(TramoFinTramoRequest request) throws ServiceError {
        try {

            Tramo tramo = this.tramoRepository.getById(request.getIdTramo());

            if (tramo == null) {
                throw new ServiceError("Tramo no encontrado", 404);
            }

            if (!tramo.esIniciado()) {
                throw new ServiceError("No se puede registar el fin de un tramo que no esta iniciado", 400);
            }

            if (!tramo.getDominioCamion().equals(request.getDominioCamion())){
                throw new ServiceError("No esta autorizado a registrar esta accion", 401);
            }

            Camion camion =  this.camionesRepository.getById(request.getDominioCamion());

            if (camion == null) {
                throw new ServiceError("Camion no encontrado", 404);
            }

            tramo.setFechaHoraFinEstimado(LocalDateTime.now());

            tramo.setEstado(Estado.FINALZADO);

            this.tramoRepository.update(tramo);

            if(tramo.getOrden().equals(tramo.getRutaTraslado().getCantidadTramos())){
                // Cambiar Solicitud a entregada
            }

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
