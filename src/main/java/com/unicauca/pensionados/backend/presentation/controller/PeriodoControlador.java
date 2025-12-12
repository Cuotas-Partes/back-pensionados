package com.unicauca.pensionados.backend.presentation.controller;

import com.unicauca.pensionados.backend.domain.model.entity.Periodo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.unicauca.pensionados.backend.application.service.interfaces.IPeriodoServicio;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/periodo")
@CrossOrigin(origins = "*")
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