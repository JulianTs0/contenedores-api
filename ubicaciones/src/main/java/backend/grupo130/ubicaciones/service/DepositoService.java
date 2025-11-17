
package backend.grupo130.ubicaciones.service;

import backend.grupo130.ubicaciones.Repository.DepositoRepository;
import backend.grupo130.ubicaciones.Repository.UbicacionRepository;
import backend.grupo130.ubicaciones.config.enums.Errores;
import backend.grupo130.ubicaciones.config.exceptions.ServiceError;
import backend.grupo130.ubicaciones.data.models.Deposito;
import backend.grupo130.ubicaciones.data.models.Ubicacion;
import backend.grupo130.ubicaciones.dto.deposito.DepositoMapperDto;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoDeleteRequest;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoEditRequest;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoGetByIdRequest;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoRegisterRequest;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoEditResponse;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoGetAllResponse;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoGetByIdResponse;
import backend.grupo130.ubicaciones.dto.ubicaciones.UbicacionesMapperDto;
import backend.grupo130.ubicaciones.dto.ubicaciones.request.UbicacionDeleteRequest;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionGetByIdResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j // <--- PASO 1: Incluir el Logger (usando Lombok)
public class DepositoService {

    private final UbicacionRepository ubicacionRepository;

    private final DepositoRepository depositoRepository;

