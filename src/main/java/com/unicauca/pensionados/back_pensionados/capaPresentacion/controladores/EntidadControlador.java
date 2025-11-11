package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.IEntidadServicio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroEntidadPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroTrabajoPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.EntidadConPensionadosRespuesta;

import java.util.List;

@RestController
@RequestMapping("/entidad")
@Tag(name = "Entidades", description = "Gestión de entidades pensionadas")
public class EntidadControlador {

    @Autowired
    private IEntidadServicio entidadService;

    @PostMapping("/registrar")
    @Operation(summary = "Registrar una nueva entidad", description = "Permite registrar una nueva entidad en el sistema.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Entidad registrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
    public ResponseEntity<?> registrarEntidad(@RequestBody RegistroEntidadPeticion entidad) {
        try{
            entidadService.registrarEntidad(entidad);
            return ResponseEntity.ok("Entidad registrada exitosamente");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Error al registrar entidad: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error interno del servidor: " + ex.getMessage());
        }
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar entidades por criterio", description = "Busca entidades según un término de búsqueda.", 
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de entidades encontradas", 
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = EntidadConPensionadosRespuesta.class))),
            @ApiResponse(responseCode = "204", description = "No se encontraron entidades"),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
    public List<EntidadConPensionadosRespuesta> buscarPorCriterio(
        @Parameter(description = "Término de búsqueda (opcional)", example = "Unicauca") 
        @RequestParam(required = false) String query) {
        if (query == null || query.trim().isEmpty()) {
            return entidadService.listarTodos();
        }
        query = query.replace("\"", "").trim(); // elimina comillas
        return entidadService.buscarEntidadesPorCriterio(query);
    }

    @GetMapping("/buscarPorNombre")
    @Operation(summary = "Buscar entidades por nombre", description = "Devuelve todas las entidades cuyo nombre coincida parcialmente.", 
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de entidades encontradas", 
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = Entidad.class))),
            @ApiResponse(responseCode = "204", description = "No se encontraron entidades"),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
    public ResponseEntity<List<Entidad>> buscarPorNombre(
        @Parameter(description = "Nombre de la entidad a buscar", required = true)
        @RequestParam String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // Devuelve un error 400 si el nombre está vacío
        }
        List<Entidad> entidades = entidadService.buscarEntidadPorNombre(nombre.trim());
        return entidades.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(entidades);
    }

    @GetMapping("/buscarPorNit/{nit}")
    @Operation(summary = "Buscar entidad por NIT", description = "Devuelve una entidad basada en su número de identificación tributaria (NIT).", 
        responses = {
            @ApiResponse(responseCode = "200", description = "Entidad encontrada", 
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = Entidad.class))),
            @ApiResponse(responseCode = "404", description = "Entidad no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
    public ResponseEntity<Entidad> buscarPorNit(
        @Parameter(description = "Número de Identificación Tributaria (NIT)", example = "9001234567")
        @PathVariable Long nit) {
        try {
            Entidad entidad = entidadService.buscarPorNit(nit);
            return ResponseEntity.ok(entidad);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar todas las entidades", description = "Devuelve una lista de todas las entidades junto con sus pensionados.", 
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de entidades encontradas", 
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = EntidadConPensionadosRespuesta.class))),
            @ApiResponse(responseCode = "204", description = "No se encontraron entidades"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
    public List<EntidadConPensionadosRespuesta> listarTodos() {
        try {
            return entidadService.listarTodos();
        } catch (RuntimeException ex) {
            throw new RuntimeException("Error al listar entidades: " + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error interno del servidor: " + ex.getMessage());
        }
    }

    @PutMapping("/actualizar/{nid}")
    @Operation(summary = "Actualizar una entidad", description = "Actualiza los datos de una entidad existente.", 
        responses = {
            @ApiResponse(responseCode = "200", description = "Entidad actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud"),
            @ApiResponse(responseCode = "404", description = "Entidad no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
    public ResponseEntity<?> actualizarEntidad(
        @Parameter(description = "Identificador único de la entidad", example = "9001234567")
        @PathVariable("nid") Long id,
        @RequestBody RegistroEntidadPeticion entidad) {
        try {
            entidadService.actualizar(id, entidad);
            return ResponseEntity.ok("Entidad actualizada exitosamente");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Error al actualizar entidad: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error interno del servidor: " + ex.getMessage());
        }
    }

    @PutMapping("/activar/{nid}")
    @Operation(summary = "Activar una entidad", description = "Activa una entidad previamente desactivada.",    
        responses = {
            @ApiResponse(responseCode = "200", description = "Entidad activada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Entidad no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
    public ResponseEntity<?> activarEntidad(
        @Parameter(description = "Número de Identificación Tributaria (NIT)", example = "9001234567")
        @PathVariable Long nid) {
        try {
            boolean resultado = entidadService.activarEntidad(nid);
            if (resultado) {
                return ResponseEntity.ok("Entidad activada exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró la entidad con NIT: " + nid);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al activar la entidad: " + ex.getMessage());
        }
    }

    @PutMapping("/desactivar/{nid}")
    @Operation(summary = "Desactivar una entidad", description = "Desactiva una entidad sin eliminarla del sistema.", 
        responses = {
            @ApiResponse(responseCode = "200", description = "Entidad desactivada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Entidad no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
    public ResponseEntity<?> desactivarEntidad(
        @Parameter(description = "Número de Identificación Tributaria (NIT)", example = "9001234567")
        @PathVariable Long nid) {
        try {
            boolean resultado = entidadService.desactivarEntidad(nid);
            if (resultado) {
                return ResponseEntity.ok("Entidad desactivada exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró la entidad con NIT: " + nid);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al desactivar la entidad: " + ex.getMessage());
        }
    }

    @PutMapping("/editarPensionados/{nitEntidad}")
    @Operation(summary = "Editar pensionados de una entidad", description = "Permite actualizar la lista de pensionados asociados a una entidad.",  
        responses = {
            @ApiResponse(responseCode = "200", description = "Pensionados de la entidad actualizados exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
    public ResponseEntity<?> editarPensionadosDeEntidad(
        @Parameter(description = "Número de Identificación Tributaria (NIT) de la entidad", example = "9001234567")
        @PathVariable Long nitEntidad,
        @RequestBody List<RegistroTrabajoPeticion> trabajosActualizados) {
        try {
            entidadService.editarPensionadosDeEntidad(nitEntidad, trabajosActualizados);
            return ResponseEntity.ok("Pensionados de la entidad actualizados exitosamente");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Error al actualizar pensionados: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + ex.getMessage());
        }
    }
}