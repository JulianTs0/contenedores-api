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


@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CamionService {

    private final CamionRepository camionRepository;

    private final UsuarioClient usuarioClient;

    // GET

    public GetByIdResponse getById(GetByIdRequest request) throws ServiceError {

        log.info("Iniciando búsqueda de camión por dominio: {}", request.getDominio());

        Camion camion = this.camionRepository.getById(request.getDominio());

        if (camion == null) {
            throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
        }

        Usuario usuario = null;

        if(camion.getTransportista() != null && camion.getTransportista().getIdUsuario() != null){
            log.debug("Camion encontrado. Buscando datos del transportista ID: {}", camion.getTransportista().getIdUsuario());
            usuario = this.usuarioClient.getById(camion.getTransportista().getIdUsuario());
        }

        camion.setTransportista(usuario);

        GetByIdResponse response = CamionesMapperDto.toResponseGetId(camion);

        log.info("Búsqueda exitosa para camión con dominio: {}", request.getDominio());
        return response;
    }

    public GetAllResponse getAll() throws ServiceError {
        log.info("Iniciando obtención de todos los camiones");

        List<Camion> camiones = this.camionRepository.getAll();

        GetAllResponse response = CamionesMapperDto.toResponseGetAll(camiones);

        log.info("Se encontraron {} camiones en total", camiones.size());
        return response;
    }

    public GetAllResponse getDisponibles() throws ServiceError {
        log.info("Iniciando obtención de camiones disponibles");

        List<Camion> camiones = this.camionRepository.findDisponibilidad();

        GetAllResponse response = CamionesMapperDto.toResponseGetAll(camiones);

        log.info("Se encontraron {} camiones disponibles", camiones.size());
        return response;
    }

    public GetPromedioCostoBaseResponse getCostoAprox(GetCostoPromedio request) throws ServiceError {
        log.info("Iniciando cálculo de costo aproximado para peso {} y volumen {}", request.getCapacidadPeso(), request.getCapacidadVolumen());

        BigDecimal promedio = this.camionRepository.getPromedioCostoTraslado(request.getCapacidadPeso(), request.getCapacidadVolumen());

        GetPromedioCostoBaseResponse response = CamionesMapperDto.toResponsePromedioCostoBase(promedio.setScale(2, RoundingMode.HALF_UP));

        log.info("Costo aproximado calculado: {}", promedio);
        return response;
    }

    public GetPromedioCombustibleActualResponse getConsumoPromedio() throws ServiceError {

        log.info("Iniciando cálculo de consumo promedio de combustible total");

        BigDecimal promedio = this.camionRepository.getPromedioConsumoTotal();

        GetPromedioCombustibleActualResponse response = CamionesMapperDto.toResponsePromedioCombustible(promedio.setScale(2, RoundingMode.HALF_UP));

        log.info("Consumo promedio total calculado: {}", promedio);
        return response;
    }

    // POST

    public RegisterResponse register(RegisterRequest request) throws ServiceError {
        log.info("Iniciando registro de nuevo camión con dominio: {}", request.getDominio());

        Camion camion = new Camion();

        camion.setDominio(request.getDominio());
        camion.setCapacidadPeso(request.getCapacidadPeso());
        camion.setCapacidadVolumen(request.getCapacidadVolumen());
        camion.setConsumoCombustible(request.getConsumoCombustible());
        camion.setCostoTrasladoBase(request.getCostoTrasladoBase());
        camion.setEstado(false);
        camion.setTransportista(null);

        Camion savedCamion = this.camionRepository.save(camion);
        log.info("Camión registrado exitosamente con dominio: {}", savedCamion.getDominio());
        
        return CamionesMapperDto.toResponsePostRegister(savedCamion);
    }

    // PATCH

    public CambiarDisponibilidadResponse cambiarDisponibilidad(CambiarDisponibilidadRequest request) throws ServiceError {
        log.info("Iniciando cambio de disponibilidad para camión: {}. Nuevo estado: {}", request.getDominio(), request.getEstado());

        Camion camion = this.camionRepository.getById(request.getDominio());

        if (camion == null) {
            throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
        }

        camion.setEstado(request.getEstado());

        this.camionRepository.update(camion);

        CambiarDisponibilidadResponse response = CamionesMapperDto.toResponsePatchDispo(camion);

        log.info("Disponibilidad cambiada exitosamente para camión: {}. Nuevo estado: {}", camion.getDominio(), camion.getEstado());
        return response;

    }

    public EditResponse edit(EditRequest request) throws ServiceError {
        log.info("Iniciando edición de datos para camión: {}", request.getDominio());

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

        log.info("Camión editado exitosamente: {}", camion.getDominio());
        return response;
    }

    public AsignarTransportistaResponse asignarTransportista(AsignarTransportistaRequest request) throws ServiceError {
        log.info("Iniciando asignación de transportista ID {} a camión {}", request.getIdTransportista(), request.getDominio());

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

        if(!(usuario.getRol().equals(Rol.TRANSPORTISTA.name()))){
            throw new ServiceError("", Errores.USUARIO_NO_TRANSPORTISTA, 400);
        }

        camion.setTransportista(usuario);

        Camion updated = this.camionRepository.update(camion);
        log.info("Transportista {} asignado exitosamente a camión {}", request.getIdTransportista(), request.getDominio());
        return CamionesMapperDto.toResponsePatchTrans(updated, usuario);
    }

    // DELETE

    public void delete(DeleteRequest request) throws ServiceError {
        log.info("Iniciando eliminación de camión: {}", request.getDominio());

        Camion camion = this.camionRepository.getById(request.getDominio());

        if (camion == null) {
            throw new ServiceError("", Errores.CAMION_NO_ENCONTRADO, 404);
        }
        if (!camion.getEstado()){
            throw new ServiceError("", Errores.CAMION_NO_DISPONIBLE, 400);
        }

        this.camionRepository.delete(camion.getDominio());
        log.info("Camión eliminado exitosamente: {}", request.getDominio());
    }
}
