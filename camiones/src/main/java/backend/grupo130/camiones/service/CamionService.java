package backend.grupo130.camiones.service;

import backend.grupo130.camiones.client.usuarios.UsuarioClient;
import backend.grupo130.camiones.client.usuarios.entity.Usuario;
import backend.grupo130.camiones.config.enums.Errores;
import backend.grupo130.camiones.config.enums.Rol;
import backend.grupo130.camiones.config.exceptions.ServiceError;
import backend.grupo130.camiones.data.entity.Camion;
import backend.grupo130.camiones.dto.CamionesMapperDto;
import backend.grupo130.camiones.dto.request.*;
import backend.grupo130.camiones.dto.response.*;
import backend.grupo130.camiones.repository.CamionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.kv;


@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CamionService {

    private final CamionRepository camionRepository;

    private final UsuarioClient usuarioClient;

    // GET

    public GetByIdResponse getById(GetByIdRequest request) throws ServiceError {

        log.info("Iniciando búsqueda de camión por dominio", 
            kv("evento", "busqueda_camion"), 
            kv("dominio", request.getDominio())
        );

        Camion camion = this.camionRepository.getById(request.getDominio());

        if (camion == null) {
            throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
        }

        Usuario usuario = null;

        if(camion.getTransportista() != null && camion.getTransportista().getIdUsuario() != null){
            log.debug("Camion encontrado. Buscando datos del transportista", 
                kv("evento", "busqueda_transportista"), 
                kv("transportista_id", camion.getTransportista().getIdUsuario())
            );
            usuario = this.usuarioClient.getById(camion.getTransportista().getIdUsuario());
        }

        camion.setTransportista(usuario);

        GetByIdResponse response = CamionesMapperDto.toResponseGetId(camion);

        log.info("Búsqueda exitosa para camión", 
            kv("evento", "busqueda_camion_exitosa"), 
            kv("dominio", request.getDominio())
        );
        return response;
    }

    public GetAllResponse getAll() throws ServiceError {
        log.info("Iniciando obtención de todos los camiones", 
            kv("evento", "obtener_todos_camiones")
        );

        List<Camion> camiones = this.camionRepository.getAll();

        GetAllResponse response = CamionesMapperDto.toResponseGetAll(camiones);

        log.info("Se encontraron camiones en total", 
            kv("evento", "obtener_todos_camiones_exitoso"), 
            kv("cantidad", camiones.size())
        );
        return response;
    }

    public GetAllResponse getDisponibles() throws ServiceError {
        log.info("Iniciando obtención de camiones disponibles", 
            kv("evento", "obtener_camiones_disponibles")
        );

        List<Camion> camiones = this.camionRepository.findDisponibilidad();

        GetAllResponse response = CamionesMapperDto.toResponseGetAll(camiones);

        log.info("Se encontraron camiones disponibles", 
            kv("evento", "obtener_camiones_disponibles_exitoso"), 
            kv("cantidad", camiones.size())
        );
        return response;
    }

    public GetPromedioCostoBaseResponse getCostoAprox(GetCostoPromedio request) throws ServiceError {
        log.info("Iniciando cálculo de costo aproximado", 
            kv("evento", "calculo_costo_aprox"), 
            kv("peso", request.getCapacidadPeso()), 
            kv("volumen", request.getCapacidadVolumen())
        );

        BigDecimal promedio = this.camionRepository.getPromedioCostoTraslado(request.getCapacidadPeso(), request.getCapacidadVolumen());

        GetPromedioCostoBaseResponse response = CamionesMapperDto.toResponsePromedioCostoBase(promedio.setScale(2, RoundingMode.HALF_UP));

        log.info("Costo aproximado calculado", 
            kv("evento", "calculo_costo_aprox_exitoso"), 
            kv("promedio", promedio)
        );
        return response;
    }

    public GetPromedioCombustibleActualResponse getConsumoPromedio() throws ServiceError {

        log.info("Iniciando cálculo de consumo promedio de combustible total", 
            kv("evento", "calculo_consumo_promedio")
        );

        BigDecimal promedio = this.camionRepository.getPromedioConsumoTotal();

        GetPromedioCombustibleActualResponse response = CamionesMapperDto.toResponsePromedioCombustible(promedio.setScale(2, RoundingMode.HALF_UP));

        log.info("Consumo promedio total calculado", 
            kv("evento", "calculo_consumo_promedio_exitoso"), 
            kv("promedio", promedio)
        );
        return response;
    }

    // POST

    public RegisterResponse register(RegisterRequest request) throws ServiceError {
        log.info("Iniciando registro de nuevo camión", 
            kv("evento", "registro_camion"), 
            kv("dominio", request.getDominio())
        );

        Camion camion = new Camion();

        camion.setDominio(request.getDominio());
        camion.setCapacidadPeso(request.getCapacidadPeso());
        camion.setCapacidadVolumen(request.getCapacidadVolumen());
        camion.setConsumoCombustible(request.getConsumoCombustible());
        camion.setCostoTrasladoBase(request.getCostoTrasladoBase());
        camion.setEstado(false);
        camion.setTransportista(null);

        Camion savedCamion = this.camionRepository.save(camion);
        log.info("Camión registrado exitosamente", 
            kv("evento", "registro_camion_exitoso"), 
            kv("dominio", savedCamion.getDominio())
        );
        
        return CamionesMapperDto.toResponsePostRegister(savedCamion);
    }

    // PATCH

    public CambiarDisponibilidadResponse cambiarDisponibilidad(CambiarDisponibilidadRequest request) throws ServiceError {
        log.info("Iniciando cambio de disponibilidad para camión", 
            kv("evento", "cambio_disponibilidad"), 
            kv("dominio", request.getDominio()), 
            kv("nuevo_estado", request.getEstado())
        );

        Camion camion = this.camionRepository.getById(request.getDominio());

        if (camion == null) {
            throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
        }

        camion.setEstado(request.getEstado());

        this.camionRepository.update(camion);

        CambiarDisponibilidadResponse response = CamionesMapperDto.toResponsePatchDispo(camion);

        log.info("Disponibilidad cambiada exitosamente para camión", 
            kv("evento", "cambio_disponibilidad_exitoso"), 
            kv("dominio", camion.getDominio()), 
            kv("nuevo_estado", camion.getEstado())
        );
        return response;

    }

    public EditResponse edit(EditRequest request) throws ServiceError {
        log.info("Iniciando edición de datos para camión", 
            kv("evento", "edicion_camion"), 
            kv("dominio", request.getDominio())
        );

        Camion camion = this.camionRepository.getById(request.getDominio());

        if (camion == null) {
            throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
        }
        if (!camion.getEstado()){
            throw new ServiceError("", Errores.CAMION_NO_DISPONIBLE, 400);
        }

        if (request.getCapacidadPeso() != null) {
            camion.setCapacidadPeso(request.getCapacidadPeso());
        }
        if (request.getCapacidadVolumen() != null) {
            camion.setCapacidadVolumen(request.getCapacidadVolumen());
        }
        if (request.getConsumoCombustible() != null) {
            camion.setConsumoCombustible(request.getConsumoCombustible());
        }
        if (request.getCostoTrasladoBase() != null) {
            camion.setCostoTrasladoBase(request.getCostoTrasladoBase());
        }

        this.camionRepository.update(camion);

        EditResponse response = CamionesMapperDto.toResponsePatchEdit(camion);

        log.info("Camión editado exitosamente", 
            kv("evento", "edicion_camion_exitosa"), 
            kv("dominio", camion.getDominio())
        );
        return response;
    }

    public AsignarTransportistaResponse asignarTransportista(AsignarTransportistaRequest request) throws ServiceError {
        log.info("Iniciando asignación de transportista a camión", 
            kv("evento", "asignacion_transportista"), 
            kv("transportista_id", request.getIdTransportista()), 
            kv("dominio", request.getDominio())
        );

        Camion camion = this.camionRepository.getById(request.getDominio());

        if (camion == null) {
            throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
        }
        if (!camion.getEstado()){
            throw new ServiceError("", Errores.CAMION_NO_DISPONIBLE, 400);
        }

        Usuario usuario = this.usuarioClient.getById(request.getIdTransportista());

        if (usuario == null) {
            throw new ServiceError("", Errores.USUARIO_NO_ENCONTRADO, 404);
        }

        if(!usuario.getRoles().contains(Rol.TRANSPORTISTA)){
            throw new ServiceError("", Errores.USUARIO_NO_TRANSPORTISTA, 400);
        }

        camion.setTransportista(usuario);

        Camion updated = this.camionRepository.update(camion);
        log.info("Transportista asignado exitosamente a camión", 
            kv("evento", "asignacion_transportista_exitosa"), 
            kv("transportista_id", request.getIdTransportista()), 
            kv("dominio", request.getDominio())
        );
        return CamionesMapperDto.toResponsePatchTrans(updated, usuario);
    }

    // DELETE

    public void delete(DeleteRequest request) throws ServiceError {
        log.info("Iniciando eliminación de camión", 
            kv("evento", "eliminacion_camion"), 
            kv("dominio", request.getDominio())
        );

        Camion camion = this.camionRepository.getById(request.getDominio());

        if (camion == null) {
            throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
        }
        if (!camion.getEstado()){
            throw new ServiceError("", Errores.CAMION_NO_DISPONIBLE, 400);
        }

        this.camionRepository.delete(camion.getDominio());
        log.info("Camión eliminado exitosamente", 
            kv("evento", "eliminacion_camion_exitosa"), 
            kv("dominio", request.getDominio())
        );
    }
}
