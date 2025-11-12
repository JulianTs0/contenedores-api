package backend.grupo130.tramos.repository;

import backend.grupo130.tramos.data.models.RutaTraslado;
import backend.grupo130.tramos.data.models.Tramo;
import backend.grupo130.tramos.data.repository.PostgresRutaRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class RutaRepository {

    private final PostgresRutaRepositoryI rutaRepository;

    public RutaTraslado getById(Integer rutaId){
        RutaTraslado model = this.rutaRepository.findByIdWithTramos(rutaId);
        return model;
    }

    public List<RutaTraslado> getAll() {
        List<RutaTraslado> models = this.rutaRepository.findWithTramos();
        return models;
    }

    public RutaTraslado save(RutaTraslado ruta) {
        RutaTraslado saved = this.rutaRepository.save(ruta);
        return saved;
    }

    public RutaTraslado update(RutaTraslado ruta) {
        RutaTraslado updated = this.rutaRepository.save(ruta);
        return updated;
    }

}
