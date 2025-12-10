package com.unicauca.pensionados.backend.presentation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unicauca.pensionados.backend.application.service.IResolucionServicio;
import com.unicauca.pensionados.backend.application.dto.request.ResolucionDTOPeticion;
import com.unicauca.pensionados.backend.application.dto.response.ResolucionDTORespuesta;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/resoluciones")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Resoluciones", description = "API para gestionar resoluciones de pensión")
public class ResolucionControlador {

    private final IResolucionServicio resolucionServicio;

    @PostMapping
    @Operation(summary = "Crear una nueva resolución")
    public ResponseEntity<ResolucionDTORespuesta> crear(@Valid @RequestBody ResolucionDTOPeticion peticion) {
        ResolucionDTORespuesta respuesta = resolucionServicio.crear(peticion);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una resolución existente")
    public ResponseEntity<ResolucionDTORespuesta> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ResolucionDTOPeticion peticion) {
        ResolucionDTORespuesta respuesta = resolucionServicio.actualizar(id, peticion);
        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una resolución")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        resolucionServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una resolución por ID")
    public ResponseEntity<ResolucionDTORespuesta> obtenerPorId(@PathVariable Long id) {
        ResolucionDTORespuesta respuesta = resolucionServicio.obtenerPorId(id);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping
    @Operation(summary = "Obtener todas las resoluciones")
    public ResponseEntity<List<ResolucionDTORespuesta>> obtenerTodas() {
        List<ResolucionDTORespuesta> resoluciones = resolucionServicio.obtenerTodas();
        return ResponseEntity.ok(resoluciones);
    }

    @GetMapping("/pensionado/{pensionadoId}")
    @Operation(summary = "Obtener resoluciones por pensionado")
    public ResponseEntity<List<ResolucionDTORespuesta>> obtenerPorPensionado(@PathVariable Long pensionadoId) {
        List<ResolucionDTORespuesta> resoluciones = resolucionServicio.obtenerPorPensionado(pensionadoId);
        return ResponseEntity.ok(resoluciones);
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener resoluciones por estado")
    public ResponseEntity<List<ResolucionDTORespuesta>> obtenerPorEstado(@PathVariable String estado) {
        List<ResolucionDTORespuesta> resoluciones = resolucionServicio.obtenerPorEstado(estado);
        return ResponseEntity.ok(resoluciones);
    }

    @GetMapping("/numero")
    @Operation(summary = "Obtener resolución por número")
    public ResponseEntity<ResolucionDTORespuesta> obtenerPorNumero(@RequestParam String numeroResolucion) {
        ResolucionDTORespuesta respuesta = resolucionServicio.obtenerPorNumero(numeroResolucion);
        return ResponseEntity.ok(respuesta);
    }
}
