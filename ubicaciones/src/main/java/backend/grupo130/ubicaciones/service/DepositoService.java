
package backend.grupo130.ubicaciones.service;

import backend.grupo130.ubicaciones.config.exceptions.ServiceError;
import backend.grupo130.ubicaciones.data.models.Deposito;
import backend.grupo130.ubicaciones.Repository.DepositoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepositoService {

    private final DepositoRepository repository;

    @Autowired
    public DepositoService(DepositoRepository repository) {
        this.repository = repository;
    }

    public List<Deposito> findAll() {
        return repository.findAll();
    }

    public Optional<Deposito> findById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Deposito> findByNombre(String nombre) {
        return repository.findByNombre(nombre);
    }

    @Transactional
    public Deposito create(Deposito deposito) {
        repository.findByNombre(deposito.getNombre()).ifPresent(d -> {
            throw new ServiceError("Ya existe un depósito con ese nombre: " + deposito.getNombre());
        });
        return repository.save(deposito);
    }

    @Transactional
    public Deposito update(Integer id, Deposito cambios) {
        return repository.findById(id).map(existente -> {
            if (cambios.getNombre() != null) existente.setNombre(cambios.getNombre());
            if (cambios.getDireccion() != null) existente.setDireccion(cambios.getDireccion());
            if (cambios.getLatitud() != null) existente.setLatitud(cambios.getLatitud());
            if (cambios.getLongitud() != null) existente.setLongitud(cambios.getLongitud());
            if (cambios.getCostoEstadiaDiario() != null) existente.setCostoEstadiaDiario(cambios.getCostoEstadiaDiario());
            if (cambios.getObservaciones() != null) existente.setObservaciones(cambios.getObservaciones());
            return repository.save(existente);
        }).orElseThrow(() -> new ServiceError("Depósito no encontrado con id: " + id));
    }

    @Transactional
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new ServiceError("Depósito no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }

    // Ejemplo de cálculo simple de costo de estadía por días
    public double calcularCostoEstadia(Integer depositoId, int dias) {
        Deposito dep = repository.findById(depositoId)
                .orElseThrow(() -> new ServiceError("Depósito no encontrado"));
        return dep.getCostoEstadiaDiario() * dias;
    }
}