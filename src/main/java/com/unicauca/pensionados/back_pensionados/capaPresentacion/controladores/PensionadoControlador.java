package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.IPensionadoServicio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.FiltroCuotaPartePeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroPensionadoPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.EntidadCuotaParteRespuesta;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PensionadoRespuesta;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//IMPORTS PARA EL METODO DE VERIFICACION DE ROL
import org.springframework.security.core.Authentication;
import java.util.Collection;

@RestController
@RequestMapping("/pensionado")
@Tag(name = "Pensionado Management", description = "APIs para la gestión de pensionados")
public class PensionadoControlador {
    private final IPensionadoServicio pensionadoServicio;

    public PensionadoControlador(IPensionadoServicio pensionadoServicio){
        this.pensionadoServicio = pensionadoServicio;
    }

    @PostMapping("/registrar")
    @Operation(summary = "Registrar un nuevo pensionado", description = "Registra un nuevo pensionado en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pensionado registrado exitosamente",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Error al registrar pensionado",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> registrarPensionado(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del pensionado a registrar", required = true,
            content = @Content(schema = @Schema(implementation = RegistroPensionadoPeticion.class))) @RequestBody RegistroPensionadoPeticion peticion) {
        try{
            pensionadoServicio.registrarPensionado(peticion);
        return ResponseEntity.ok("Pensionado registrado exitosamente");
        } catch (RuntimeException ex) {
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al registrar pensionado: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + ex.getMessage());
        }
        
    }

    @PostMapping("/actualizar/{id}")
    @Operation(summary = "Actualizar un pensionado existente", description = "Actualiza los datos de un pensionado existente identificado por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pensionado actualizado exitosamente",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Error al actualizar pensionado",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Pensionado no encontrado",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))), 
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> actualizarPensionado(
            @io.swagger.v3.oas.annotations.Parameter(description = "ID del pensionado a actualizar", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del pensionado", required = true,
                    content = @Content(schema = @Schema(implementation = RegistroPensionadoPeticion.class))) @RequestBody RegistroPensionadoPeticion peticion) {
        try{
            pensionadoServicio.actualizarPensionado(id, peticion);
            return ResponseEntity.ok("Pensionado actualizado exitosamente");
        } catch (RuntimeException ex) {
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al actualzar pensionado: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + ex.getMessage());
        }
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar todos los pensionados", description = "Obtiene una lista de todos los pensionados registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pensionados obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PensionadoRespuesta.class, type = "array"))),
            @ApiResponse(responseCode = "400", description = "Error al listar pensionados",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> listarPensionados() {
        try {
            List<PensionadoRespuesta> pensionados = pensionadoServicio.listarPensionados();
            return ResponseEntity.ok(pensionados);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al listar pensionados: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor: " + ex.getMessage());
        }
    }
    
 
    @GetMapping("/buscar/id/{id}")
    @Operation(summary = "Buscar pensionado por ID", description = "Busca un pensionado específico utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pensionado encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PensionadoRespuesta.class))),
            @ApiResponse(responseCode = "404", description = "Pensionado no encontrado",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> buscarPensionadoPorId(
            @io.swagger.v3.oas.annotations.Parameter(description = "ID del pensionado a buscar", required = true) @PathVariable Long id) {
        try {
            PensionadoRespuesta pensionadoDTO = pensionadoServicio.buscarPensionadoPorId(id);
            return ResponseEntity.ok(pensionadoDTO);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + ex.getMessage());
        }
    }




    @GetMapping("/buscar/nombre")
    @Operation(summary = "Buscar pensionados por nombre", description = "Busca pensionados cuyo nombre coincida (parcial o totalmente) con el término proporcionado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pensionados encontrados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pensionado.class, type = "array"))),
            @ApiResponse(responseCode = "400", description = "El parámetro 'nombre' no puede estar vacío",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> buscarPensionadosPorNombre(
            @io.swagger.v3.oas.annotations.Parameter(description = "Nombre o parte del nombre del pensionado a buscar", required = false) @RequestParam(required = false) String nombre) {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("El parámetro 'nombre' no puede estar vacío");
            }
            List<Pensionado> pensionados = pensionadoServicio.buscarPensionadosPorNombre(nombre.trim());
            return ResponseEntity.ok(pensionados);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + ex.getMessage());
        }
    }

    @GetMapping("/buscar/apellido")
    @Operation(summary = "Buscar pensionados por apellido", description = "Busca pensionados cuyo apellido coincida (parcial o totalmente) con el término proporcionado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pensionados encontrados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pensionado.class, type = "array"))),
            @ApiResponse(responseCode = "400", description = "El parámetro 'apellido' no puede estar vacío",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> buscarPensionadosPorApellido(
            @io.swagger.v3.oas.annotations.Parameter(description = "Apellido o parte del apellido del pensionado a buscar", required = false) @RequestParam(required = false) String apellido) {
        try {
            if (apellido == null || apellido.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("El parámetro 'apellido' no puede estar vacío");
            }
            List<Pensionado> pensionados = pensionadoServicio.buscarPensionadosPorApellido(apellido.trim());
            return ResponseEntity.ok(pensionados);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + ex.getMessage());
        }
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar pensionados por criterio", description = "Busca pensionados por un criterio general (nombre, apellido o cédula). El término de búsqueda se aplica a estos campos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pensionados encontrados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pensionado.class, type = "array"))),
            @ApiResponse(responseCode = "400", description = "Error en la búsqueda",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> buscarPensionadosPorCriterio(
            @io.swagger.v3.oas.annotations.Parameter(description = "Término de búsqueda para nombre, apellido o cédula", required = false) @RequestParam(required = false) String query) {
        try {
            List<Pensionado> pensionados = pensionadoServicio.buscarPensionadosPorCriterio(query);
            return ResponseEntity.ok(pensionados);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error en la búsqueda: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + ex.getMessage());
        }
    }

    @PatchMapping("/desactivar/{id}")
    @Operation(summary = "Desactivar un pensionado", description = "Marca un pensionado como inactivo en el sistema utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pensionado desactivado exitosamente",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Pensionado no encontrado",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> desactivarPensionado(
            @io.swagger.v3.oas.annotations.Parameter(description = "ID del pensionado a desactivar", required = true) @PathVariable Long id) {
        try {
            pensionadoServicio.desactivarPensionado(id);
            return ResponseEntity.ok("Pensionado desactivado exitosamente");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + ex.getMessage());
        }
    }

    @GetMapping("/{pensionadoId}/entidades-cuotaparte")
    @Operation(summary = "Obtener entidades y cuotas parte por ID de pensionado",
               description = "Recupera una lista de entidades y sus respectivas cuotas parte asociadas a un pensionado específico, identificado por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entidades y cuotas parte obtenidas exitosamente",
                    content = @Content(mediaType = "application/json", 
                                       schema = @Schema(implementation = EntidadCuotaParteRespuesta.class, type = "array"))),
            @ApiResponse(responseCode = "404", description = "Pensionado no encontrado o sin entidades asociadas", // Assuming the service might return empty or handle not found
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = EntidadCuotaParteRespuesta.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))) // Assuming error message as plain text
    })
    public ResponseEntity<List<EntidadCuotaParteRespuesta>> getEntidadesYCuotaParteByPensionadoId(
            @io.swagger.v3.oas.annotations.Parameter(description = "ID del pensionado para consultar sus entidades y cuotas parte", required = true)
            @PathVariable Long pensionadoId) {
        List<EntidadCuotaParteRespuesta> response = pensionadoServicio.getEntidadesYCuotaParteByPensionadoId(pensionadoId);
        return ResponseEntity.ok(response);
    }

    //NUEVO METOD IMPLEMENTADO
    //VERIFICA EL ROL DEL USUARIO PARA SABER SUS PERMISOS
        @GetMapping("/verificar-permisos")
        @Operation(summary = "Verificar los permisos del usuario autenticado", description = "Endpoint de diagnóstico para ver los permisos (acciones) asociados al token.")
        public ResponseEntity<String> verificarPermisos(Authentication authentication) {
        
        String nombreUsuario = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // Convertimos la lista de permisos a un String para mostrarla
        String permisos = authorities.stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .collect(Collectors.joining(", "));

        // Verificamos si tiene un permiso específico
        boolean puedeRegistrar = authorities.stream()
                                                .anyMatch(auth -> auth.getAuthority().equals("REGISTRO_PENSIONADO"));

        String mensaje = "¡Hola, " + nombreUsuario + "! Tus permisos son: [" + permisos + "]. ";
        if (puedeRegistrar) {
                mensaje += "Tienes permiso para registrar pensionados.";
        } else {
                mensaje += "NO tienes permiso para registrar pensionados.";
        }
        
        return ResponseEntity.ok(mensaje);
        }

}