    public DepositoGetByIdResponse getById(DepositoGetByIdRequest request) throws ServiceError {
        // PASO 2: Log de nivel INFO
        log.info("Iniciando getById para Deposito con ID: {}", request.getIdDeposito());
        try {

            Deposito deposito = this.depositoRepository.getById(request.getIdDeposito());

            if (deposito == null) {
                // PASO 2: Log de nivel WARN
                log.warn("No se encontró Deposito con ID: {}", request.getIdDeposito());
                throw new ServiceError("", Errores.DEPOSITO_NO_ENCONTRADO, 404);
            }

            // PASO 2: Log de nivel DEBUG
            log.debug("Deposito encontrado: {}", deposito.getIdDeposito());

            Long idUbicacion = this.ubicacionRepository.findByDepositoId(deposito.getIdDeposito());
            log.debug("Ubicacion asociada ID: {}", idUbicacion);

            DepositoGetByIdResponse response = DepositoMapperDto.toResponseGet(deposito, idUbicacion);

            log.info("Finalizado getById para Deposito con ID: {}", request.getIdDeposito());
            return response;
        } catch (ServiceError ex) {
            // PASO 2: Log de nivel ERROR (preservando la traza)
            log.error("Error de servicio en getById de Deposito: {}", ex.getMessage(), ex);
            throw ex;
        }
        catch (Exception ex) {
            // PASO 2: Log de nivel ERROR
            log.error("Error interno inesperado en getById de Deposito", ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public DepositoGetAllResponse getAll() throws ServiceError {
        log.info("Iniciando getAll para Depositos");
        try {

            List<Deposito> depositos = this.depositoRepository.getAll();
            log.debug("Se encontraron {} depositos", depositos.size());

            DepositoGetAllResponse response = DepositoMapperDto.toResponseGet(depositos);

            log.info("Finalizado getAll para Depositos. Total: {}", depositos.size());
            return response;
        } catch (ServiceError ex) {
            log.error("Error de servicio en getAll de Depositos: {}", ex.getMessage(), ex);
            throw ex;
        }  catch (Exception ex) {
            log.error("Error interno inesperado en getAll de Depositos", ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public void register(DepositoRegisterRequest request) throws ServiceError {
        log.info("Iniciando register para nuevo Deposito en Ubicacion ID: {}", request.getIdUbicacion());
        try {

            Ubicacion ubicacion = this.ubicacionRepository.getById(request.getIdUbicacion());

            if (ubicacion == null) {
                log.warn("No se encontró Ubicacion con ID: {} para registrar Deposito", request.getIdUbicacion());
                throw new ServiceError("", Errores.UBICACION_NO_ENCONTRADA, 404);
            }
            if (ubicacion.getDeposito() != null){
                log.warn("La Ubicacion con ID: {} ya tiene un deposito asignado (ID: {})", ubicacion.getIdUbicacion(), ubicacion.getDeposito().getIdDeposito());
                throw new ServiceError("", Errores.UBICACION_CON_DEPOSTIO, 400);
            }

            Deposito deposito = new Deposito();
            deposito.setNombre(request.getNombre());
            deposito.setCostoEstadiaDiario(request.getCostoEstadiaDiario());

            log.debug("Datos de nuevo deposito a persistir: Nombre={}, Costo={}",
                deposito.getNombre(), deposito.getCostoEstadiaDiario());

            ubicacion.setDeposito(deposito);

            this.depositoRepository.save(deposito);
            this.ubicacionRepository.update(ubicacion);

            log.info("Deposito registrado (ID: {}) y asignado a Ubicacion (ID: {}) exitosamente.",
                deposito.getIdDeposito(), ubicacion.getIdUbicacion());

        } catch (ServiceError ex) {
            log.error("Error de servicio en register de Deposito: {}", ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno inesperado en register de Deposito", ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public DepositoEditResponse edit(DepositoEditRequest request) throws ServiceError {
        log.info("Iniciando edit para Deposito con ID: {}", request.getIdDeposito());
        try {

            Deposito deposito = this.depositoRepository.getById(request.getIdDeposito());

            if (deposito == null) {
                log.warn("No se encontró Deposito para editar con ID: {}", request.getIdDeposito());
                throw new ServiceError("", Errores.DEPOSITO_NO_ENCONTRADO, 404);
            }

            log.debug("Deposito ANTES de editar: ID={}, Nombre={}, Costo={}",
                deposito.getIdDeposito(), deposito.getNombre(), deposito.getCostoEstadiaDiario());

            if (request.getNombre() != null) {
                deposito.setNombre(request.getNombre());
            }
            if (request.getCostoEstadiaDiario() != null) {
                deposito.setCostoEstadiaDiario(request.getCostoEstadiaDiario());
            }

            this.depositoRepository.update(deposito);

            log.debug("Deposito DESPUES de editar: ID={}, Nombre={}, Costo={}",
                deposito.getIdDeposito(), deposito.getNombre(), deposito.getCostoEstadiaDiario());

            DepositoEditResponse response = DepositoMapperDto.toResponsePatch(deposito);

            log.info("Finalizado edit para Deposito con ID: {}", request.getIdDeposito());
            return response;
        } catch (ServiceError ex) {
            log.error("Error de servicio en edit de Deposito: {}", ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno inesperado en edit de Deposito", ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

    public void delete(DepositoDeleteRequest request) throws ServiceError {
        log.info("Iniciando delete para Deposito con ID: {}", request.getIdDeposito());
        try {

            Deposito deposito = this.depositoRepository.getById(request.getIdDeposito());

            if (deposito == null) {
                log.warn("No se encontró Deposito para eliminar con ID: {}", request.getIdDeposito());
                throw new ServiceError("", Errores.DEPOSITO_NO_ENCONTRADO, 404);
            }

            Ubicacion ubicacion = this.ubicacionRepository.findUbicacionByDepositoId(deposito.getIdDeposito());

            if (ubicacion != null) {
                log.debug("Desvinculando Deposito ID: {} de Ubicacion ID: {}", deposito.getIdDeposito(), ubicacion.getIdUbicacion());
                ubicacion.setDeposito(null);
                // this.ubicacionRepository.update(ubicacion); // Asumo que esto se maneja por cascada o ya está en la sesión transaccional
            } else {
                log.warn("El deposito ID: {} a eliminar no estaba asociado a ninguna ubicacion.", deposito.getIdDeposito());
            }

            this.depositoRepository.delete(deposito.getIdDeposito());
            log.info("Finalizado delete para Deposito con ID: {}", request.getIdDeposito());

        } catch (ServiceError ex) {
            log.error("Error de servicio en delete de Deposito: {}", ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Error interno inesperado en delete de Deposito", ex);
            throw new ServiceError(ex.getMessage() , Errores.ERROR_INTERNO, 500);
        }
    }

}
