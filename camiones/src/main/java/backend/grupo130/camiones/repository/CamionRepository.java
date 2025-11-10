package backend.grupo130.camiones.repository;


import backend.grupo130.camiones.data.models.Camion;
import backend.grupo130.camiones.data.repository.PostgresCamionRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class CamionRepository {

    private final PostgresCamionRepositoryI camionRepository;

    public Camion getById(Integer camionId) {
        return this.camionRepository.findById(camionId).orElse(null);
    }

    public List<Camion> getAll() {
        return this.camionRepository.findAll();
    }

    public Camion save(Camion camion) {
        return this.camionRepository.save(camion);
    }

    public Camion update(Camion camion) {
        return this.camionRepository.save(camion);
    }

    public void delete(Integer camionId) {
        this.camionRepository.deleteById(camionId);
    }
}
