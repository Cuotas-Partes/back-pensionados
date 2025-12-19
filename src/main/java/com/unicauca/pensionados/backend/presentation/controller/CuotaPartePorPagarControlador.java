package com.unicauca.pensionados.backend.presentation.controller;

import com.unicauca.pensionados.backend.application.service.CuotaPartePorPagarServicio;
import com.unicauca.pensionados.backend.domain.model.entity.CuotaPartePorPagar;
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
 * Controlador REST para gestionar las cuotas partes por pagar
 */
@RestController
@RequestMapping("/api/cuotas-partes/por-pagar")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Cuotas Partes Por Pagar", description = "API para gestionar las cuotas partes que la entidad debe pagar a otras instituciones")
public class CuotaPartePorPagarControlador {

    private final CuotaPartePorPagarServicio cuotaPartePorPagarServicio;

    @PostMapping
    @Operation(summary = "Crear una nueva cuota parte por pagar")
    public ResponseEntity<CuotaPartePorPagar> crear(@Valid @RequestBody CuotaPartePorPagar cuotaParte) {
        CuotaPartePorPagar nuevaCuotaParte = cuotaPartePorPagarServicio.crear(cuotaParte);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCuotaParte);
    }

    @GetMapping
    @Operation(summary = "Obtener todas las cuotas partes por pagar")
    public ResponseEntity<List<CuotaPartePorPagar>> obtenerTodas() {
        List<CuotaPartePorPagar> cuotasPartes = cuotaPartePorPagarServicio.buscarTodas();
        return ResponseEntity.ok(cuotasPartes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cuota parte por pagar por ID")
    public ResponseEntity<CuotaPartePorPagar> obtenerPorId(@PathVariable Long id) {
        CuotaPartePorPagar cuotaParte = cuotaPartePorPagarServicio.buscarPorId(id);
        return ResponseEntity.ok(cuotaParte);
    }

    @GetMapping("/pensionado/{idPensionado}")
    @Operation(summary = "Obtener cuotas partes por pagar de un pensionado")
    public ResponseEntity<List<CuotaPartePorPagar>> obtenerPorPensionado(@PathVariable Long idPensionado) {
        List<CuotaPartePorPagar> cuotasPartes = cuotaPartePorPagarServicio.buscarPorPensionado(idPensionado);
        return ResponseEntity.ok(cuotasPartes);
    }

    @GetMapping("/entidad-acreedora/{idEntidad}")
    @Operation(summary = "Obtener cuotas partes por pagar a una entidad acreedora")
    public ResponseEntity<List<CuotaPartePorPagar>> obtenerPorEntidadAcreedora(@PathVariable Long idEntidad) {
        List<CuotaPartePorPagar> cuotasPartes = cuotaPartePorPagarServicio.buscarPorEntidadAcreedora(idEntidad);
        return ResponseEntity.ok(cuotasPartes);
    }

    @GetMapping("/periodo/{anio}/{mes}")
    @Operation(summary = "Obtener cuotas partes por pagar de un periodo específico")
    public ResponseEntity<List<CuotaPartePorPagar>> obtenerPorPeriodo(
            @PathVariable Integer anio,
            @PathVariable Integer mes) {
        List<CuotaPartePorPagar> cuotasPartes = cuotaPartePorPagarServicio.buscarPorPeriodo(anio, mes);
        return ResponseEntity.ok(cuotasPartes);
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener cuotas partes por pagar por estado")
    public ResponseEntity<List<CuotaPartePorPagar>> obtenerPorEstado(@PathVariable EstadoCuotaParte estado) {
        List<CuotaPartePorPagar> cuotasPartes = cuotaPartePorPagarServicio.buscarPorEstado(estado);
        return ResponseEntity.ok(cuotasPartes);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una cuota parte por pagar")
    public ResponseEntity<CuotaPartePorPagar> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CuotaPartePorPagar cuotaParte) {
        CuotaPartePorPagar cuotaParteActualizada = cuotaPartePorPagarServicio.actualizar(id, cuotaParte);
        return ResponseEntity.ok(cuotaParteActualizada);
    }

    @PatchMapping("/{id}/marcar-pagada")
    @Operation(summary = "Marcar una cuota parte como pagada")
    public ResponseEntity<CuotaPartePorPagar> marcarComoPagada(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaPago) {
        CuotaPartePorPagar cuotaParte = cuotaPartePorPagarServicio.marcarComoPagada(id, fechaPago);
        return ResponseEntity.ok(cuotaParte);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una cuota parte por pagar")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cuotaPartePorPagarServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

