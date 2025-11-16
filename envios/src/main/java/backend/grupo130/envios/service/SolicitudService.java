package backend.grupo130.envios.service;

import backend.grupo130.envios.client.contenedores.models.Contenedor;
import backend.grupo130.envios.config.enums.Errores;
import backend.grupo130.envios.config.enums.Estado;
import backend.grupo130.envios.config.exceptions.ServiceError;
import backend.grupo130.envios.data.models.SeguimientoEnvio;
import backend.grupo130.envios.data.models.SolicitudTraslado;
import backend.grupo130.envios.data.models.Tarifa;
import backend.grupo130.envios.dto.solicitud.SolicitudMapperDto;
import backend.grupo130.envios.dto.solicitud.request.SolicitudCambioDeEstadoRequest;
import backend.grupo130.envios.dto.solicitud.request.SolicitudEditRequest;
import backend.grupo130.envios.dto.solicitud.request.SolicitudGetByIdRequest;
import backend.grupo130.envios.dto.solicitud.request.SolicitudRegisterRequest;
import backend.grupo130.envios.dto.solicitud.response.SolicitudCambioDeEstadoResponse;
import backend.grupo130.envios.dto.solicitud.response.SolicitudEditResponse;
import backend.grupo130.envios.dto.solicitud.response.SolicitudGetAllResponse;
import backend.grupo130.envios.dto.solicitud.response.SolicitudGetByIdResponse;
import backend.grupo130.envios.repository.ContenedorRepository;
import backend.grupo130.envios.repository.SolicitudTrasladoRepository;
import backend.grupo130.envios.repository.TarifaRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class SolicitudService {

    private final SolicitudTrasladoRepository solicitudRepository;

    private final ContenedorRepository contenedorRepository; // Inyectamos el repo de Contenedor

    private final TarifaRepository tarifaRepository;


    public SolicitudGetByIdResponse getById(SolicitudGetByIdRequest request) throws ServiceError {
        try {
            log.info("Buscando solicitud con ID: {}", request.getIdSolicitud());
            SolicitudTraslado solicitud = this.solicitudRepository.getById(request.getIdSolicitud());

            if (solicitud == null) {
                log.warn("Solicitud no encontrada con ID: {}", request.getIdSolicitud());
                throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
            }

            // --- ADAPTACIÓN MANUAL ---
            // En lugar de usar el Mapper, construimos la respuesta manualmente
            // con los campos de la entidad "solicitud" que acabamos de obtener.
            return new SolicitudGetByIdResponse(
                solicitud.getIdSolicitud(),
                solicitud.getFechaInicio(),
                solicitud.getFechaFin(),
                solicitud.getEstado().toString(), // Convertimos el Enum a String
                solicitud.getTarifa(),
                solicitud.getSeguimientos(),
                solicitud.getIdContenedor(),
                solicitud.getIdCliente(),
                solicitud.getIdOrigen(),
                solicitud.getIdDestino(),
                // --- Campos agregados ---
                solicitud.getCostoEstimado(),
                solicitud.getCostoFinal(),
                solicitud.getTiempoEstimadoHoras(),
                solicitud.getTiempoRealHoras()
            );

        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno al buscar solicitud por ID {}: {}", request.getIdSolicitud(), ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO, 500);
        }
    }


    public SolicitudGetAllResponse getAll() throws ServiceError {
        try {

            List<SolicitudTraslado> solicitudes = this.solicitudRepository.getAll();

            return SolicitudMapperDto.toResponseGet(solicitudes);
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO, 500);
        }
    }

    public SolicitudGetByIdResponse register(SolicitudRegisterRequest request) throws ServiceError {
        try {

            Contenedor contenedor;
            try {
                log.info("Buscando contenedor existente para cliente {}...", request.getIdCliente());
                contenedor = this.contenedorRepository.getByPesoVolumen(
                    request.getPeso(),
                    request.getVolumen(),
                    request.getIdCliente()
                );
                this.contenedorRepository.asignarCliente(contenedor.getIdContenedor(), request.getIdCliente());
                log.info("Contenedor encontrado. ID: {}", contenedor.getIdContenedor());

            } catch (ServiceError ex) {
                if (ex.getMessage() != null && ex.getMessage().contains("[404]")) {
                    log.warn("Contenedor no encontrado (404). Creando uno nuevo...");
                    this.contenedorRepository.register(
                        request.getPeso(),
                        request.getVolumen(),
                        request.getIdCliente()
                    );
                    log.info("Volviendo a buscar el contenedor recién creado...");
                    contenedor = this.contenedorRepository.getByPesoVolumen(
                        request.getPeso(),
                        request.getVolumen(),
                        request.getIdCliente()
                    );
                    log.info("Contenedor creado y recuperado. ID: {}", contenedor.getIdContenedor());
                } else {
                    throw ex;
                }
            }

            SolicitudTraslado solicitud = new SolicitudTraslado();
            solicitud.setIdContenedor(contenedor.getIdContenedor());
            solicitud.setIdCliente(request.getIdCliente());
            solicitud.setEstado(Estado.BORRADOR);

            SeguimientoEnvio primerSeguimiento = new SeguimientoEnvio(
                null,
                LocalDateTime.now(),
                null,
                Estado.BORRADOR,
                "Solicitud creada."
            );

            List<SeguimientoEnvio> inicio = List.of(primerSeguimiento);
            solicitud.setSeguimientos(inicio);
            SolicitudTraslado solicitudGuardada = this.solicitudRepository.save(solicitud);

            return SolicitudMapperDto.toResponseGet(solicitudGuardada);

        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error al registrar solicitud: {}", ex.getMessage(), ex);
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO, 500);
        }
    }


    public SolicitudEditResponse edit(SolicitudEditRequest request) throws ServiceError {
        try {
            SolicitudTraslado solicitud = this.solicitudRepository.getById(request.getIdSolicitud());
            if (solicitud == null) {
                throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
            }

            // Solo permitimos editar si está PENDIENTE o EN_CURSO
            if (solicitud.getEstado() == Estado.ENTREGADO) {
                throw new ServiceError("No se puede editar una solicitud FINALIZADA", Errores.TRANSICION_ESTADO_INVALIDA, 400);
            }

            // Actualizamos los campos de la Solicitud
            if (request.getFechaInicio() != null) {
                solicitud.setFechaInicio(request.getFechaInicio());
            }
            if (request.getFechaFin() != null) {
                solicitud.setFechaFin(request.getFechaFin());
            }
            if (request.getTiempoEstimadoHoras() != null) {
                solicitud.setTiempoEstimadoHoras(request.getTiempoEstimadoHoras());
            }
            if (request.getTiempoRealHoras() != null) {
                solicitud.setTiempoRealHoras(request.getTiempoRealHoras());
            }

            // --- Lógica para Tarifa ---
            if (request.getTarifa() != null) {
                log.info("Procesando tarifa para solicitud {}", solicitud.getIdSolicitud());

                // 1. Obtenemos la tarifa que viene del request (de 'tramos')
                Tarifa tarifaRequest = request.getTarifa();

                // 2. Le asignamos el costo estimado, que viene por separado en el request
                if (request.getCostoEstimado() != null) {
                    tarifaRequest.setCostoEstimado(request.getCostoEstimado());
                }

                // 3. Le asignamos el costo final si también viene
                if (request.getCostoFinal() != null) {
                    tarifaRequest.setCostoFinal(request.getCostoFinal());
                }

                // 4. Verificamos si la solicitud YA tiene una tarifa (para actualizar) o no (para crear)
                Tarifa tarifaExistente = solicitud.getTarifa();
                if (tarifaExistente != null) {
                    // La solicitud ya tiene tarifa, la actualizamos
                    log.info("Actualizando tarifa existente ID: {}", tarifaExistente.getIdTarifa());
                    tarifaExistente.setPesoMax(tarifaRequest.getPesoMax());
                    tarifaExistente.setVolumenMax(tarifaRequest.getVolumenMax());
                    tarifaExistente.setCostoBase(tarifaRequest.getCostoBase());
                    tarifaExistente.setConsumoAprox(tarifaRequest.getConsumoAprox());
                    tarifaExistente.setValorLitro(tarifaRequest.getValorLitro());
                    tarifaExistente.setCostoEstadia(tarifaRequest.getCostoEstadia());
                    tarifaExistente.setCostoEstimado(tarifaRequest.getCostoEstimado());
                    tarifaExistente.setCostoFinal(tarifaRequest.getCostoFinal());

                    this.tarifaRepository.update(tarifaExistente); // Actualizamos la existente
                } else {
                    // La solicitud NO tiene tarifa, guardamos la del request como nueva
                    log.info("Guardando nueva tarifa para la solicitud");
                    Tarifa tarifaGuardada = this.tarifaRepository.save(tarifaRequest);
                    solicitud.setTarifa(tarifaGuardada); // La vinculamos a la solicitud
                }

            }

            // Si se pasan costos, los actualizamos en la tarifa de la solicitud
            // (Asumimos que la solicitud ya tiene una tarifa)
            /*if (solicitud.getTarifa() != null) {
                if (request.getCostoEstimado() != null) {
                    solicitud.getTarifa().setCostoEstimado(request.getCostoEstimado());
                }
                if (request.getCostoFinal() != null) {
                    solicitud.getTarifa().setCostoFinal(request.getCostoFinal());
                }
                // Guardamos la tarifa (aunque JPA lo haría por cascada si está configurado)
                tarifaRepository.update(solicitud.getTarifa());
            } else if (request.getCostoEstimado() != null || request.getCostoFinal() != null) {
                // No se puede asignar costos si no hay tarifa
                throw new ServiceError("No se puede asignar costos si la solicitud no tiene una tarifa vinculada", Errores.TARIFA_NO_ENCONTRADA, 400);
            }*/

            SolicitudTraslado solicitudActualizada = this.solicitudRepository.update(solicitud);
            return SolicitudMapperDto.toResponsePatch(solicitudActualizada);

        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO, 500);
        }
    }

    // CAMBIO DE ESTADO (Sin cambios)
    public SolicitudCambioDeEstadoResponse cambioDeEstado(SolicitudCambioDeEstadoRequest request) throws ServiceError {
        try {

            SolicitudTraslado solicitud = this.solicitudRepository.getById(request.getIdSolicitud());

            if (solicitud == null) {
                throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
            }

            Estado nuevoEstado = Estado.fromString(request.getNuevoEstado());
            if (nuevoEstado == null) {
                throw new ServiceError("", Errores.ESTADO_INVALIDO, 400);
            }

            switch (solicitud.getEstado()) {
                case BORRADOR:
                    if (nuevoEstado != Estado.PROGRAMADO) {
                        throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);
                    }
                    break;
                case PROGRAMADO:
                    if (nuevoEstado != Estado.EN_TRANSITO) {
                        throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);
                    }
                    break;
                case EN_TRANSITO:
                    if (nuevoEstado != Estado.ENTREGADO) {
                        throw new ServiceError("", Errores.TRANSICION_ESTADO_INVALIDA, 400);
                    }
                    break;
                case ENTREGADO:
                    throw new ServiceError("", Errores.SOLICITUD_YA_FINALIZADA, 400);
            }

            if(nuevoEstado.equals(Estado.EN_TRANSITO)){
                solicitud.setFechaInicio(LocalDateTime.now());
            }
            if(nuevoEstado.equals(Estado.ENTREGADO)){
                solicitud.setFechaFin(LocalDateTime.now());
            }

            solicitud.setEstado(nuevoEstado);

            SeguimientoEnvio nuevoSeguimiento = new SeguimientoEnvio(
                null,
                LocalDateTime.now(),
                (nuevoEstado == Estado.ENTREGADO) ? LocalDateTime.now() : null,
                nuevoEstado,
                request.getDescripcion()
            );

            solicitud.getSeguimientos().getLast().setFechaHoraFin(LocalDateTime.now());
            solicitud.getSeguimientos().add(nuevoSeguimiento);

            SolicitudTraslado solicitudActualizada = this.solicitudRepository.update(solicitud);

            SeguimientoEnvio seguimientoSolicitudActualizada = solicitudActualizada.getSeguimientos().get(solicitudActualizada.getSeguimientos().size() - 1);

            return SolicitudMapperDto.toResponsePatch(solicitudActualizada, seguimientoSolicitudActualizada);

        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError(ex.getMessage(), Errores.ERROR_INTERNO, 500);
        }
    }
}
