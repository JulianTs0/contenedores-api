
package backend.grupo130.camiones.repository;

import backend.grupo130.camiones.data.PersistanceMapper;
import backend.grupo130.camiones.data.entity.Camion;
import backend.grupo130.camiones.data.models.CamionModel;
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
        CamionModel model = this.postgresRepository.findById(dominio).orElse(null);
        return PersistanceMapper.toDomain(model);
    }

    public List<Camion> getAll() {
        List<CamionModel> models = this.postgresRepository.findAll();
        return PersistanceMapper.toDomain(models);
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
        List<CamionModel> models = this.postgresRepository.findByEstadoTrue();
        return PersistanceMapper.toDomain(models);
    }

    public Camion save(Camion camion) {
        CamionModel model = PersistanceMapper.toModel(camion);
        CamionModel saved = this.postgresRepository.save(model);
        return PersistanceMapper.toDomain(saved);
    }

    public Camion update(Camion camion) {
        CamionModel model = PersistanceMapper.toModel(camion);
        CamionModel updated = this.postgresRepository.save(model);
        return PersistanceMapper.toDomain(updated);
    }

    public void delete(String dominio){
        this.postgresRepository.deleteById(dominio);
    }
    
}
