package backend.grupo130.contenedores.data.models;

import backend.grupo130.contenedores.config.enums.EstadoContenedor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "contenedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContenedorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contenedor")
    private Long idContenedor;

    @Column(name = "peso", precision = 10, scale = 2, nullable = false)
    private BigDecimal peso;

    @Column(name = "volumen", precision = 10, scale = 2, nullable = false)
    private BigDecimal volumen;

    @Column(name = "id_cliente")
    private Long idCliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoContenedor estado;

}
