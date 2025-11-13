package backend.grupo130.tramos.service;

import backend.grupo130.tramos.client.camiones.models.Camion;
import backend.grupo130.tramos.client.envios.models.SolicitudTraslado;
import backend.grupo130.tramos.client.ubicaciones.models.Ubicacion;
import backend.grupo130.tramos.config.enums.Estado;
import backend.grupo130.tramos.config.exceptions.ServiceError;
import backend.grupo130.tramos.data.models.RutaTraslado;
import backend.grupo130.tramos.data.models.Tramo;
import backend.grupo130.tramos.dto.tramo.request.*;
import backend.grupo130.tramos.repository.*;
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

    private final EnviosRepository enviosRepository;

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

            List<Tramo> tramosDeLaRuta = tramo.getRutaTraslado().getTramos();

            boolean todosAsignados = tramosDeLaRuta.stream()
                .allMatch(Tramo::esAsignado);

            if (todosAsignados) {
                // Cambiar solicitud a programada
            }

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

            RutaTraslado ruta = tramo.getRutaTraslado();

            SolicitudTraslado solicitudTraslado = this.enviosRepository.getSolicitudTrasladoById(ruta.getIdSolicitud());

            if(solicitudTraslado.esBorrador() || solicitudTraslado.esEntregada()){
                throw new ServiceError("No se puede registar el inicio de un tramo de una solicitud que no esta programada", 400);
            }

            List<Tramo> tramosDeLaRuta = ruta.getTramos();

            int ordenActual = tramo.getOrden();

            if (ordenActual > 0) {

                Tramo tramoAnterior = tramosDeLaRuta.get(ordenActual - 1);

                if (!tramoAnterior.esFinalizado()) {
                    throw new ServiceError("No se puede iniciar este tramo. El tramo anterior aún no ha sido terminado.", 400);
                }
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

            if(tramo.getOrden() == 0){
                // Cambiar Solicitud a Iniciada
            }

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

            RutaTraslado ruta = tramo.getRutaTraslado();

            SolicitudTraslado solicitudTraslado = this.enviosRepository.getSolicitudTrasladoById(ruta.getIdSolicitud());

            if(!solicitudTraslado.esEntransito()){
                throw new ServiceError("No se puede registar el fin de un tramo de una solicitud que no esta iniciada", 400);
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

}
