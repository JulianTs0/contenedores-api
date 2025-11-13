
package backend.grupo130.ubicaciones.controller;

import backend.grupo130.ubicaciones.config.exceptions.ServiceError;
import backend.grupo130.ubicaciones.data.models.Deposito;
import backend.grupo130.ubicaciones.service.DepositoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/depositos")
public class DepositoController {

    private final DepositoService service;

    @Autowired
    public DepositoController(DepositoService service) {
        this.service = service;
    }

    // Obtener todos los depósitos
    @GetMapping
    public ResponseEntity<List<Deposito>> getAll() {
        List<Deposito> depositos = service.findAll();
        return ResponseEntity.ok(depositos);
    }

    // Obtener depósito por ID
    @GetMapping("/{id}")
    public ResponseEntity<Deposito> getById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ServiceError("Depósito no encontrado con id: " + id));
    }

    // Crear un nuevo depósito
    @PostMapping
    public ResponseEntity<Deposito> create(@RequestBody Deposito deposito) {
        Deposito creado = service.create(deposito);
        return ResponseEntity.ok(creado);
    }

    // Actualizar un depósito existente
    @PutMapping("/{id}")
    public ResponseEntity<Deposito> update(@PathVariable Integer id, @RequestBody Deposito cambios) {
        Deposito actualizado = service.update(id, cambios);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar un depósito por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Calcular costo de estadía
    @GetMapping("/{id}/costo")
    public ResponseEntity<Double> calcularCostoEstadia(
            @PathVariable Integer id,
            @RequestParam int dias) {
        double costo = service.calcularCostoEstadia(id, dias);
        return ResponseEntity.ok(costo);
    }
}