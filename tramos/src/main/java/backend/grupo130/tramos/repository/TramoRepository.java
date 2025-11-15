package backend.grupo130.tramos.repository;

import backend.grupo130.tramos.data.models.Tramo;
import backend.grupo130.tramos.data.repository.PostgresTramoRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class TramoRepository {

    private final PostgresTramoRepositoryI tramoRepository;

    public Tramo getById(Integer tramoId){
        Tramo model = this.tramoRepository.findById(tramoId).orElse(null);
        return model;
    }

    public List<Tramo> getAll() {
        List<Tramo> models = this.tramoRepository.findAll();
        return models;
    }

    public Tramo save(Tramo tramo) {
        Tramo saved = this.tramoRepository.save(tramo);
        return saved;
    }

    public List<Tramo> saveAll(List<Tramo> tramos) {
        List<Tramo> saved = this.tramoRepository.saveAll(tramos);
        return saved;
    }

    public Tramo update(Tramo tramo) {
        Tramo updated = this.tramoRepository.save(tramo);
        return updated;
    }

    public List<Tramo> buscarPorRuta(Integer idRuta){
        List<Tramo> tramos = this.tramoRepository.buscarTodosPorIdRuta(idRuta);
        return tramos;
    }

}
