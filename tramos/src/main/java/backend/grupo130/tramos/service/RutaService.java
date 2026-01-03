package backend.grupo130.tramos.service;

import backend.grupo130.tramos.client.camiones.CamionClient;
import backend.grupo130.tramos.client.contenedores.ContenedorClient;
import backend.grupo130.tramos.client.contenedores.entity.Contenedor;
import backend.grupo130.tramos.client.envios.EnviosClient;
import backend.grupo130.tramos.client.envios.entity.PreciosNegocio;
import backend.grupo130.tramos.client.envios.entity.SolicitudTraslado;
import backend.grupo130.tramos.client.envios.entity.Tarifa;
import backend.grupo130.tramos.client.envios.request.SolicitudEditRequest;
import backend.grupo130.tramos.client.ubicaciones.UbicacionesClient;
import backend.grupo130.tramos.client.ubicaciones.entity.Ubicacion;
import backend.grupo130.tramos.config.enums.Errores;
import backend.grupo130.tramos.config.enums.EstadoTramo;
import backend.grupo130.tramos.config.enums.TipoTramo;
import backend.grupo130.tramos.config.exceptions.ServiceError;
import backend.grupo130.tramos.data.entity.RutaTraslado;
import backend.grupo130.tramos.data.entity.Tramo;
import backend.grupo130.tramos.dto.osrm.response.RouteResponse;
import backend.grupo130.tramos.dto.ruta.RutaMapperDto;
import backend.grupo130.tramos.dto.ruta.request.RutaCrearTentativaRequest;
import backend.grupo130.tramos.dto.ruta.request.RutaGetByIdRequest;
import backend.grupo130.tramos.dto.ruta.response.RutaGetAllResponse;
import backend.grupo130.tramos.dto.ruta.response.RutaGetByIdResponse;
import backend.grupo130.tramos.dto.ruta.response.RutaGetOpcionesResponse;
import backend.grupo130.tramos.external.OsrmApiClient;
import backend.grupo130.tramos.dto.osrm.OsrmMapperDto;
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
import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.kv;

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

        log.debug("Ruta encontrada", 
            kv("ruta_id", ruta.getIdRuta())
        );

        SolicitudTraslado solicitud = null;

        if (ruta.getSolicitud() != null && ruta.getSolicitud().getIdSolicitud() != null){
            log.debug("Buscando Solicitud asociada", 
                kv("solicitud_id", ruta.getSolicitud().getIdSolicitud())
            );
            solicitud = this.enviosClient.getSolicitudTrasladoById(ruta.getSolicitud().getIdSolicitud());
        }

        ruta.setSolicitud(solicitud);

        RutaGetByIdResponse response = RutaMapperDto.toResponseGetById(ruta);

        log.info("Busqueda de Ruta completada exitosamente", 
            kv("ruta_id", request.getIdRuta())
        );
        return response;
    }

    public RutaGetAllResponse getAll() throws ServiceError {
        log.info("Iniciando busqueda de todas las Rutas");

        List<RutaTraslado> rutas = this.rutaRepository.getAll();

        RutaGetAllResponse response = RutaMapperDto.toResponseGetAll(rutas);

        log.info("Busqueda de todas las Rutas completada", 
            kv("cantidad_rutas", rutas.size())
        );
        return response;
    }


    public RutaGetOpcionesResponse getRutaTentativa(RutaCrearTentativaRequest request) throws ServiceError {
        log.info("Iniciando calculo de Ruta Tentativa", 
            kv("solicitud_id", request.getIdSolicitud())
        );

        SolicitudTraslado solicitud = this.enviosClient.getSolicitudTrasladoById(request.getIdSolicitud());

        if (solicitud == null) {
            throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
        }
        if (!solicitud.esBorrador()) {
            throw new ServiceError("", Errores.SOLICITUD_YA_CONFIRMADA, 404);
        }

        log.debug("Buscando Contenedor", 
            kv("contenedor_id", solicitud.getIdContenedor())
        );
        Contenedor contenedor = this.contenedorClient.getById(solicitud.getIdContenedor());

        RutaTraslado ruta = this.rutaRepository.getBySolicitud(request.getIdSolicitud());
        PreciosNegocio precioActual = this.enviosClient.getUltimosPrecios();

        if(ruta == null){
            ruta = new RutaTraslado();

            ruta.setCantidadTramos(0);
            ruta.setCantidadDepositos(0);
            ruta.setCargosGestionFijo(precioActual.getCargoGestion());
            ruta.setSolicitud(solicitud);

            ruta = this.rutaRepository.save(ruta);
        } else {
            this.tramoRepository.deleteByRutaId(ruta.getIdRuta());
        }

        log.info("Nueva Ruta (tentativa) creada", 
            kv("ruta_id", ruta.getIdRuta())
        );

        List<Ubicacion> ubicaciones = this.ubicacionesClient.getByListIds(request.getUbicaciones());
        if (ubicaciones.size() != request.getUbicaciones().size()){
            throw new ServiceError("", Errores.TRAMOS_UBICACION_INVALIDA, 404);
        }

        List<Tramo> tramos = new ArrayList<>();
        BigDecimal costoEstadiaTotal = BigDecimal.ZERO;
        int depositos = 0;
        double distanciaTotal = 0;
        double tiempoTotal = 0;

        log.debug("Iniciando procesamiento de ubicaciones para crear tramoModels", 
            kv("cantidad_ubicaciones", request.getUbicaciones().size())
        );
        for (int orden = 0; orden < ubicaciones.size() - 1; orden++){

            Ubicacion origen = ubicaciones.get(orden);
            Ubicacion destino = ubicaciones.get(orden + 1);

            Tramo tramo = new Tramo();

            if (orden == 0){
                tramo.setFechaHoraInicioEstimado(request.getFechaHoraInicioEstimado());
            } else {
                tramo.setFechaHoraInicioEstimado(tramos.getLast().getFechaHoraFinEstimado());
            }

            log.debug("Calculando ruta OSRM", 
                kv("origen_id", origen.getIdUbicacion()), 
                kv("destino_id", destino.getIdUbicacion())
            );
            RouteResponse routeResponse = this.osrmApiClient.calcularDistancia(
                OsrmMapperDto.toRequest(origen, destino, request.getAlternativa())
            ).getRuta();

            double distancia = routeResponse.getDistance() / 1000.0;

            distanciaTotal += distancia;
            tiempoTotal += routeResponse.getDuration();

            LocalDateTime finTramo = tramo.getFechaHoraInicioEstimado()
                .plusSeconds(Math.round(routeResponse.getDuration()));

            tramo.setFechaHoraFinEstimado(finTramo);
            tramo.setOrigen(origen);
            tramo.setDestino(destino);
            tramo.setDistancia(distancia);
            tramo.setOrden(orden + 1);
            tramo.setRutaTraslado(ruta);
            tramo.setEstado(EstadoTramo.ESTIMADO);
            tramo.resolverTipo();

            if (
                tramo.getTipoTramo() == TipoTramo.ORIGEN_DEPOSITO ||
                tramo.getTipoTramo() == TipoTramo.DEPOSITO_DEPOSITO
            ){
                depositos++;
                costoEstadiaTotal = costoEstadiaTotal.add(destino.getDeposito().getCostoEstadiaDiario());
            }

            tramos.add(tramo);
            log.debug("Tramo creado", 
                kv("tramo_id", tramo.getIdTramo()), 
                kv("orden", tramo.getOrden())
            );
        }

        if (!tramos.isEmpty()) {
            tramos = this.tramoRepository.saveAll(tramos);
            log.info("Guardados tramoModels para la Ruta", 
                kv("cantidad_tramos", tramos.size()), 
                kv("ruta_id", ruta.getIdRuta())
            );
        }

        ruta.setCantidadTramos(tramos.size());
        ruta.setCantidadDepositos(depositos);
        ruta.setSolicitud(solicitud);

        Tarifa tarifa = solicitud.getTarifa();
        if (tarifa == null) {
            tarifa = new Tarifa();
        }

        log.debug("Calculando costo base promedio", 
            kv("peso", contenedor.getPeso()), 
            kv("volumen", contenedor.getVolumen())
        );
        BigDecimal costoBasePromedio = this.camionClient.getPromedioCostoBase(contenedor.getPeso(), contenedor.getVolumen());

        if (costoBasePromedio.compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceError("", Errores.CAMIONES_NO_ENCONTRADOS, 404);
        }

        log.debug("Calculando consumo promedio de combustible");
        BigDecimal consumoAprox = this.camionClient.getConsumoPromedio();

        if (consumoAprox.compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceError("", Errores.CAMIONES_NO_ENCONTRADOS, 404);
        }

        tarifa.setValorLitro(precioActual.getValorLitro());
        tarifa.setPesoMax(contenedor.getPeso());
        tarifa.setVolumenMax(contenedor.getVolumen());
        tarifa.setCostoBase(costoBasePromedio.setScale(2,RoundingMode.HALF_UP));
        tarifa.setConsumoAprox(consumoAprox);
        tarifa.setCostoEstadia(costoEstadiaTotal);

        BigDecimal distanciaEnKm = BigDecimal.valueOf(distanciaTotal).setScale(2, RoundingMode.HALF_UP);

        tarifa.calcularCostoEstimado(distanciaEnKm, ruta.getCargosGestionFijo(), tramos.size(), 1, precioActual);
        log.debug("Costo estimado calculado", 
            kv("costo_estimado", tarifa.getCostoEstimado())
        );

        BigDecimal horas = BigDecimal.valueOf(tiempoTotal / 3600).setScale(2, RoundingMode.HALF_UP);
        log.debug("Tiempo estimado calculado", 
            kv("horas", horas)
        );

        SolicitudEditRequest requestEdit = SolicitudEditRequest.builder()
            .tiempoEstimadoHoras(horas)
            .tarifa(tarifa)
            .idOrigen(tramos.getFirst().getOrigen().getIdUbicacion())
            .idDestino(tramos.getLast().getDestino().getIdUbicacion())
            .build();

        log.info("Actualizando Solicitud con datos de ruta tentativa", 
            kv("solicitud_id", solicitud.getIdSolicitud())
        );
        this.enviosClient.editSolicitud(solicitud.getIdSolicitud(), requestEdit);

        this.rutaRepository.update(ruta);
        log.info("Ruta Tentativa calculada exitosamente", 
            kv("solicitud_id", request.getIdSolicitud()), 
            kv("ruta_id", ruta.getIdRuta())
        );

        return RutaMapperDto.toResponseGetOpciones(tarifa,horas, ruta, tramos);
    }

}
