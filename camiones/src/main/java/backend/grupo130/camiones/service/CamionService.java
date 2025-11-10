package backend.grupo130.camiones.service;

import backend.grupo130.camiones.data.models.Camion;
import backend.grupo130.camiones.repository.CamionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CamionService {

    private final CamionRepository repository;

    @Autowired
    public CamionService(CamionRepository repository) {
        this.repository = repository;
    }

    public List<Camion> findAll() {
        return repository.findAll();
    }

    public Optional<Camion> findById(String dominio) {
        return repository.findById(dominio);
    }

    public List<Camion> findAvailable() {
        return repository.findByDisponibleTrue();
    }

    @Transactional
    public Camion save(Camion camion) {
        return repository.save(camion);
    }

    @Transactional
    public void deleteById(String dominio) {
        repository.deleteById(dominio);
    }

    public List<Camion> findAptosParaTraslado(Double peso, Double volumen) {
        return repository.findByCapacidadPesoGreaterThanEqualAndCapacidadVolumenGreaterThanEqual(peso, volumen);
    }
}
