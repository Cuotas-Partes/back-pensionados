package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Eventos.HistoricoLiquidacionPorCobrar;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoLiquidacionPorCobrarRepositorio
        extends JpaRepository<HistoricoLiquidacionPorCobrar, Long> {

    // buscar por pensionado
    List<HistoricoLiquidacionPorCobrar> findByPensionado(Pensionado pensionado);


}
