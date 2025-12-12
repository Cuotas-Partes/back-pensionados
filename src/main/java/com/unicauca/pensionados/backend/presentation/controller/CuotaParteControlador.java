package com.unicauca.pensionados.backend.presentation.controller;

import org.springframework.web.bind.annotation.*;

import com.unicauca.pensionados.backend.application.service.CuotaParteServicio;
import com.unicauca.pensionados.backend.application.dto.request.FiltroCuotaPartePeticion;
import com.unicauca.pensionados.backend.application.dto.response.EntidadValorCuotaParteDTO;
import com.unicauca.pensionados.backend.application.dto.response.ResultadoCobroPorPensionado;
import com.unicauca.pensionados.backend.application.dto.response.ResultadoCobroPorPeriodoDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/cuotaParte")
@CrossOrigin(origins = "*")
@Tag(name = "Cuota Parte", description = "Controlador para gestionar las cuotas parte de los pensionados")
public class CuotaParteControlador {
    private final CuotaParteServicio cuotaParteServicio;
    private static final Logger logger = LoggerFactory.getLogger(CuotaParteControlador.class);

    public CuotaParteControlador(CuotaParteServicio cuotaParteServicio) {
        this.cuotaParteServicio = cuotaParteServicio;
    }

    @PostMapping("/liquidacion-por-cobrar/filtrar-por-periodo")
    @Operation(summary = "Filtrar cuotas parte por periodo",
            description = "Permite filtrar las cuotas parte de los pensionados jubilados en unicauca por un rango de fechas específico.")
    @ApiResponse(
            responseCode = "200",
            description = "Cuotas parte filtradas correctamente",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ResultadoCobroPorPeriodoDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Solicitud incorrecta, verifique los parámetros enviados",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json"
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor, por favor intente más tarde",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json"
            )
    )
    public ResultadoCobroPorPeriodoDTO filtrar(@RequestBody FiltroCuotaPartePeticion filtro) {
        return cuotaParteServicio.obtenerCobroPorPeriodo(filtro);
    }

    @GetMapping("/liquidacion-por-cobrar/cobro-pensionado")
    @Operation(summary = "Obtener cuotas parte por cobrar de pensionados",
            description = "Obtiene las cuotas parte por cobrar de los pensionados jubilados en unicauca.")
    @ApiResponse(
            responseCode = "200",
            description = "Cuotas parte obtenidas correctamente",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ResultadoCobroPorPensionado.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor, por favor intente más tarde",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json"
            )
    )
    public ResultadoCobroPorPensionado obtenerCuotasParteUnicauca() {
        return cuotaParteServicio.cuotasPartesPorCobrarPensionado();
    }


    @PostMapping("liquidacion-por-cobrar/cobro-pensionado/{idPensionado}/entidades-por-pensionado-y-periodo")
    @Operation(summary = "Obtener entidades y valores por pensionado en un periodo",
            description = "Obtiene las entidades y el valor de la cuota parte a cobrar para un pensionado específico en un rango de fechas.")
    @ApiResponse(
            responseCode = "200",
            description = "Entidades y valores obtenidos correctamente",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = EntidadValorCuotaParteDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Solicitud incorrecta, verifique los parámetros enviados",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json"
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor, por favor intente más tarde",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json"
            )
    )
    public ResponseEntity<?> obtenerEntidadesPorPensionadoYPeriodo(@RequestBody FiltroCuotaPartePeticion filtro,@PathVariable Long idPensionado) {
        try {
            List<EntidadValorCuotaParteDTO> resultado = cuotaParteServicio
                    .listarEntidadesYValorPorPensionadoYRango(idPensionado, filtro);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
        }
    }
}