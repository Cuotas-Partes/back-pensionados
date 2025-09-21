package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Histórico de los cambios realizados por un usuario.
 * Registra qué entidad se modificó, la acción ejecutada, los valores antes/después
 * y la fecha junto con el usuario actor.
 */
@Entity
@Table(name = "log_cambio",
        indexes = {
                @Index(name="idx_hist_usuario", columnList="usuario_id"),
                @Index(name="idx_hist_fecha",   columnList="fecha_evento")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogCambio {

    /** Identificador del registro de log (PK autoincremental). */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre de la entidad afectada. Ej.: "Pensionado", "CuotaParte". */
    @Column(nullable = false, length = 80)
    private String entidad;

    /** Acción ejecutada. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Accion accion;

    /** JSON con los datos antes del cambio (puede ser null en CREAR). */
    @Column(name = "valor_anterior", columnDefinition = "JSON")
    private String valorAnterior;

    /** JSON con los datos después del cambio. */
    @Column(name = "valor_nuevo", columnDefinition = "JSON")
    private String valorNuevo;

    /** Fecha y hora en que se registró el cambio. */
    @Column(nullable = false)
    private LocalDateTime fecha;

    /** Usuario que ejecutó la acción. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** Asigna la fecha automáticamente del evento. */
    @PrePersist
    public void prePersist() {
        if (this.fecha == null) this.fecha = LocalDateTime.now();
    }

    /** Enumeración de acciones registrables en el log. */
    public enum Accion { CREAR, ACTUALIZAR, ELIMINAR, CONSULTAR }
}
