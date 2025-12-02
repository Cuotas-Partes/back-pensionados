package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Periodo;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PeriodoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.IPeriodoServicio;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/periodo")
public class PeriodoControlador {

    @Autowired
    private IPeriodoServicio periodoServicio;

    @GetMapping("/existe")
    public ResponseEntity<?> existePeriodo(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin
    ) {
        Map<String, String> response = new HashMap<>();
        LocalDate inicio = LocalDate.parse(fechaInicio);
        LocalDate fin = LocalDate.parse(fechaFin);
        Periodo periodo = periodoServicio.findPeriodoByFechas(inicio, fin);

        if(periodo == null) return ResponseEntity.notFound().build();

        response.put("status", "success");
        response.put("message", "El periodo existe");
        return ResponseEntity.ok(response);

    }
    


}