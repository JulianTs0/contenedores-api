
package backend.grupo130.ubicaciones.service;

import backend.grupo130.ubicaciones.data.entity.Deposito;
import backend.grupo130.ubicaciones.data.entity.Ubicacion;
import backend.grupo130.ubicaciones.repository.DepositoRepository;
import backend.grupo130.ubicaciones.repository.UbicacionRepository;
import backend.grupo130.ubicaciones.config.enums.Errores;
import backend.grupo130.ubicaciones.config.exceptions.ServiceError;
import backend.grupo130.ubicaciones.dto.deposito.DepositoMapperDto;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoEditRequest;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoGetByIdRequest;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoRegisterRequest;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoEditResponse;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoGetAllResponse;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoGetByIdResponse;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoRegisterResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class DepositoService {

    private final UbicacionRepository ubicacionRepository;

    private final DepositoRepository depositoRepository;

    public DepositoGetByIdResponse getById(DepositoGetByIdRequest request) throws ServiceError {
        log.info("Iniciando getById para Deposito con ID: {}", request.getIdDeposito());

        Deposito deposito = this.depositoRepository.getById(request.getIdDeposito());

        if (deposito == null) {
            throw new ServiceError("", Errores.DEPOSITO_NO_ENCONTRADO, 404);
        }

        log.debug("Deposito encontrado: {}", deposito.getIdDeposito());

        Long idUbicacion = this.ubicacionRepository.findByDepositoId(deposito.getIdDeposito());
        log.debug("Ubicacion asociada ID: {}", idUbicacion);

        DepositoGetByIdResponse response = DepositoMapperDto.toResponseGetById(deposito, idUbicacion);

        log.info("Finalizado getById para Deposito con ID: {}", request.getIdDeposito());
        return response;
    }

    public DepositoGetAllResponse getAll() throws ServiceError {
        log.info("Iniciando getAll para Depositos");

        List<Deposito> depositos = this.depositoRepository.getAll();
        log.debug("Se encontraron {} depositos", depositos.size());

        DepositoGetAllResponse response = DepositoMapperDto.toResponseGetAll(depositos);

        log.info("Finalizado getAll para Depositos. Total: {}", depositos.size());
        return response;
    }


    public DepositoRegisterResponse register(DepositoRegisterRequest request) throws ServiceError {
        log.info("Iniciando register para nuevo Deposito en Ubicacion ID: {}", request.getIdUbicacion());

        Ubicacion ubicacion = this.ubicacionRepository.getById(request.getIdUbicacion());

        if (ubicacion == null) {
            throw new ServiceError("", Errores.UBICACION_NO_ENCONTRADA, 404);
        }
        if (ubicacion.getDeposito() != null){
            throw new ServiceError("", Errores.UBICACION_CON_DEPOSTIO, 400);
        }

        Deposito deposito = new Deposito();
        deposito.setNombre(request.getNombre());
        deposito.setCostoEstadiaDiario(request.getCostoEstadiaDiario());

        log.debug("Datos de nuevo deposito a persistir: Nombre={}, Costo={}",
            deposito.getNombre(), deposito.getCostoEstadiaDiario());

        Deposito savedDeposito = this.depositoRepository.save(deposito);

        ubicacion.setDeposito(savedDeposito);

        this.ubicacionRepository.update(ubicacion);

        log.info("Deposito registrado (ID: {}) y asignado a Ubicacion (ID: {}) exitosamente.",
            savedDeposito.getIdDeposito(), ubicacion.getIdUbicacion());

        return DepositoMapperDto.toResponsePostRegister(savedDeposito);
    }

    public DepositoEditResponse edit(DepositoEditRequest request) throws ServiceError {
        log.info("Iniciando edit para Deposito con ID: {}", request.getIdDeposito());

        Deposito deposito = this.depositoRepository.getById(request.getIdDeposito());

        if (deposito == null) {
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

        DepositoEditResponse response = DepositoMapperDto.toResponsePatchEdit(deposito);

        log.info("Finalizado edit para Deposito con ID: {}", request.getIdDeposito());
        return response;
    }

    public void delete(Long id) throws ServiceError {
        log.info("Iniciando delete para Deposito con ID: {}", id);

        Deposito deposito = this.depositoRepository.getById(id);

        if (deposito == null) {
            throw new ServiceError("", Errores.DEPOSITO_NO_ENCONTRADO, 404);
        }

        Ubicacion ubicacion = this.ubicacionRepository.findUbicacionByDepositoId(deposito.getIdDeposito());

        if(ubicacion == null){
            throw new ServiceError("", Errores.DEPOSITO_SIN_UBICACION, 500);
        }

        log.debug("Desvinculando Deposito ID: {} de Ubicacion ID: {}", deposito.getIdDeposito(), ubicacion.getIdUbicacion());

        ubicacion.setDeposito(null);
        this.ubicacionRepository.update(ubicacion);

        this.depositoRepository.delete(deposito.getIdDeposito());
        log.info("Finalizado delete para Deposito con ID: {}", id);
    }

}
