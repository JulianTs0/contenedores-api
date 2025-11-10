package backend.grupo130.camiones.controller;

import backend.grupo130.camiones.data.models.Camion;
import backend.grupo130.camiones.service.CamionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/camiones")
public class CamionController {

    private final CamionService service;

    @Autowired
    public CamionController(CamionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Camion>> listarTodos() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Camion>> listarDisponibles() {
        return ResponseEntity.ok(service.findAvailable());
    }

    @GetMapping("/{dominio}")
    public ResponseEntity<Camion> obtenerPorId(@PathVariable String dominio) {
        return service.findById(dominio)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Camion> crearCamion(@Valid @RequestBody Camion nuevoCamion) {
        Camion saved = service.save(nuevoCamion);
        return ResponseEntity.created(URI.create("/api/v1/camiones/" + saved.getDominio())).body(saved);
    }

    @PutMapping("/{dominio}")
    public ResponseEntity<Camion> actualizarCamion(@PathVariable String dominio, @Valid @RequestBody Camion detalles) {
        return service.findById(dominio)
                .map(existing -> {
                    existing.setNombreTransportista(detalles.getNombreTransportista());
                    existing.setCapacidadPeso(detalles.getCapacidadPeso());
                    existing.setCapacidadVolumen(detalles.getCapacidadVolumen());
                    existing.setConsumoKm(detalles.getConsumoKm());
                    existing.setDisponible(detalles.getDisponible());
                    existing.setTelefonoContacto(detalles.getTelefonoContacto());
                    existing.setCostoKm(detalles.getCostoKm());
                    Camion updated = service.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{dominio}")
    public ResponseEntity<Void> eliminarCamion(@PathVariable String dominio) {
        return service.findById(dominio)
                .map(c -> {
                    service.deleteById(dominio);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
