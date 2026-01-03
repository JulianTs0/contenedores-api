package backend.grupo130.ubicaciones.service;

import backend.grupo130.ubicaciones.data.entity.Ubicacion;
import backend.grupo130.ubicaciones.data.models.UbicacionModel;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionGetListByIdRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.*;
import backend.grupo130.ubicaciones.repository.UbicacionRepository;
import backend.grupo130.ubicaciones.config.enums.Errores;
import backend.grupo130.ubicaciones.config.exceptions.ServiceError;
import backend.grupo130.ubicaciones.dto.ubicaciones.UbicacionesMapperDto;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionEditRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionGetByIdRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionRegisterRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class UbicacionService {

    private final UbicacionRepository ubicacionRepository;

    public UbicacionGetByIdResponse getById(UbicacionGetByIdRequest request) throws ServiceError {
        log.info("Iniciando getById para Ubicacion",
                kv("evento", "get_by_id_ubicacion"),
                kv("id_ubicacion", request.getIdUbicacion()));

        Ubicacion ubicacion = this.ubicacionRepository.getById(request.getIdUbicacion());

        if (ubicacion == null) {
            throw new ServiceError("", Errores.UBICACION_NO_ENCONTRADA, 404);
        }

        log.debug("Ubicacion encontrada",
                kv("evento", "ubicacion_encontrada"),
                kv("id_ubicacion", ubicacion.getIdUbicacion()));

        UbicacionGetByIdResponse response = UbicacionesMapperDto.toResponseGetById(ubicacion);

        log.info("Finalizado getById para Ubicacion",
                kv("evento", "get_by_id_ubicacion_finalizado"),
                kv("id_ubicacion", request.getIdUbicacion()));
        return response;
    }

    public UbicacionGetAllResponse getAll() throws ServiceError {
        log.info("Iniciando getAll para Ubicaciones",
                kv("evento", "get_all_ubicaciones"));

        List<Ubicacion> ubicaciones = this.ubicacionRepository.getAll();
        log.debug("Se encontraron ubicaciones",
                kv("evento", "ubicaciones_encontradas"),
                kv("cantidad", ubicaciones.size()));

        UbicacionGetAllResponse response = UbicacionesMapperDto.toResponseGetAll(ubicaciones);

        log.info("Finalizado getAll para Ubicaciones",
                kv("evento", "get_all_ubicaciones_finalizado"),
                kv("cantidad", ubicaciones.size()));
        return response;
    }

    public UbicacionGetListByIdResponse getByListIds(UbicacionGetListByIdRequest request) throws ServiceError {
        log.info("Iniciando busqueda para Ubicaciones por lista de IDs",
                kv("evento", "get_by_list_ids_ubicaciones"),
                kv("ids", request.getIds()));

        List<Ubicacion> ubicaciones = this.ubicacionRepository.getByListIds(request.getIds());
        log.debug("Se encontraron ubicaciones",
                kv("evento", "ubicaciones_encontradas_por_lista"),
                kv("cantidad", ubicaciones.size()));

        Map<Long, Ubicacion> mapaUbicaciones = ubicaciones.stream()
            .collect(
                Collectors.toMap(
                    Ubicacion::getIdUbicacion,
                    u -> u)
            );

        ubicaciones = request.getIds().stream()
            .filter(mapaUbicaciones::containsKey)
            .map(mapaUbicaciones::get)
            .collect(Collectors.toList());

        UbicacionGetListByIdResponse response = UbicacionesMapperDto.toResponseGetList(ubicaciones);

        log.info("Finalizado busqueda para Ubicaciones por lista de IDs",
                kv("evento", "get_by_list_ids_ubicaciones_finalizado"),
                kv("cantidad", ubicaciones.size()));
        return response;
    }

    public UbicacionRegisterResponse register(UbicacionRegisterRequest request) throws ServiceError {
        log.info("Iniciando register para nueva Ubicacion",
                kv("evento", "register_ubicacion"),
                kv("direccion", request.getDireccion()));

        Ubicacion ubicacion = new Ubicacion();

        ubicacion.setDireccionTextual(request.getDireccion());
        ubicacion.setLatitud(request.getLatitud());
        ubicacion.setLongitud(request.getLongitud());

        log.debug("Datos de nueva ubicacion a persistir",
                kv("evento", "datos_ubicacion_persistir"),
                kv("direccion", ubicacion.getDireccionTextual()),
                kv("latitud", ubicacion.getLatitud()),
                kv("longitud", ubicacion.getLongitud()));

        Ubicacion savedUbicacion = this.ubicacionRepository.save(ubicacion);

        log.info("Ubicacion registrada exitosamente",
                kv("evento", "ubicacion_registrada"),
                kv("id_ubicacion", savedUbicacion.getIdUbicacion()));
        return UbicacionesMapperDto.toResponsePostRegister(savedUbicacion);
    }

    public UbicacionEditResponse edit(UbicacionEditRequest request) throws ServiceError {
        log.info("Iniciando edit para Ubicacion",
                kv("evento", "edit_ubicacion"),
                kv("id_ubicacion", request.getIdUbicacion()));

        Ubicacion ubicacion = this.ubicacionRepository.getById(request.getIdUbicacion());

        if (ubicacion == null) {
            throw new ServiceError("", Errores.UBICACION_NO_ENCONTRADA, 404);
        }

        log.debug("Ubicacion ANTES de editar",
                kv("evento", "ubicacion_antes_editar"),
                kv("id_ubicacion", ubicacion.getIdUbicacion()),
                kv("direccion", ubicacion.getDireccionTextual()),
                kv("latitud", ubicacion.getLatitud()),
                kv("longitud", ubicacion.getLongitud()));

        if (request.getLatitud() != null) {
            ubicacion.setLatitud(request.getLatitud());
        }
        if (request.getLongitud() != null) {
            ubicacion.setLongitud(request.getLongitud());
        }
        if(request.getDireccion() != null){
            ubicacion.setDireccionTextual(request.getDireccion());
        }

        this.ubicacionRepository.update(ubicacion);
        log.debug("Ubicacion DESPUES de editar",
                kv("evento", "ubicacion_despues_editar"),
                kv("id_ubicacion", ubicacion.getIdUbicacion()),
                kv("direccion", ubicacion.getDireccionTextual()),
                kv("latitud", ubicacion.getLatitud()),
                kv("longitud", ubicacion.getLongitud()));

        UbicacionEditResponse response = UbicacionesMapperDto.toResponsePatchEdit(ubicacion);

        log.info("Finalizado edit para Ubicacion",
                kv("evento", "edit_ubicacion_finalizado"),
                kv("id_ubicacion", request.getIdUbicacion()));
        return response;
    }

    public void delete(Long id) throws ServiceError {
        log.info("Iniciando delete para Ubicacion",
                kv("evento", "delete_ubicacion"),
                kv("id_ubicacion", id));

        Ubicacion ubicacion = this.ubicacionRepository.getById(id);

        if (ubicacion == null) {
            throw new ServiceError("", Errores.UBICACION_NO_ENCONTRADA, 404);
        }

        this.ubicacionRepository.delete(id);
        log.info("Finalizado delete para Ubicacion",
                kv("evento", "delete_ubicacion_finalizado"),
                kv("id_ubicacion", id));
    }

}
