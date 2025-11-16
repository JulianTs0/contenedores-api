package backend.grupo130.tramos.service;

import backend.grupo130.tramos.client.OSRM.OsrmApiClient;
import backend.grupo130.tramos.client.OSRM.response.RouteResponse;
import backend.grupo130.tramos.client.camiones.models.Camion;
import backend.grupo130.tramos.client.contenedores.models.Contenedor;
import backend.grupo130.tramos.client.envios.models.SolicitudTraslado;
import backend.grupo130.tramos.client.envios.models.Tarifa;
import backend.grupo130.tramos.client.envios.request.SolicitudEditRequest;
import backend.grupo130.tramos.client.ubicaciones.models.Ubicacion;
import backend.grupo130.tramos.config.enums.Errores;
import backend.grupo130.tramos.config.enums.EstadoTramo;
import backend.grupo130.tramos.config.enums.TipoTramo;
import backend.grupo130.tramos.config.exceptions.ServiceError;
import backend.grupo130.tramos.data.models.RutaTraslado;
import backend.grupo130.tramos.data.models.Tramo;
import backend.grupo130.tramos.dto.ruta.RutaMapperDto;
import backend.grupo130.tramos.dto.ruta.request.RutaAsignarSolicitudRequest;
import backend.grupo130.tramos.dto.ruta.request.RutaGetByIdRequest;
import backend.grupo130.tramos.dto.ruta.request.RutaGetOpcionesRequest;
import backend.grupo130.tramos.dto.ruta.request.RutaRegisterRequest;
import backend.grupo130.tramos.dto.ruta.response.RutaGetByIdResponse;
import backend.grupo130.tramos.dto.ruta.response.RutaGetOpcionesResponse;
import backend.grupo130.tramos.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class RutaService {

    private final RutaRepository rutaRepository;

    private final TramoRepository tramoRepository;

    private final EnviosRepository enviosRepository;

    private final UbicacionesRepository ubicacionesRepository;

    private final OsrmApiClient osrmApiClient;

    private final ContenedorRepository contenedorRepository;

    private final CamionesRepository camionesRepository;

    public RutaGetByIdResponse getById(RutaGetByIdRequest request) throws ServiceError {
        try {

            RutaTraslado ruta = this.rutaRepository.getById(request.getIdRuta());

            if (ruta == null) {
                throw new ServiceError("", Errores.RUTA_NO_ENCONTRADA, 404);
            }

            SolicitudTraslado solicitud = null;

            if (ruta.getIdSolicitud() != null){
                solicitud = this.enviosRepository.getSolicitudTrasladoById(ruta.getIdSolicitud());
            }

            RutaGetByIdResponse response = RutaMapperDto.toResponseGet(ruta, solicitud);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public List<RutaTraslado> getAll() throws ServiceError {
        try {

            List<RutaTraslado> rutas = this.rutaRepository.getAll();

            return rutas;
        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public void register(RutaRegisterRequest request) throws ServiceError {
        try {

            SolicitudTraslado solicitud = this.enviosRepository.getSolicitudTrasladoById(request.getIdSolicitud());

            if(solicitud == null){
                throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
            }

            RutaTraslado rutaTraslado = this.rutaRepository.getById(request.getIdRuta());

            if(rutaTraslado == null){
                throw new ServiceError("", Errores.RUTA_NO_ENCONTRADA, 404);
            }

            rutaTraslado.setIdSolicitud(solicitud.getIdSolicitud());

            this.rutaRepository.update(rutaTraslado);
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public RutaGetOpcionesResponse getRutaTentativa(RutaGetOpcionesRequest request) throws ServiceError {
        try {

            SolicitudTraslado solicitud = this.enviosRepository.getSolicitudTrasladoById(request.getIdSolicitud());

            Tarifa tarifa = solicitud.getTarifa();
            if (tarifa == null) {
                log.warn("La solicitud {} no tiene tarifa (es nula). Creando una nueva instancia de Tarifa en memoria.", solicitud.getIdSolicitud());
                tarifa = new Tarifa();
                tarifa.setValorLitro(BigDecimal.valueOf(10));
            }

            Contenedor contenedor = this.contenedorRepository.getById(solicitud.getIdContenedor());

            BigDecimal costoBasePromedio = this.camionesRepository.getPromedioCostoBase(contenedor.getPeso(), contenedor.getVolumen());

            if (costoBasePromedio.equals(BigDecimal.ZERO)){
                throw new ServiceError("", Errores.CAMIONES_NO_ENCONTRADOS, 404);
            }

            BigDecimal consumoAprox = this.camionesRepository.getConsumoPromedio();

            if (consumoAprox.equals(BigDecimal.ZERO)){
                throw new ServiceError("", Errores.CAMIONES_NO_ENCONTRADOS, 404);
            }

            RutaTraslado ruta = new RutaTraslado();

            ruta.setCargosGestionFijo(request.getCargosGestionFijo());

            ruta.setCantidadTramos(0);

            ruta.setCantidadDepositos(0);

            this.rutaRepository.save(ruta);

            List<Tramo> tramos = new ArrayList<>();
            Set<Long> depositos = new HashSet<>();
            BigDecimal costoEstadiaTotal = BigDecimal.ZERO;
            double distanciaTotal = 0;
            double tiempoTotal = 0;

            for (int orden = 0; orden < request.getUbicaciones().size() - 1; orden++){

                Ubicacion origen = this.ubicacionesRepository.getUbicacionById(request.getUbicaciones().get(orden));

                if (origen == null){
                    throw new ServiceError("", Errores.TRAMOS_UBICACION_INVALIDA, 404);
                }

                Ubicacion destino = this.ubicacionesRepository.getUbicacionById(request.getUbicaciones().get(orden + 1));

                if (destino == null){
                    throw new ServiceError("", Errores.TRAMOS_UBICACION_INVALIDA, 404);
                }

                Tramo tramo = new Tramo();

                if (orden == 0){
                    tramo.setFechaHoraInicioEstimado(request.getFechaHoraInicioEstimado());
                } else {
                    tramo.setFechaHoraInicioEstimado(tramos.getLast().getFechaHoraFinEstimado());
                }

                RouteResponse routeResponse = this.osrmApiClient.calcularDistancia(origen, destino).getRoutes().get(0);

                Long segundos = Math.round(routeResponse.getDuration());

                distanciaTotal += routeResponse.getDistance();
                tiempoTotal += routeResponse.getDuration();

                LocalDateTime finTramo = tramo.getFechaHoraInicioEstimado().plusSeconds(segundos);

                tramo.setFechaHoraFinEstimado(finTramo);

                tramo.setIdOrigen(origen.getIdUbicacion());

                tramo.setIdDestino(destino.getIdUbicacion());

                if(origen.getDeposito() == null && destino.getDeposito() == null){
                    tramo.setTipoTramo(TipoTramo.ORIGEN_DESTINO);
                }
                else if (origen.getDeposito() == null){
                    tramo.setTipoTramo(TipoTramo.ORIGEN_DEPOSITO);
                    depositos.add(destino.getDeposito().getIdDeposito());
                    costoEstadiaTotal = costoEstadiaTotal.add(destino.getDeposito().getCostoEstadiaDiario());
                }
                else if (destino.getDeposito() == null){
                    tramo.setTipoTramo(TipoTramo.DEPOSITO_DESTINO);
                    depositos.add(origen.getDeposito().getIdDeposito());
                }
                else {
                    tramo.setTipoTramo(TipoTramo.DEPOSITO_DEPOSITO);
                    depositos.add(destino.getDeposito().getIdDeposito());
                    depositos.add(destino.getDeposito().getIdDeposito());
                    costoEstadiaTotal = costoEstadiaTotal.add(destino.getDeposito().getCostoEstadiaDiario());
                }

                tramo.setOrden(orden + 1);

                tramo.setRutaTraslado(ruta);

                tramo.setEstado(EstadoTramo.ESTIMADO);

                tramos.add(tramo);
            }

            if (!tramos.isEmpty()) {
                this.tramoRepository.saveAll(tramos);
            }

            ruta.setCantidadTramos(tramos.size());

            ruta.setCantidadDepositos(depositos.size());

            tarifa.setPesoMax(contenedor.getPeso());

            tarifa.setVolumenMax(contenedor.getVolumen());

            tarifa.setCostoBase(costoBasePromedio);

            tarifa.setConsumoAprox(consumoAprox);

            tarifa.setCostoEstadia(costoEstadiaTotal);

            BigDecimal costoEstiamdo = tarifa.calcularCostoEstimado(BigDecimal.valueOf(distanciaTotal), ruta.getCargosGestionFijo());

            SolicitudEditRequest requestEdit = new SolicitudEditRequest();
            requestEdit.setIdSolicitud(solicitud.getIdSolicitud());
            requestEdit.setCostoEstimado(costoEstiamdo);

            BigDecimal horas = BigDecimal.valueOf(tiempoTotal / 3600).setScale(2, RoundingMode.HALF_UP);

            log.warn(horas.toString());

            requestEdit.setTiempoEstimadoHoras(horas);
            requestEdit.setTarifa(tarifa);
            requestEdit.setIdOrigen(tramos.getFirst().getIdOrigen());
            requestEdit.setIdDestino(tramos.getLast().getIdDestino());

            this.enviosRepository.editSolicitud(requestEdit);

            RutaGetOpcionesResponse response = RutaMapperDto.toResponseGet(tarifa,horas, ruta, tramos);

            this.rutaRepository.update(ruta);

            return response;
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public void asignarSolicitud(RutaAsignarSolicitudRequest request) throws ServiceError {
        try {

            RutaTraslado ruta = this.rutaRepository.getById(request.getIdRuta());

            if(ruta == null){
                throw new ServiceError("", Errores.RUTA_NO_ENCONTRADA, 404);
            }

            SolicitudTraslado solicitud = this.enviosRepository.getSolicitudTrasladoById(request.getIdSolicitud());

            if(solicitud == null){
                throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
            }

            List<Tramo> tramos = this.tramoRepository.buscarPorRuta(ruta.getIdRuta());

            if(tramos == null || tramos.isEmpty()){
                throw new ServiceError("", Errores.RUTA_SIN_TRAMOS, 400);
            }

            /*for (Tramo tramo : tramos){
                if (!tramo.esAsignado()){
                    throw new ServiceError("", Errores.RUTA_SIN_CONDUCTORES, 400);
                }
            }*/

            ruta.setIdSolicitud(solicitud.getIdSolicitud());

            this.rutaRepository.update(ruta);
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

}
