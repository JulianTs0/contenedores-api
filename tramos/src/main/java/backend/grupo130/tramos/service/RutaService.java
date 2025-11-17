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
        log.info("Iniciando busqueda de Ruta por ID: {}", request.getIdRuta());
        try {

            RutaTraslado ruta = this.rutaRepository.getById(request.getIdRuta());

            if (ruta == null) {
                log.warn("Ruta no encontrada para ID: {}", request.getIdRuta());
                throw new ServiceError("", Errores.RUTA_NO_ENCONTRADA, 404);
            }
            log.debug("Ruta encontrada: {}", ruta.getIdRuta());

            SolicitudTraslado solicitud = null;

            if (ruta.getIdSolicitud() != null){
                log.debug("Buscando Solicitud asociada: {}", ruta.getIdSolicitud());
                solicitud = this.enviosRepository.getSolicitudTrasladoById(ruta.getIdSolicitud());
            }

            RutaGetByIdResponse response = RutaMapperDto.toResponseGet(ruta, solicitud);

            log.info("Busqueda de Ruta por ID {} completada exitosamente.", request.getIdRuta());
            return response;
        } catch (ServiceError ex) {
            log.warn("Error de servicio al buscar Ruta por ID {}: {}", request.getIdRuta(), ex.getMessage());
            throw ex;
        }
        catch (Exception ex) {
            // Requisito TPI: No perder la traza de la excepción raíz
            log.error("Error interno inesperado al buscar Ruta por ID: {}", request.getIdRuta(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public List<RutaTraslado> getAll() throws ServiceError {
        log.info("Iniciando busqueda de todas las Rutas.");
        try {

            List<RutaTraslado> rutas = this.rutaRepository.getAll();
            log.info("Busqueda de todas las Rutas completada. Encontradas: {} rutas.", rutas.size());
            return rutas;
        } catch (ServiceError ex) {
            log.warn("Error de servicio al buscar todas las Rutas: {}", ex.getMessage());
            throw ex;
        }  catch (Exception ex) {
            log.error("Error interno inesperado al buscar todas las Rutas.", ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    /**
     * @deprecated Este método parece registrar una solicitud a una ruta existente. Considerar renombrar a 'asignarSolicitudARuta'.
     * El método 'register' usualmente implica creación.
     */
    @Deprecated
    public void register(RutaRegisterRequest request) throws ServiceError {
        log.info("Iniciando registro (asignacion) de Solicitud ID {} a Ruta ID {}.", request.getIdSolicitud(), request.getIdRuta());
        try {

            SolicitudTraslado solicitud = this.enviosRepository.getSolicitudTrasladoById(request.getIdSolicitud());

            if(solicitud == null){
                log.warn("Solicitud no encontrada para ID: {}", request.getIdSolicitud());
                throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
            }

            RutaTraslado rutaTraslado = this.rutaRepository.getById(request.getIdRuta());

            if(rutaTraslado == null){
                log.warn("Ruta no encontrada para ID: {}", request.getIdRuta());
                throw new ServiceError("", Errores.RUTA_NO_ENCONTRADA, 404);
            }

            log.debug("Asignando solicitud {} a ruta {}.", solicitud.getIdSolicitud(), rutaTraslado.getIdRuta());
            rutaTraslado.setIdSolicitud(solicitud.getIdSolicitud());

            this.rutaRepository.update(rutaTraslado);
            log.info("Asignacion de Solicitud ID {} a Ruta ID {} completada.", request.getIdSolicitud(), request.getIdRuta());
        } catch (ServiceError ex) {
            log.warn("Error de servicio al asignar Solicitud a Ruta: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno inesperado al asignar Solicitud {} a Ruta {}.", request.getIdSolicitud(), request.getIdRuta(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public RutaGetOpcionesResponse getRutaTentativa(RutaGetOpcionesRequest request) throws ServiceError {
        log.info("Iniciando calculo de Ruta Tentativa para Solicitud ID: {}", request.getIdSolicitud());
        try {

            SolicitudTraslado solicitud = this.enviosRepository.getSolicitudTrasladoById(request.getIdSolicitud());
            if (solicitud == null) {
                log.warn("Solicitud no encontrada para ID: {}", request.getIdSolicitud());
                throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
            }

            Tarifa tarifa = solicitud.getTarifa();
            if (tarifa == null) {
                log.warn("La solicitud {} no tiene tarifa (es nula). Creando una nueva instancia de Tarifa en memoria.", solicitud.getIdSolicitud());
                tarifa = new Tarifa();
                // Valor de respaldo, idealmente esto debería venir de configuración
                tarifa.setValorLitro(BigDecimal.valueOf(10));
            }

            log.debug("Buscando Contenedor ID: {}", solicitud.getIdContenedor());
            Contenedor contenedor = this.contenedorRepository.getById(solicitud.getIdContenedor());

            log.debug("Calculando costo base promedio para peso {} y volumen {}.", contenedor.getPeso(), contenedor.getVolumen());
            BigDecimal costoBasePromedio = this.camionesRepository.getPromedioCostoBase(contenedor.getPeso(), contenedor.getVolumen());

            if (costoBasePromedio.equals(BigDecimal.ZERO)){
                log.warn("No se encontraron camiones compatibles para calcular costo base promedio.");
                throw new ServiceError("", Errores.CAMIONES_NO_ENCONTRADOS, 404);
            }

            log.debug("Calculando consumo promedio de combustible.");
            BigDecimal consumoAprox = this.camionesRepository.getConsumoPromedio();

            if (consumoAprox.equals(BigDecimal.ZERO)){
                log.warn("No se pudo calcular el consumo promedio (cero).");
                throw new ServiceError("", Errores.CAMIONES_NO_ENCONTRADOS, 404);
            }

            RutaTraslado ruta = new RutaTraslado();
            ruta.setCargosGestionFijo(request.getCargosGestionFijo());
            ruta.setCantidadTramos(0);
            ruta.setCantidadDepositos(0);

            this.rutaRepository.save(ruta);
            log.info("Nueva Ruta (tentativa) creada con ID: {}", ruta.getIdRuta());

            List<Tramo> tramos = new ArrayList<>();
            Set<Long> depositos = new HashSet<>();
            BigDecimal costoEstadiaTotal = BigDecimal.ZERO;
            double distanciaTotal = 0;
            double tiempoTotal = 0;

            log.debug("Iniciando procesamiento de {} ubicaciones para crear tramos.", request.getUbicaciones().size());
            for (int orden = 0; orden < request.getUbicaciones().size() - 1; orden++){

                Long idOrigen = request.getUbicaciones().get(orden);
                Ubicacion origen = this.ubicacionesRepository.getUbicacionById(idOrigen);
                if (origen == null){
                    log.warn("Ubicacion de origen no encontrada: {}", idOrigen);
                    throw new ServiceError("", Errores.TRAMOS_UBICACION_INVALIDA, 404);
                }

                Long idDestino = request.getUbicaciones().get(orden + 1);
                Ubicacion destino = this.ubicacionesRepository.getUbicacionById(idDestino);
                if (destino == null){
                    log.warn("Ubicacion de destino no encontrada: {}", idDestino);
                    throw new ServiceError("", Errores.TRAMOS_UBICACION_INVALIDA, 404);
                }

                Tramo tramo = new Tramo();

                if (orden == 0){
                    tramo.setFechaHoraInicioEstimado(request.getFechaHoraInicioEstimado());
                } else {
                    tramo.setFechaHoraInicioEstimado(tramos.getLast().getFechaHoraFinEstimado());
                }

                log.debug("Calculando ruta OSRM entre {} ({}) y {} ({}).", origen.getIdUbicacion(), idOrigen, destino.getIdUbicacion(), idDestino);
                RouteResponse routeResponse = this.osrmApiClient.calcularDistancia(origen, destino).getRoutes().get(0);

                Long segundos = Math.round(routeResponse.getDuration());
                distanciaTotal += routeResponse.getDistance();
                tiempoTotal += routeResponse.getDuration();

                LocalDateTime finTramo = tramo.getFechaHoraInicioEstimado().plusSeconds(segundos);
                tramo.setFechaHoraFinEstimado(finTramo);
                tramo.setIdOrigen(origen.getIdUbicacion());
                tramo.setIdDestino(destino.getIdUbicacion());

                // ... (Lógica de negocio de TipoTramo) ...
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
                log.debug("Tramo {} (Orden {}) creado.", tramo.getIdTramo(), tramo.getOrden());
            }

            if (!tramos.isEmpty()) {
                this.tramoRepository.saveAll(tramos);
                log.info("Guardados {} tramos para la Ruta ID: {}", tramos.size(), ruta.getIdRuta());
            }

            ruta.setCantidadTramos(tramos.size());
            ruta.setCantidadDepositos(depositos.size());

            // ... (Lógica de negocio de Tarifa) ...
            tarifa.setPesoMax(contenedor.getPeso());
            tarifa.setVolumenMax(contenedor.getVolumen());
            tarifa.setCostoBase(costoBasePromedio);
            tarifa.setConsumoAprox(consumoAprox);
            tarifa.setCostoEstadia(costoEstadiaTotal);

            BigDecimal costoEstiamdo = tarifa.calcularCostoEstimado(BigDecimal.valueOf(distanciaTotal), ruta.getCargosGestionFijo());
            log.debug("Costo estimado calculado: {}", costoEstiamdo);

            SolicitudEditRequest requestEdit = new SolicitudEditRequest();
            requestEdit.setIdSolicitud(solicitud.getIdSolicitud());
            requestEdit.setCostoEstimado(costoEstiamdo);

            BigDecimal horas = BigDecimal.valueOf(tiempoTotal / 3600).setScale(2, RoundingMode.HALF_UP);
            log.debug("Tiempo estimado calculado: {} horas.", horas);
            // El log.warn original. Lo mantengo como DEBUG ya que parece ser de diagnóstico.
            log.debug("Valor original log.warn de horas: {}", horas.toString());

            requestEdit.setTiempoEstimadoHoras(horas);
            requestEdit.setTarifa(tarifa);
            requestEdit.setIdOrigen(tramos.getFirst().getIdOrigen());
            requestEdit.setIdDestino(tramos.getLast().getIdDestino());

            log.info("Actualizando Solicitud ID: {} con datos de ruta tentativa.", solicitud.getIdSolicitud());
            this.enviosRepository.editSolicitud(requestEdit);

            RutaGetOpcionesResponse response = RutaMapperDto.toResponseGet(tarifa,horas, ruta, tramos);

            this.rutaRepository.update(ruta);
            log.info("Ruta Tentativa para Solicitud ID {} calculada exitosamente. Ruta ID: {}", request.getIdSolicitud(), ruta.getIdRuta());

            return response;
        } catch (ServiceError ex) {
            log.warn("Error de servicio al calcular Ruta Tentativa para Solicitud ID {}: {}", request.getIdSolicitud(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno inesperado al calcular Ruta Tentativa para Solicitud ID: {}", request.getIdSolicitud(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

    public void asignarSolicitud(RutaAsignarSolicitudRequest request) throws ServiceError {
        log.info("Iniciando asignacion final de Solicitud ID {} a Ruta ID {}.", request.getIdSolicitud(), request.getIdRuta());
        try {

            RutaTraslado ruta = this.rutaRepository.getById(request.getIdRuta());

            if(ruta == null){
                log.warn("Ruta no encontrada para ID: {}", request.getIdRuta());
                throw new ServiceError("", Errores.RUTA_NO_ENCONTRADA, 404);
            }

            SolicitudTraslado solicitud = this.enviosRepository.getSolicitudTrasladoById(request.getIdSolicitud());

            if(solicitud == null){
                log.warn("Solicitud no encontrada para ID: {}", request.getIdSolicitud());
                throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
            }

            List<Tramo> tramos = this.tramoRepository.buscarPorRuta(ruta.getIdRuta());

            if(tramos == null || tramos.isEmpty()){
                log.warn("La Ruta ID {} no tiene tramos. No se puede asignar.", ruta.getIdRuta());
                throw new ServiceError("", Errores.RUTA_SIN_TRAMOS, 400);
            }

            /* // Lógica de negocio comentada, la respeto.
            for (Tramo tramo : tramos){
                if (!tramo.esAsignado()){
                    throw new ServiceError("", Errores.RUTA_SIN_CONDUCTORES, 400);
                }
            }*/

            ruta.setIdSolicitud(solicitud.getIdSolicitud());

            this.rutaRepository.update(ruta);
            log.info("Asignacion final de Solicitud ID {} a Ruta ID {} completada.", request.getIdSolicitud(), request.getIdRuta());
        } catch (ServiceError ex) {
            log.warn("Error de servicio al asignar Solicitud {} a Ruta {}: {}", request.getIdSolicitud(), request.getIdRuta(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno inesperado al asignar Solicitud {} a Ruta {}.", request.getIdSolicitud(), request.getIdRuta(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO , 500);
        }
    }

}
