package backend.grupo130.envios.data.models;

import backend.grupo130.envios.config.enums.Estado;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "seguimiento_envio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeguimientoEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_seguimiento")
    private Long idSeguimiento;

    @Column(name = "fecha_hora_inicio", nullable = false)
    private LocalDateTime fechaHoraInicio;

    @Column(name = "fecha_hora_fin")
    private LocalDateTime fechaHoraFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado estado;

    @Column(columnDefinition = "text", name = "descripcion")
    private String descripcion;

}
