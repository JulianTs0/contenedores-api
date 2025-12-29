package backend.grupo130.envios.service;

import backend.grupo130.envios.config.enums.Errores;
import backend.grupo130.envios.config.enums.EstadoSolicitud;
import backend.grupo130.envios.config.exceptions.ServiceError;
import backend.grupo130.envios.data.entity.SeguimientoEnvio;
import backend.grupo130.envios.data.entity.SolicitudTraslado;
import backend.grupo130.envios.dto.seguimiento.SeguimientoMapperDto;
import backend.grupo130.envios.dto.seguimiento.request.SeguimientoGetByIdRequest;
import backend.grupo130.envios.dto.seguimiento.request.SeguimientoRegisterRequest;
import backend.grupo130.envios.dto.seguimiento.response.SeguimientoGetAllResponse;
import backend.grupo130.envios.dto.seguimiento.response.SeguimientoGetByIdResponse;
import backend.grupo130.envios.repository.SeguimientoEnvioRepository;
import backend.grupo130.envios.repository.SolicitudTrasladoRepository;
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
public class SeguimientoService {

    private final SeguimientoEnvioRepository seguimientoRepository;

    private final SolicitudTrasladoRepository solicitudRepository;


    public SeguimientoGetByIdResponse getById(SeguimientoGetByIdRequest request) throws ServiceError {

        SeguimientoEnvio seguimiento = this.seguimientoRepository.getById(request.getIdSeguimiento());

        if (seguimiento == null) {
            throw new ServiceError("", Errores.SEGUIMIENTO_NO_ENCONTRADO, 404);
        }

        log.info("Seguimiento encontrado exitosamente. ID: {}", request.getIdSeguimiento());

        SeguimientoGetByIdResponse response = SeguimientoMapperDto.toSeguimientoGetByIdResponse(seguimiento);
        return response;
    }

    public SeguimientoGetAllResponse getAll() throws ServiceError {
        log.info("Inicio getAll. Buscando todos los seguimientos.");

        List<SeguimientoEnvio> seguimientos = this.seguimientoRepository.getAll();
        log.info("Se encontraron {} seguimientos.", seguimientos.size());

        SeguimientoGetAllResponse response = SeguimientoMapperDto.toSeguimientoGetAllResponse(seguimientos);
        return response;
    }

    public void register(SeguimientoRegisterRequest request) throws ServiceError {
        log.info("Inicio register. Registrando nuevo seguimiento para Solicitud ID: {}", request.getIdSolicitud());

        SolicitudTraslado solicitud = solicitudRepository.getById(request.getIdSolicitud());

        if (solicitud == null) {
            throw new ServiceError("", Errores.SOLICITUD_NO_ENCONTRADA, 404);
        }

        EstadoSolicitud estado = EstadoSolicitud.fromString(request.getEstado());
        if (estado == null) {
            throw new ServiceError("", Errores.ESTADO_INVALIDO, 400);
        }

        SeguimientoEnvio seguimiento = new SeguimientoEnvio();
        seguimiento.setDescripcion(request.getDescripcion());
        seguimiento.setFechaHoraInicio(LocalDateTime.now());
        seguimiento.setEstado(estado);
        solicitud.getSeguimientos().add(seguimiento);

        this.solicitudRepository.update(solicitud);
        log.info("Seguimiento registrado exitosamente para Solicitud ID: {}", request.getIdSolicitud());
    }

}
