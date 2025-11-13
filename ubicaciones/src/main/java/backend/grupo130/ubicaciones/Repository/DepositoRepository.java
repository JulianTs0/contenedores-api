package backend.grupo130.ubicaciones.Repository;

import backend.grupo130.ubicaciones.data.models.Deposito;
import backend.grupo130.ubicaciones.data.repository.PostgresUbicacionRepositoryI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class DepositoRepository {

    private final PostgresUbicacionRepositoryI depositoRepository;

    public List<Deposito> findAll() {
        return depositoRepository.findAll();
    }

    public Optional<Deposito> findById(Integer id) {
        return depositoRepository.findById(id);
    }

    public Optional<Deposito> findByNombre(String nombre) {
        return depositoRepository.findByNombre(nombre);
    }

    public boolean existsById(Integer id) {
        return depositoRepository.existsById(id);
    }

    public Deposito save(Deposito deposito) {
        return depositoRepository.save(deposito);
    }

    public void deleteById(Integer id) {
        depositoRepository.deleteById(id);
    }
}