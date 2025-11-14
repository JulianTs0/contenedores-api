
package backend.grupo130.ubicaciones.service;

import backend.grupo130.ubicaciones.Repository.DepositoRepository;
import backend.grupo130.ubicaciones.Repository.UbicacionRepository;
import backend.grupo130.ubicaciones.config.exceptions.ServiceError;
import backend.grupo130.ubicaciones.data.models.Deposito;
import backend.grupo130.ubicaciones.data.models.Ubicacion;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoEditRequest;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoGetByIdRequest;
import backend.grupo130.ubicaciones.dto.deposito.request.DepositoRegisterRequest;
import backend.grupo130.ubicaciones.dto.deposito.response.DepositoGetByIdResponse;
import backend.grupo130.ubicaciones.dto.ubicaciones.response.UbicacionGetByIdResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class DepositoService {

    private final UbicacionRepository ubicacionRepository;

    private final DepositoRepository depositoRepository;

    public Deposito getById(DepositoGetByIdRequest request) throws ServiceError {
        try {

            Deposito deposito = this.depositoRepository.getById(request.getIdDeposito());

            if (deposito == null) {
                throw new ServiceError("Deposito no encontrado", 404);
            }

            return deposito;
        } catch (ServiceError ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public List<Deposito> getAll() throws ServiceError {
        try {

            List<Deposito> depositos = this.depositoRepository.getAll();

            return depositos;
        } catch (ServiceError ex) {
            throw ex;
        }  catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public void register(DepositoRegisterRequest request) throws ServiceError {
        try {

            Ubicacion ubicacion = this.ubicacionRepository.getById(request.getIdUbicacion());

            if (ubicacion == null) {
                throw new ServiceError("Ubicacion no encontrado", 404);
            }

            Deposito deposito = new Deposito();

            deposito.setNombre(request.getNombre());

            deposito.setCostoEstadiaDiario(request.getCostoEstadiaDiario());

            deposito.setUbicacion(ubicacion);

            this.depositoRepository.save(deposito);
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

    public Deposito edit(DepositoEditRequest request) throws ServiceError {
        try {

            Deposito deposito = this.depositoRepository.getById(request.getIdDeposito());

            if (deposito == null) {
                throw new ServiceError("Deposito no encontrado", 404);
            }

            if (request.getNombre() != null) {
                deposito.setNombre(request.getNombre());
            }
            if (request.getCostoEstadiaDiario() != null) {
                deposito.setCostoEstadiaDiario(request.getCostoEstadiaDiario());
            }
            if (request.getIdUbicacion() != null) {
                Ubicacion ubicacion = this.ubicacionRepository.getById(request.getIdUbicacion());

                if (ubicacion == null) {
                    throw new ServiceError("Ubicacion no encontrada", 404);
                }

                deposito.setUbicacion(ubicacion);
            }

            this.depositoRepository.update(deposito);

            return deposito;
        } catch (ServiceError ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceError("Error interno", 500);
        }
    }

}
