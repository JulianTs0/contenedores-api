package backend.grupo130.contenedores.data.models;

import backend.grupo130.contenedores.client.usuarios.models.Usuario;
import backend.grupo130.contenedores.config.enums.Estado;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "contenedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contenedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contenedor")
    private Integer idContenedor;

    @Column(name = "peso", precision = 10, scale = 2, nullable = false)
    private BigDecimal peso;

    @Column(name = "volumen", precision = 10, scale = 2, nullable = false)
    private BigDecimal volumen;

    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "Estado", nullable = false)
    private Estado estado;

    @Transient
    private Usuario usuario;

}
