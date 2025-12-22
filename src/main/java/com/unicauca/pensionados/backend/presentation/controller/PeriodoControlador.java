package com.unicauca.pensionados.backend.presentation.controller;

import com.unicauca.pensionados.backend.application.dto.request.periodo.RegistroPeriodoPeticion;
import com.unicauca.pensionados.backend.application.dto.response.PeriodoRespuesta;
import com.unicauca.pensionados.backend.application.service.interfaces.IPeriodoServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/periodo")
@CrossOrigin(origins = "*")
public class PeriodoControlador {

    private final IPeriodoServicio periodoServicio;

    public PeriodoControlador(IPeriodoServicio periodoServicio) {
        this.periodoServicio = periodoServicio;
    }

    @GetMapping("/listar")
    public ResponseEntity<List<PeriodoRespuesta>> listar() {
        return ResponseEntity.ok(periodoServicio.listar());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(periodoServicio.obtenerPorId(id));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody RegistroPeriodoPeticion peticion) {
        try {
            PeriodoRespuesta creado = periodoServicio.crear(peticion);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody RegistroPeriodoPeticion peticion) {
        try {
            return ResponseEntity.ok(periodoServicio.actualizar(id, peticion));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            periodoServicio.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
