package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.ISucesorServicio;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroSucesorPeticion;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/sucesor")
@Tag(name = "Sucesor", description = "APIs para la gestión de sucesores")
public class SucesorControlador {
    @Autowired
    private ISucesorServicio servicioSucesor;

    @PostMapping("/registrar")
    @Operation(summary = "Registrar un sucesor", description = "Registra un nuevo sucesor en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesor registrado exitosamente",
            content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Error al registrar sucesor",
            content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> registrarSucesor(@RequestBody RegistroSucesorPeticion peticion) {
        try {
            servicioSucesor.registrarSucesor(peticion);
            return ResponseEntity.ok("Sucesor registrado exitosamente");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al registrar sucesor: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + ex.getMessage());
        }
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar sucesores", description = "Obtiene la lista de todos los sucesores registrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de sucesores obtenida exitosamente",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Error al listar sucesores",
            content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> listarSucesores() {
        try {
            return ResponseEntity.ok(servicioSucesor.listaSucesores());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al listar sucesores: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + ex.getMessage());
        }
    }

    @GetMapping("/buscarPorId/{id}")
    @Operation(summary = "Buscar sucesor por ID", description = "Busca un sucesor específico utilizando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesor encontrado",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Error al buscar sucesor",
            content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> buscarSucesorPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(servicioSucesor.obtenerSucesorPorId(id));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al buscar sucesor: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + ex.getMessage());
        }
    }
    
    @PutMapping("/editar/{id}")
    @Operation(summary = "Editar sucesor", description = "Edita los datos de un sucesor existente identificado por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesor actualizado exitosamente",
            content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Error al actualizar sucesor",
            content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> editarSucesor(
        @io.swagger.v3.oas.annotations.Parameter(description = "ID del sucesor a editar", required = true, example = "12345")
        @PathVariable Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del sucesor", required = true,
            content = @Content(schema = @Schema(implementation = RegistroSucesorPeticion.class)))
        @RequestBody RegistroSucesorPeticion sucesor) {
        try {
            servicioSucesor.editarSucesor(id, sucesor);
            return ResponseEntity.ok("Sucesor actualizado exitosamente");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Error al actualizar sucesor: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error interno del servidor: " + ex.getMessage());
        }
    }
    
    @DeleteMapping("/eliminar/{id}")
    @Operation(summary = "Eliminar sucesor", description = "Elimina un sucesor del sistema utilizando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucesor eliminado exitosamente",
            content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Error al eliminar sucesor",
            content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> eliminarSucesor(
        @io.swagger.v3.oas.annotations.Parameter(description = "ID del sucesor a eliminar", required = true, example = "12345")
        @PathVariable Long id) {
        try {
            servicioSucesor.eliminarSucesor(id);
            return ResponseEntity.ok("Sucesor eliminado exitosamente");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Error al eliminar sucesor: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error interno del servidor: " + ex.getMessage());
        }
    }
}
