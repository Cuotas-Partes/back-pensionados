package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Deuda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

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
}
