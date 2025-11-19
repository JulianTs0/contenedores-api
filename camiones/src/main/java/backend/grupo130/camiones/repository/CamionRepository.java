
package backend.grupo130.camiones.repository;

import backend.grupo130.camiones.data.models.Camion;
import backend.grupo130.camiones.data.repository.PostgresCamionRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@AllArgsConstructor
public class CamionRepository {
    
    private final PostgresCamionRepositoryI postgresRepository;

    public Camion getById(String dominio){
        Camion model = this.postgresRepository.findById(dominio).orElse(null);
        return model;
    }

    public List<Camion> getAll() {
        List<Camion> models = this.postgresRepository.findAll();
        return models;
    }

    public BigDecimal getPromedioCostoTraslado(BigDecimal peso, BigDecimal volumen){
        BigDecimal promedio = this.postgresRepository.findAverageCostoTraslado(peso, volumen);
        return (promedio != null) ? promedio : BigDecimal.ZERO;
    }

    public BigDecimal getPromedioConsumoTotal() {
        BigDecimal promedio = this.postgresRepository.findAverageConsumoCombustible();
        return (promedio != null) ? promedio : BigDecimal.ZERO;
    }

    public List<Camion> findDisponibilidad(){
        List<Camion> models = this.postgresRepository.findByEstadoTrue();
        return models;
    }

    public Camion save(Camion camion) {
        Camion saved = this.postgresRepository.save(camion);
        return saved;
    }

    public Camion update(Camion camion) {
        Camion updated = this.postgresRepository.save(camion);
        return updated;
    }

    public void delete(String dominio){
        this.postgresRepository.deleteById(dominio);
        return;
    }
    
}
