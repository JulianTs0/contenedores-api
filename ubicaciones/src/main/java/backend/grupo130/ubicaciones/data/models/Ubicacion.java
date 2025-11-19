package backend.grupo130.ubicaciones.data.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "Ubicacion")
@Getter
@Setter
@NoArgsConstructor
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubicacion")
    private Long idUbicacion;

    @Column(name = "direccion_textual", nullable = false, length = 60)
    private String direccionTextual;

    @Column(name = "latitud", nullable = false, precision = 10, scale = 4)
    private BigDecimal latitud;

    @Column(name = "longitud", nullable = false, precision = 10, scale = 4)
    private BigDecimal longitud;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_deposito", referencedColumnName = "id_deposito", unique = true)
    private Deposito deposito;

}
