
package backend.grupo130.camiones.repository;

import backend.grupo130.camiones.data.models.Camion;
import backend.grupo130.camiones.data.repository.PostgresCamionRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@AllArgsConstructor
public class CamionRepository {

    // Inyección del repositorio JPA
    private final PostgresCamionRepositoryI postgresRepository;

    // Obtener camión por dominio
    public Camion getByDominio(String dominio) {
        return postgresRepository.findById(dominio).orElse(null);
    }

    // Obtener todos los camiones
    public List<Camion> getAll() {
        return postgresRepository.findAll();
    }

    // Guardar o actualizar un camión
    public Camion save(Camion camion) {
        return postgresRepository.save(camion);
    }

    // Eliminar un camión por dominio
    public void deleteByDominio(String dominio) {
        postgresRepository.deleteById(dominio);
    }

    // Obtener solo camiones disponibles
    public List<Camion> getDisponibles() {
        return postgresRepository.findByDisponibleTrue();
    }

    // Obtener camiones aptos para traslado (por capacidad mínima)
    public List<Camion> getAptos(Double peso, Double volumen) {
        return postgresRepository
                .findByCapacidadPesoGreaterThanEqualAndCapacidadVolumenGreaterThanEqual(peso, volumen);
    }
}