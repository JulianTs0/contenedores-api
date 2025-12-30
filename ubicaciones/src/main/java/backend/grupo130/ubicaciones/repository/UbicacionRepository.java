
package backend.grupo130.ubicaciones.repository;

import backend.grupo130.ubicaciones.data.PersistenceMapper;
import backend.grupo130.ubicaciones.data.entity.Ubicacion;
import backend.grupo130.ubicaciones.data.models.UbicacionModel;
import backend.grupo130.ubicaciones.data.repository.PostgresUbicacionRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class UbicacionRepository {

    private final PostgresUbicacionRepositoryI ubicacionRepository;

    public Ubicacion getById(Long idUbicacion){
        UbicacionModel model = this.ubicacionRepository.findById(idUbicacion).orElse(null);
        return PersistenceMapper.toDomain(model);
    }

    public List<Ubicacion> getAll() {
        List<UbicacionModel> models = this.ubicacionRepository.findAll();
        return PersistenceMapper.toDomain(models);
    }

    public List<Ubicacion> getByListIds(List<Long> ids) {
        List<UbicacionModel> models = this.ubicacionRepository.findByListIds(ids);
        return PersistenceMapper.toDomain(models);
    }

    public Ubicacion save(Ubicacion ubicacion) {
        UbicacionModel model = PersistenceMapper.toModel(ubicacion);
        UbicacionModel saved = this.ubicacionRepository.save(model);
        return PersistenceMapper.toDomain(saved);
    }

    public Ubicacion update(Ubicacion ubicacion) {
        UbicacionModel model = PersistenceMapper.toModel(ubicacion);
        UbicacionModel updated = this.ubicacionRepository.save(model);
        return PersistenceMapper.toDomain(updated);
    }

    public void delete(Long idUbicacion){
        this.ubicacionRepository.deleteById(idUbicacion);
    }

    public Long findByDepositoId(Long idDeposito){
        return this.ubicacionRepository.findUbicacionIdByDepositoId(idDeposito);
    }

    public Ubicacion findUbicacionByDepositoId(Long idDeposito){
        UbicacionModel ubicacion = this.ubicacionRepository.findUbicacionByDepositoId(idDeposito);
        return PersistenceMapper.toDomain(ubicacion);
    }

}

