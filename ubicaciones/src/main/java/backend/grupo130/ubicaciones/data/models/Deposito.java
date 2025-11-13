
package backend.grupo130.ubicaciones.data.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "depositos")
@Data
@EqualsAndHashCode(of = "id")
public class Deposito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 150)
    @NotNull
    private String nombre;

    @Column(name = "direccion", nullable = false, length = 300)
    @NotNull
    private String direccion;

    @Column(name = "latitud", nullable = false)
    @NotNull
    private Double latitud;

    @Column(name = "longitud", nullable = false)
    @NotNull
    private Double longitud;

    @Column(name = "costo_estadia_diario", nullable = false)
    @NotNull
    private Double costoEstadiaDiario;

    @Column(name = "observaciones", length = 500)
    private String observaciones;
}
