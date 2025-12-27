package backend.grupo130.contenedores.repository;

import backend.grupo130.contenedores.config.enums.EstadoContenedor;
import backend.grupo130.contenedores.data.PersistenceMapper;
import backend.grupo130.contenedores.data.entity.Contenedor;
import backend.grupo130.contenedores.data.models.ContenedorModel;
import backend.grupo130.contenedores.data.repository.PostgresContenedorRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@AllArgsConstructor
public class ContenedorRepository {

    private final PostgresContenedorRepositoryI contenedorRepository;

    public Contenedor getById(Long contenedorId){
        ContenedorModel model = this.contenedorRepository.findById(contenedorId).orElse(null);
        return PersistenceMapper.toDomain(model);
    }

    public List<Contenedor> getAll() {
        List<ContenedorModel> models = this.contenedorRepository.findAll();
        return PersistenceMapper.toDomain(models);
    }

    public List<Contenedor> findByEstado(EstadoContenedor estadoContenedor) {
        List<ContenedorModel> models = this.contenedorRepository.findByEstado(estadoContenedor);
        return PersistenceMapper.toDomain(models);
    }

    public Contenedor save(Contenedor contenedor) {
        ContenedorModel model = PersistenceMapper.toModel(contenedor);
        ContenedorModel saved = this.contenedorRepository.save(model);
        return PersistenceMapper.toDomain(saved);
    }

    public Contenedor update(Contenedor contenedor) {
        ContenedorModel model = PersistenceMapper.toModel(contenedor);
        ContenedorModel updated = this.contenedorRepository.save(model);
        return PersistenceMapper.toDomain(updated);
    }

    public void delete(Long contenedorId){
        this.contenedorRepository.deleteById(contenedorId);
    }

    public Contenedor findByPesoVolumen(BigDecimal peso, BigDecimal volumen){
        ContenedorModel contenedor = this.contenedorRepository.findFirstByPesoAndVolumenAndIdClienteIsNull(peso, volumen);
        return PersistenceMapper.toDomain(contenedor);
    }

}
