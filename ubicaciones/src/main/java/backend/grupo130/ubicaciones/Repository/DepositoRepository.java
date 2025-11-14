package backend.grupo130.ubicaciones.Repository;

import backend.grupo130.ubicaciones.data.models.Deposito;
import backend.grupo130.ubicaciones.data.models.Ubicacion;
import backend.grupo130.ubicaciones.data.repository.PostgresDepositoRepositoryI;
import backend.grupo130.ubicaciones.data.repository.PostgresUbicacionRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class DepositoRepository {

    private final PostgresDepositoRepositoryI depositoRepositoryI;

    public Deposito getById(Integer depositoId){
        Deposito model = this.depositoRepositoryI.findById(depositoId).orElse(null);
        return model;
    }

    public List<Deposito> getAll() {
        List<Deposito> models = this.depositoRepositoryI.findAll();
        return models;
    }

    public Deposito save(Deposito deposito) {
        Deposito saved = this.depositoRepositoryI.save(deposito);
        return saved;
    }

    public Deposito update(Deposito deposito) {
        Deposito updated = this.depositoRepositoryI.save(deposito);
        return updated;
    }

}
