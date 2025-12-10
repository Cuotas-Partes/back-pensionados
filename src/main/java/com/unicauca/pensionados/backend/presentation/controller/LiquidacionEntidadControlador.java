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
import org.springframework.web.bind.annotation.RestController;

import com.unicauca.pensionados.backend.application.service.ILiquidacionEntidadServicio;
import com.unicauca.pensionados.backend.application.dto.request.LiquidacionEntidadDTOPeticion;
import com.unicauca.pensionados.backend.application.dto.response.LiquidacionEntidadDTORespuesta;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/liquidaciones-entidad")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Liquidaciones Entidad", description = "API para gestionar liquidaciones de entidades")
public class LiquidacionEntidadControlador {

    private final ILiquidacionEntidadServicio liquidacionServicio;

    @PostMapping
    @Operation(summary = "Crear una nueva liquidación de entidad")
    public ResponseEntity<LiquidacionEntidadDTORespuesta> crear(@Valid @RequestBody LiquidacionEntidadDTOPeticion peticion) {
        LiquidacionEntidadDTORespuesta respuesta = liquidacionServicio.crear(peticion);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una liquidación de entidad")
    public ResponseEntity<LiquidacionEntidadDTORespuesta> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody LiquidacionEntidadDTOPeticion peticion) {
        LiquidacionEntidadDTORespuesta respuesta = liquidacionServicio.actualizar(id, peticion);
        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una liquidación")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        liquidacionServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener liquidación por ID")
    public ResponseEntity<LiquidacionEntidadDTORespuesta> obtenerPorId(@PathVariable Long id) {
        LiquidacionEntidadDTORespuesta respuesta = liquidacionServicio.obtenerPorId(id);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping
    @Operation(summary = "Obtener todas las liquidaciones")
    public ResponseEntity<List<LiquidacionEntidadDTORespuesta>> obtenerTodas() {
        List<LiquidacionEntidadDTORespuesta> liquidaciones = liquidacionServicio.obtenerTodas();
        return ResponseEntity.ok(liquidaciones);
    }

    @GetMapping("/entidad/{nitEntidad}")
    @Operation(summary = "Obtener liquidaciones por entidad")
    public ResponseEntity<List<LiquidacionEntidadDTORespuesta>> obtenerPorEntidad(@PathVariable Long nitEntidad) {
        List<LiquidacionEntidadDTORespuesta> liquidaciones = liquidacionServicio.obtenerPorEntidad(nitEntidad);
        return ResponseEntity.ok(liquidaciones);
    }

    @GetMapping("/periodo/{periodoId}")
    @Operation(summary = "Obtener liquidaciones por periodo")
    public ResponseEntity<List<LiquidacionEntidadDTORespuesta>> obtenerPorPeriodo(@PathVariable Long periodoId) {
        List<LiquidacionEntidadDTORespuesta> liquidaciones = liquidacionServicio.obtenerPorPeriodo(periodoId);
        return ResponseEntity.ok(liquidaciones);
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener liquidaciones por estado")
    public ResponseEntity<List<LiquidacionEntidadDTORespuesta>> obtenerPorEstado(@PathVariable String estado) {
        List<LiquidacionEntidadDTORespuesta> liquidaciones = liquidacionServicio.obtenerPorEstado(estado);
        return ResponseEntity.ok(liquidaciones);
    }
}
