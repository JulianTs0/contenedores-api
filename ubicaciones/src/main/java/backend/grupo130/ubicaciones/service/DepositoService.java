
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

import static net.logstash.logback.argument.StructuredArguments.kv;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class DepositoService {

    private final UbicacionRepository ubicacionRepository;

    private final DepositoRepository depositoRepository;

    public DepositoGetByIdResponse getById(DepositoGetByIdRequest request) throws ServiceError {
        log.info("Iniciando getById para Deposito",
                kv("evento", "get_by_id_deposito"),
                kv("id_deposito", request.getIdDeposito()));

        Deposito deposito = this.depositoRepository.getById(request.getIdDeposito());

        if (deposito == null) {
            throw new ServiceError("", Errores.DEPOSITO_NO_ENCONTRADO, 404);
        }

        log.debug("Deposito encontrado",
                kv("evento", "deposito_encontrado"),
                kv("id_deposito", deposito.getIdDeposito()));

        Long idUbicacion = this.ubicacionRepository.findByDepositoId(deposito.getIdDeposito());
        log.debug("Ubicacion asociada encontrada",
                kv("evento", "ubicacion_asociada_encontrada"),
                kv("id_ubicacion", idUbicacion));

        DepositoGetByIdResponse response = DepositoMapperDto.toResponseGetById(deposito, idUbicacion);

        log.info("Finalizado getById para Deposito",
                kv("evento", "get_by_id_deposito_finalizado"),
                kv("id_deposito", request.getIdDeposito()));
        return response;
    }

    public DepositoGetAllResponse getAll() throws ServiceError {
        log.info("Iniciando getAll para Depositos",
                kv("evento", "get_all_depositos"));

        List<Deposito> depositos = this.depositoRepository.getAll();
        log.debug("Se encontraron depositos",
                kv("evento", "depositos_encontrados"),
                kv("cantidad", depositos.size()));

        DepositoGetAllResponse response = DepositoMapperDto.toResponseGetAll(depositos);

        log.info("Finalizado getAll para Depositos",
                kv("evento", "get_all_depositos_finalizado"),
                kv("cantidad", depositos.size()));
        return response;
    }


    public DepositoRegisterResponse register(DepositoRegisterRequest request) throws ServiceError {
        log.info("Iniciando register para nuevo Deposito",
                kv("evento", "register_deposito"),
                kv("id_ubicacion", request.getIdUbicacion()));

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

        log.debug("Datos de nuevo deposito a persistir",
                kv("evento", "datos_deposito_persistir"),
                kv("nombre", deposito.getNombre()),
                kv("costo", deposito.getCostoEstadiaDiario()));

        Deposito savedDeposito = this.depositoRepository.save(deposito);

        ubicacion.setDeposito(savedDeposito);

        this.ubicacionRepository.update(ubicacion);

        log.info("Deposito registrado y asignado a Ubicacion exitosamente",
                kv("evento", "deposito_registrado"),
                kv("id_deposito", savedDeposito.getIdDeposito()),
                kv("id_ubicacion", ubicacion.getIdUbicacion()));

        return DepositoMapperDto.toResponsePostRegister(savedDeposito);
    }

    public DepositoEditResponse edit(DepositoEditRequest request) throws ServiceError {
        log.info("Iniciando edit para Deposito",
                kv("evento", "edit_deposito"),
                kv("id_deposito", request.getIdDeposito()));

        Deposito deposito = this.depositoRepository.getById(request.getIdDeposito());

        if (deposito == null) {
            throw new ServiceError("", Errores.DEPOSITO_NO_ENCONTRADO, 404);
        }

        log.debug("Deposito ANTES de editar",
                kv("evento", "deposito_antes_editar"),
                kv("id_deposito", deposito.getIdDeposito()),
                kv("nombre", deposito.getNombre()),
                kv("costo", deposito.getCostoEstadiaDiario()));

        if (request.getNombre() != null) {
            deposito.setNombre(request.getNombre());
        }
        if (request.getCostoEstadiaDiario() != null) {
            deposito.setCostoEstadiaDiario(request.getCostoEstadiaDiario());
        }

        this.depositoRepository.update(deposito);

        log.debug("Deposito DESPUES de editar",
                kv("evento", "deposito_despues_editar"),
                kv("id_deposito", deposito.getIdDeposito()),
                kv("nombre", deposito.getNombre()),
                kv("costo", deposito.getCostoEstadiaDiario()));

        DepositoEditResponse response = DepositoMapperDto.toResponsePatchEdit(deposito);

        log.info("Finalizado edit para Deposito",
                kv("evento", "edit_deposito_finalizado"),
                kv("id_deposito", request.getIdDeposito()));
        return response;
    }

    public void delete(Long id) throws ServiceError {
        log.info("Iniciando delete para Deposito",
                kv("evento", "delete_deposito"),
                kv("id_deposito", id));

        Deposito deposito = this.depositoRepository.getById(id);

        if (deposito == null) {
            throw new ServiceError("", Errores.DEPOSITO_NO_ENCONTRADO, 404);
        }

        Ubicacion ubicacion = this.ubicacionRepository.findUbicacionByDepositoId(deposito.getIdDeposito());

        if(ubicacion == null){
            throw new ServiceError("", Errores.DEPOSITO_SIN_UBICACION, 500);
        }

        log.debug("Desvinculando Deposito de Ubicacion",
                kv("evento", "desvinculando_deposito"),
                kv("id_deposito", deposito.getIdDeposito()),
                kv("id_ubicacion", ubicacion.getIdUbicacion()));

        ubicacion.setDeposito(null);
        this.ubicacionRepository.update(ubicacion);

        this.depositoRepository.delete(deposito.getIdDeposito());
        log.info("Finalizado delete para Deposito",
                kv("evento", "delete_deposito_finalizado"),
                kv("id_deposito", id));
    }

}
