package com.unicauca.pensionados.backend.presentation.controller;

import com.unicauca.pensionados.backend.application.service.CuotaPartePorCobrarServicio;
import com.unicauca.pensionados.backend.domain.model.entity.CuotaPartePorCobrar;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoCuotaParte;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para gestionar las cuotas partes por cobrar
 */
@RestController
@RequestMapping("/api/cuotas-partes/por-cobrar")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Cuotas Partes Por Cobrar", description = "API para gestionar las cuotas partes que otras entidades deben pagar")
public class CuotaPartePorCobrarControlador {

    private final CuotaPartePorCobrarServicio cuotaPartePorCobrarServicio;

    @PostMapping
    @Operation(summary = "Crear una nueva cuota parte por cobrar")
    public ResponseEntity<CuotaPartePorCobrar> crear(@Valid @RequestBody CuotaPartePorCobrar cuotaParte) {
        CuotaPartePorCobrar nuevaCuotaParte = cuotaPartePorCobrarServicio.crear(cuotaParte);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCuotaParte);
    }

    @GetMapping
    @Operation(summary = "Obtener todas las cuotas partes por cobrar")
    public ResponseEntity<List<CuotaPartePorCobrar>> obtenerTodas() {
        List<CuotaPartePorCobrar> cuotasPartes = cuotaPartePorCobrarServicio.buscarTodas();
        return ResponseEntity.ok(cuotasPartes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cuota parte por cobrar por ID")
    public ResponseEntity<CuotaPartePorCobrar> obtenerPorId(@PathVariable Long id) {
        CuotaPartePorCobrar cuotaParte = cuotaPartePorCobrarServicio.buscarPorId(id);
        return ResponseEntity.ok(cuotaParte);
    }

    @GetMapping("/pensionado/{idPensionado}")
    @Operation(summary = "Obtener cuotas partes por cobrar de un pensionado")
    public ResponseEntity<List<CuotaPartePorCobrar>> obtenerPorPensionado(@PathVariable Long idPensionado) {
        List<CuotaPartePorCobrar> cuotasPartes = cuotaPartePorCobrarServicio.buscarPorPensionado(idPensionado);
        return ResponseEntity.ok(cuotasPartes);
    }

    @GetMapping("/entidad-deudora/{idEntidad}")
    @Operation(summary = "Obtener cuotas partes por cobrar de una entidad deudora")
    public ResponseEntity<List<CuotaPartePorCobrar>> obtenerPorEntidadDeudora(@PathVariable Long idEntidad) {
        List<CuotaPartePorCobrar> cuotasPartes = cuotaPartePorCobrarServicio.buscarPorEntidadDeudora(idEntidad);
        return ResponseEntity.ok(cuotasPartes);
    }

    @GetMapping("/periodo/{anio}/{mes}")
    @Operation(summary = "Obtener cuotas partes por cobrar de un periodo específico")
    public ResponseEntity<List<CuotaPartePorCobrar>> obtenerPorPeriodo(
            @PathVariable Integer anio,
            @PathVariable Integer mes) {
        List<CuotaPartePorCobrar> cuotasPartes = cuotaPartePorCobrarServicio.buscarPorPeriodo(anio, mes);
        return ResponseEntity.ok(cuotasPartes);
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener cuotas partes por cobrar por estado")
    public ResponseEntity<List<CuotaPartePorCobrar>> obtenerPorEstado(@PathVariable EstadoCuotaParte estado) {
        List<CuotaPartePorCobrar> cuotasPartes = cuotaPartePorCobrarServicio.buscarPorEstado(estado);
        return ResponseEntity.ok(cuotasPartes);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una cuota parte por cobrar")
    public ResponseEntity<CuotaPartePorCobrar> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CuotaPartePorCobrar cuotaParte) {
        CuotaPartePorCobrar cuotaParteActualizada = cuotaPartePorCobrarServicio.actualizar(id, cuotaParte);
        return ResponseEntity.ok(cuotaParteActualizada);
    }

    @PatchMapping("/{id}/marcar-pagada")
    @Operation(summary = "Marcar una cuota parte como pagada")
    public ResponseEntity<CuotaPartePorCobrar> marcarComoPagada(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaPago) {
        CuotaPartePorCobrar cuotaParte = cuotaPartePorCobrarServicio.marcarComoPagada(id, fechaPago);
        return ResponseEntity.ok(cuotaParte);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una cuota parte por cobrar")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cuotaPartePorCobrarServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

