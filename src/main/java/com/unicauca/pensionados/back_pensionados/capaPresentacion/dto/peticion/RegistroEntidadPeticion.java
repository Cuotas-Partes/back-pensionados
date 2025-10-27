package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Petición para registrar una entidad")
public class RegistroEntidadPeticion {

    @Schema(description = "NIT de la entidad", example = "1234567890")
    @NotNull(message = "El NIT de la entidad no puede ser nulo")
    private Long nitEntidad;

    @Schema(description = "Nombre de la entidad", example = "Universidad del Cauca")
    @NotEmpty(message = "El nombre de la entidad no puede estar vacio")
    private String nombreEntidad;

    @Schema(description = "Dirección de la entidad", example = "Calle 5 # 4-70")
    @NotEmpty(message = "La direccion de la entidad no puede estar vacia")
    private String direccionEntidad;

    @Schema(description = "Teléfono de la entidad", example = "123456789")
    @NotNull(message = "El telefono de la entidad no puede ser nulo")
    private Long telefonoEntidad;

    @Schema(description = "Email de la entidad", example = "relacionesinter@unicauca.edu.co")
    @NotEmpty(message = "El email de la entidad no puede estar vacio")
    @Email(message = "El email de la entidad no tiene un formato válido")
    private String emailEntidad;

    @Schema(description = "Estado de la entidad", example = "ACTIVO")
    @NotEmpty(message = "El estado de la entidad no puede estar vacio")
    private String estadoEntidad;

    @Schema(description = "Lista de trabajos asociados a la entidad")
    //Lista de trabajos asociados a la entidad 
    private List<RegistroTrabajoPeticion> trabajos;
}

