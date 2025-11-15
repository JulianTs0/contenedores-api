package backend.grupo130.contenedores.repository;

import backend.grupo130.contenedores.config.enums.Estado;
import backend.grupo130.contenedores.data.models.Contenedor;
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
        Contenedor model = this.contenedorRepository.findById(contenedorId).orElse(null);
        return model;
    }

    public List<Contenedor> getAll() {
        List<Contenedor> models = this.contenedorRepository.findAll();
        return models;
    }

    public List<Contenedor> findByEstado(Estado estado) {
        List<Contenedor> models = this.contenedorRepository.findByEstado(estado);
        return models;
    }

    public Contenedor save(Contenedor contenedor) {
        Contenedor saved = this.contenedorRepository.save(contenedor);
        return saved;
    }

    public Contenedor update(Contenedor contenedor) {
        Contenedor updated = this.contenedorRepository.save(contenedor);
        return updated;
    }

    public void delete(Long contenedorId){
        this.contenedorRepository.deleteById(contenedorId);
        return;
    }

    public Contenedor findByPesoVolumen(BigDecimal peso, BigDecimal volumen){
        Contenedor contenedor = this.contenedorRepository.findFirstByPesoAndVolumen(peso, volumen);
        return contenedor;
    }

}
