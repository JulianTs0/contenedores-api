package backend.grupo130.camiones.data.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "camiones")
@Data
@EqualsAndHashCode(of = "dominio")
public class Camion {

    @Id
    @Column(length = 50)
    private String dominio;

    @Column(name = "transportista_nombre", length = 100, nullable = false)
    private String nombreTransportista;

    @Column(name = "capacidad_peso", nullable = false)
    private Double capacidadPeso;

    @Column(name = "capacidad_volumen", nullable = false)
    private Double capacidadVolumen;

    @Column(name = "consumo_km", nullable = false)
    private Double consumoKm;

    @Column(name = "disponibilidad", nullable = false)
    private Boolean disponible;

    @Column(name = "telefono_contacto", length = 30)
    private String telefonoContacto;

    @Column(name = "costo_km", nullable = false)
    private Double costoKm;
    // ✅ NUEVO CAMPO
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
}
