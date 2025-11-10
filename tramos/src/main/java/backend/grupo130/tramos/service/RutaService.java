package backend.grupo130.tramos.service;

import backend.grupo130.tramos.client.OSRM.OsrmApiClient;
import backend.grupo130.tramos.client.OSRM.response.RouteResponse;
import backend.grupo130.tramos.client.envios.models.SolicitudTraslado;
import backend.grupo130.tramos.client.ubicaciones.models.Ubicacion;
import backend.grupo130.tramos.config.enums.Estado;
import backend.grupo130.tramos.config.enums.TipoTramo;
import backend.grupo130.tramos.config.exceptions.ServiceError;
import backend.grupo130.tramos.data.models.RutaTraslado;
import backend.grupo130.tramos.data.models.Tramo;
import backend.grupo130.tramos.dto.ruta.request.RutaGetByIdRequest;
import backend.grupo130.tramos.dto.ruta.request.RutaRegisterRequest;
import backend.grupo130.tramos.dto.tramo.request.TramoRegisterRequest;
import backend.grupo130.tramos.repository.EnviosRepository;
import backend.grupo130.tramos.repository.RutaRepository;
import backend.grupo130.tramos.repository.TramoRepository;
import backend.grupo130.tramos.repository.UbicacionesRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class RutaService {

    private final RutaRepository rutaRepository;

    private final TramoRepository tramoRepository;

    private final EnviosRepository enviosRepository;

    private final UbicacionesRepository ubicacionesRepository;

    private final OsrmApiClient osrmApiClient;

    public RutaTraslado getById(RutaGetByIdRequest request) throws ServiceError {
        try {

            RutaTraslado ruta = this.rutaRepository.getById(request.getIdRuta());

            if (ruta == null) {
                throw new ServiceError("Ruta no encontrada", 404);
            }

            SolicitudTraslado solicitud = this.enviosRepository.getSolicitudTrasladoById(ruta.getIdSolicitud());

            ruta.setSolicitud(solicitud);

            return ruta;
        } catch (ServiceError ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public List<RutaTraslado> getAll() throws ServiceError {
        try {

            List<RutaTraslado> rutas = this.rutaRepository.getAll();

            for(RutaTraslado ruta : rutas){
                SolicitudTraslado solicitud = this.enviosRepository.getSolicitudTrasladoById(ruta.getIdSolicitud());
                ruta.setSolicitud(solicitud);
            }

            return rutas;
        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public void register(RutaRegisterRequest request) throws ServiceError {
        try {

            SolicitudTraslado solicitud = this.enviosRepository.getSolicitudTrasladoById(request.getIdSolicitud());

            if(solicitud == null){
                throw new ServiceError("Solicitud no encontrada", 404);
            }

            RutaTraslado ruta = new RutaTraslado();

            ruta.setCargosGestionFijo(request.getCargosGestionFijo());

            ruta.setIdSolicitud(request.getIdSolicitud());

            ruta.setCantidadTramos(0);

            ruta.setCantidadDepositos(0);

            this.rutaRepository.save(ruta);

            Map<Integer,Ubicacion> ubicaciones =
                this.ubicacionesRepository.getUbicacionAll().stream()
                    .collect(Collectors.toMap(
                        Ubicacion::getIdUbicacion,
                            ubicacion -> ubicacion));
            List<Tramo> tramos = new ArrayList<>();
            Set<Integer> depositos = new HashSet<>();

            for (int orden = 0; orden < request.getUbicaciones().size() - 1; orden++){

                Ubicacion origen = ubicaciones.get(request.getUbicaciones().get(orden));

                if (origen == null){
                    throw new ServiceError("Los tramos son invalidos debido a su ubicacion", 404);
                }

                Ubicacion destino = ubicaciones.get(request.getUbicaciones().get(orden+1));

                if (destino == null){
                    throw new ServiceError("Los tramos son invalidos debido a su ubicacion", 404);
                }

                Tramo tramo = new Tramo();

                if (orden == 0){
                    tramo.setFechaHoraInicioEstimado(request.getFechaHoraInicioEstimado());
                } else {
                    tramo.setFechaHoraInicioEstimado(tramos.getLast().getFechaHoraFinEstimado());
                }

                RouteResponse routeResponse = this.osrmApiClient.calcularDistancia(origen, destino).getRoutes().get(0);

                Long segundos = Math.round(routeResponse.getDuration());

                LocalDateTime finTramo = tramo.getFechaHoraInicioEstimado().plusSeconds(segundos);

                tramo.setFechaHoraFinEstimado(finTramo);

                tramo.setOrigen(origen);

                tramo.setDestino(destino);

                if(origen.getIdDeposito() == null && destino.getIdDeposito() == null){
                    tramo.setTipoTramo(TipoTramo.ORIGEN_DESTINO);
                }
                else if (origen.getIdDeposito() == null){
                    tramo.setTipoTramo(TipoTramo.ORIGEN_DEPOSITO);
                    depositos.add(destino.getIdDeposito());
                }
                else if (destino.getIdDeposito() == null){
                    tramo.setTipoTramo(TipoTramo.DEPOSITO_DESTINO);
                    depositos.add(origen.getIdDeposito());
                }
                else {
                    tramo.setTipoTramo(TipoTramo.DEPOSITO_DEPOSITO);
                    depositos.add(destino.getIdDeposito());
                    depositos.add(origen.getIdDeposito());
                }

                tramo.setOrden(orden);

                tramo.setRutaTraslado(ruta);

                tramo.setEstado(Estado.ESTIMADO);

                tramos.add(tramo);
            }

            if (!tramos.isEmpty()) {
                this.tramoRepository.saveAll(tramos);
            }

            ruta.setCantidadTramos(tramos.size());

            ruta.setCantidadDepositos(depositos.size());

            this.rutaRepository.update(ruta);
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

}
