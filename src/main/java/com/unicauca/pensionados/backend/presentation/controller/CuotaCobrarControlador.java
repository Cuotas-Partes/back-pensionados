package com.unicauca.pensionados.backend.presentation.controller;

import com.unicauca.pensionados.backend.application.dto.request.cuota.RegistroCuotaCobrarPeticion;
import com.unicauca.pensionados.backend.application.dto.response.cuota.CuotaCobrarRespuestaDTO;
import com.unicauca.pensionados.backend.application.service.interfaces.ICuotaCobrarServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuota-cobrar")
@CrossOrigin(origins = "*")
public class CuotaCobrarControlador {

    private final ICuotaCobrarServicio cuotaCobrarServicio;

    public CuotaCobrarControlador(ICuotaCobrarServicio cuotaCobrarServicio) {
        this.cuotaCobrarServicio = cuotaCobrarServicio;
    }

    @GetMapping("/listar")
    public ResponseEntity<List<CuotaCobrarRespuestaDTO>> listar() {
        return ResponseEntity.ok(cuotaCobrarServicio.listar());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(cuotaCobrarServicio.obtenerPorId(id));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody RegistroCuotaCobrarPeticion peticion) {
        try {
            CuotaCobrarRespuestaDTO creado = cuotaCobrarServicio.crear(peticion);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody RegistroCuotaCobrarPeticion peticion) {
        try {
            return ResponseEntity.ok(cuotaCobrarServicio.actualizar(id, peticion));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            cuotaCobrarServicio.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
