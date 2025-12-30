package backend.grupo130.tramos.service;

import backend.grupo130.tramos.client.camiones.CamionClient;
import backend.grupo130.tramos.client.contenedores.ContenedorClient;
import backend.grupo130.tramos.client.contenedores.entity.Contenedor;
import backend.grupo130.tramos.client.envios.EnviosClient;
import backend.grupo130.tramos.client.envios.entity.SolicitudTraslado;
import backend.grupo130.tramos.client.envios.entity.Tarifa;
import backend.grupo130.tramos.client.envios.request.SolicitudEditRequest;
import backend.grupo130.tramos.client.ubicaciones.UbicacionesClient;
import backend.grupo130.tramos.client.ubicaciones.entity.Ubicacion;
import backend.grupo130.tramos.config.enums.Errores;
import backend.grupo130.tramos.config.enums.EstadoTramo;
import backend.grupo130.tramos.config.enums.PreciosNegocio;
import backend.grupo130.tramos.config.enums.TipoTramo;
import backend.grupo130.tramos.config.exceptions.ServiceError;
import backend.grupo130.tramos.data.entity.RutaTraslado;
import backend.grupo130.tramos.data.entity.Tramo;
import backend.grupo130.tramos.dto.ruta.RutaMapperDto;
import backend.grupo130.tramos.dto.ruta.request.RutaCrearTentativaRequest;
import backend.grupo130.tramos.dto.ruta.request.RutaGetByIdRequest;
import backend.grupo130.tramos.dto.ruta.response.RutaGetAllResponse;
import backend.grupo130.tramos.dto.ruta.response.RutaGetByIdResponse;
import backend.grupo130.tramos.dto.ruta.response.RutaGetOpcionesResponse;
import backend.grupo130.tramos.external.OSRM.OsrmApiClient;
import backend.grupo130.tramos.external.OSRM.response.RouteResponse;
import backend.grupo130.tramos.repository.RutaRepository;
import backend.grupo130.tramos.repository.TramoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class RutaService {

    private final RutaRepository rutaRepository;

    private final TramoRepository tramoRepository;

    private final EnviosClient enviosClient;

    private final UbicacionesClient ubicacionesClient;

    private final OsrmApiClient osrmApiClient;

    private final ContenedorClient contenedorClient;

    private final CamionClient camionClient;

    public RutaGetByIdResponse getById(RutaGetByIdRequest request) throws ServiceError {

        RutaTraslado ruta = this.rutaRepository.getById(request.getIdRuta());

        if (ruta == null) {
            throw new ServiceError("", Errores.RUTA_NO_ENCONTRADA, 404);
        }

        log.debug("Ruta encontrada: {}", ruta.getIdRuta());

        SolicitudTraslado solicitud = null;

        if (ruta.getIdSolicitud() != null){
            log.debug("Buscando Solicitud asociada: {}", ruta.getIdSolicitud());
            solicitud = this.enviosClient.getSolicitudTrasladoById(ruta.getIdSolicitud());
        }

        RutaGetByIdResponse response = RutaMapperDto.toResponseGetById(ruta, solicitud);

        log.info("Busqueda de Ruta por ID {} completada exitosamente.", request.getIdRuta());
        return response;
    }

    public RutaGetAllResponse getAll() throws ServiceError {
        log.info("Iniciando busqueda de todas las Rutas.");

        List<RutaTraslado> rutas = this.rutaRepository.getAll();

        RutaGetAllResponse response = RutaMapperDto.toResponseGetAll(rutas);

        log.info("Busqueda de todas las Rutas completada. Encontradas: {} rutas.", rutas.size());
        return response;
    }

    // TODO: Revisar el calculo de la tarifa estimada
    // TODO: Permitir varias rutas alternativas entre nodos

    public RutaGetOpcionesResponse getRutaTentativa(RutaCrearTentativaRequest request) throws ServiceError {
        log.info("Iniciando calculo de Ruta Tentativa para Solicitud ID: {}", request.getIdSolicitud());

        SolicitudTraslado solicitud = this.enviosClient.getSolicitudTrasladoById(request.getIdSolicitud());

        if (solicitud == null) {
            throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
        }
        if (!solicitud.esBorrador()) {
            throw new ServiceError("", Errores.SOLICITUD_YA_CONFIRMADA, 404);
        }

        log.debug("Buscando Contenedor ID: {}", solicitud.getIdContenedor());
        Contenedor contenedor = this.contenedorClient.getById(solicitud.getIdContenedor());

        log.debug("Calculando costo base promedio para peso {} y volumen {}.", contenedor.getPeso(), contenedor.getVolumen());
        BigDecimal costoBasePromedio = this.camionClient.getPromedioCostoBase(contenedor.getPeso(), contenedor.getVolumen());

        if (costoBasePromedio.equals(BigDecimal.ZERO)){
            throw new ServiceError("", Errores.CAMIONES_NO_ENCONTRADOS, 404);
        }

        log.debug("Calculando consumo promedio de combustible.");
        BigDecimal consumoAprox = this.camionClient.getConsumoPromedio();

        if (consumoAprox.equals(BigDecimal.ZERO)){
            throw new ServiceError("", Errores.CAMIONES_NO_ENCONTRADOS, 404);
        }

        RutaTraslado ruta = this.rutaRepository.getBySolicitud(request.getIdSolicitud());

        if(ruta == null){
            ruta = new RutaTraslado();

            ruta.setCantidadTramos(0);
            ruta.setCantidadDepositos(0);
            ruta.setCargosGestionFijo(PreciosNegocio.CARGO_GESTION.getValor());

            ruta = this.rutaRepository.save(ruta);
        } else {
            this.tramoRepository.deleteByRutaId(ruta.getIdRuta());
        }

        log.info("Nueva Ruta (tentativa) creada con ID: {}", ruta.getIdRuta());

        List<Tramo> tramos = new ArrayList<>();
        List<Ubicacion> ubicaciones = this.ubicacionesClient.getByListIds(request.getUbicaciones());

        BigDecimal costoEstadiaTotal = BigDecimal.ZERO;
        int depositos = 0;
        double distanciaTotal = 0;
        double tiempoTotal = 0;

        log.debug("Iniciando procesamiento de {} ubicaciones para crear tramoModels.", request.getUbicaciones().size());
        for (int orden = 0; orden < ubicaciones.size() - 1; orden++){

            Ubicacion origen = ubicaciones.get(orden);
            if (origen == null){
                throw new ServiceError("", Errores.TRAMOS_UBICACION_INVALIDA, 404);
            }

            Ubicacion destino = ubicaciones.get(orden + 1);
            if (destino == null){
                throw new ServiceError("", Errores.TRAMOS_UBICACION_INVALIDA, 404);
            }

            Tramo tramo = new Tramo();

            if (orden == 0){
                tramo.setFechaHoraInicioEstimado(request.getFechaHoraInicioEstimado());
            } else {
                tramo.setFechaHoraInicioEstimado(tramos.getLast().getFechaHoraFinEstimado());
            }

            log.debug("Calculando ruta OSRM entre {} ({}) y {} ({}).", origen.getIdUbicacion(), origen.getIdUbicacion(), destino.getIdUbicacion(), destino.getIdUbicacion());
            RouteResponse routeResponse = this.osrmApiClient.calcularDistancia(origen, destino).getRoutes().getFirst();


            double distancia = Math.round(routeResponse.getDistance() / 1000);
            long segundos = Math.round(routeResponse.getDuration());

            distanciaTotal += distancia;
            tiempoTotal += routeResponse.getDuration();

            LocalDateTime finTramo = tramo.getFechaHoraInicioEstimado().plusSeconds(segundos);
            tramo.setFechaHoraFinEstimado(finTramo);
            tramo.setIdOrigen(origen.getIdUbicacion());
            tramo.setIdDestino(destino.getIdUbicacion());
            tramo.setDistancia(distancia);

            if(origen.getDeposito() == null && destino.getDeposito() == null){
                tramo.setTipoTramo(TipoTramo.ORIGEN_DESTINO);
            }
            else if (origen.getDeposito() == null){
                tramo.setTipoTramo(TipoTramo.ORIGEN_DEPOSITO);
                depositos++;
                costoEstadiaTotal = costoEstadiaTotal.add(destino.getDeposito().getCostoEstadiaDiario());
            }
            else if (destino.getDeposito() == null){
                tramo.setTipoTramo(TipoTramo.DEPOSITO_DESTINO);
            }
            else {
                tramo.setTipoTramo(TipoTramo.DEPOSITO_DEPOSITO);
                depositos++;
                costoEstadiaTotal = costoEstadiaTotal.add(destino.getDeposito().getCostoEstadiaDiario());
            }

            tramo.setOrden(orden + 1);
            tramo.setRutaTraslado(ruta);
            tramo.setEstado(EstadoTramo.ESTIMADO);

            tramos.add(tramo);
            log.debug("TramoModel {} (Orden {}) creado.", tramo.getIdTramo(), tramo.getOrden());
        }

        if (!tramos.isEmpty()) {
            this.tramoRepository.saveAll(tramos);
            log.info("Guardados {} tramoModels para la Ruta ID: {}", tramos.size(), ruta.getIdRuta());
        }

        ruta.setCantidadTramos(tramos.size());
        ruta.setCantidadDepositos(depositos);
        ruta.setIdSolicitud(solicitud.getIdSolicitud());

        Tarifa tarifa = solicitud.getTarifa();
        if (tarifa == null) {
            tarifa = new Tarifa();
        }

        tarifa.setPesoMax(contenedor.getPeso());
        tarifa.setVolumenMax(contenedor.getVolumen());
        tarifa.setCostoBase(costoBasePromedio.setScale(2,RoundingMode.HALF_UP));
        tarifa.setConsumoAprox(consumoAprox);
        tarifa.setCostoEstadia(costoEstadiaTotal);

        BigDecimal distanciaEnKm = BigDecimal.valueOf(distanciaTotal);

        BigDecimal costoEstiamdo = tarifa.calcularCostoEstimado(distanciaEnKm, ruta.getCargosGestionFijo(), tramos.size(), 1);
        log.debug("Costo estimado calculado: {}", costoEstiamdo);

        SolicitudEditRequest requestEdit = new SolicitudEditRequest();
        requestEdit.setCostoEstimado(costoEstiamdo);

        BigDecimal horas = BigDecimal.valueOf(tiempoTotal / 3600).setScale(2, RoundingMode.HALF_UP);
        log.debug("Tiempo estimado calculado: {} horas.", horas);

        log.debug("Valor original log.warn de horas: {}", horas);

        requestEdit.setTiempoEstimadoHoras(horas);
        requestEdit.setTarifa(tarifa);
        requestEdit.setIdOrigen(tramos.getFirst().getIdOrigen());
        requestEdit.setIdDestino(tramos.getLast().getIdDestino());

        log.info("Actualizando Solicitud ID: {} con datos de ruta tentativa.", solicitud.getIdSolicitud());
        this.enviosClient.editSolicitud(solicitud.getIdSolicitud(), requestEdit);

        RutaGetOpcionesResponse response = RutaMapperDto.toResponseGetOpciones(tarifa,horas, ruta, tramos);

        this.rutaRepository.update(ruta);
        log.info("Ruta Tentativa para Solicitud ID {} calculada exitosamente. Ruta ID: {}", request.getIdSolicitud(), ruta.getIdRuta());

        return response;
    }

}
