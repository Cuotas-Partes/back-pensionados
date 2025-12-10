package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import com.unicauca.pensionados.backend.domain.model.entity.HistoricoLiquidacionPorCobrar;
import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoLiquidacionPorCobrarRepositorio
        extends JpaRepository<HistoricoLiquidacionPorCobrar, Long> {

    // buscar por pensionado
    List<HistoricoLiquidacionPorCobrar> findByPensionado(Pensionado pensionado);


}
