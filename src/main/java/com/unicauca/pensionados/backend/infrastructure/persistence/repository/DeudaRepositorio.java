package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeudaRepositorio extends JpaRepository<Deuda, Long> {

    @Query(
        """
            SELECT d
            FROM Deuda d
            WHERE (:tipoDeuda IS NULL OR d.tipoDeuda = :tipoDeuda)
            AND (:estadoDeuda IS NULL OR d.estadoDeuda = :estadoDeuda)
            AND (:idPersona IS NULL OR d.persona.id = :idPersona)
        """
    )
    List<Deuda> findByTipoEstadoPersona(@Param("tipoDeuda") Deuda.TipoDeuda tipoDeuda, @Param("estadoDeuda") Deuda.EstadoDeuda estadoDeuda, @Param("idPersona") Long idPersona);

    @Query(
            """
                SELECT d
                FROM Deuda d
                WHERE d.fechaVencimiento = :fechaVencimiento
            """
    )
    Optional<Deuda> findByFechaVencimiento(@Param("fechaVencimiento") String fechaVencimiento);
}
