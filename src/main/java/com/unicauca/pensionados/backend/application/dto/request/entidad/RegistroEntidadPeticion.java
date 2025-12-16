package com.unicauca.pensionados.backend.application.dto.request.entidad;

import com.unicauca.pensionados.backend.domain.model.enums.EstadoEntidad;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Petición para crear/actualizar una entidad")
public class RegistroEntidadPeticion {

    @Schema(description = "NIT de la entidad", example = "8911500319")
    @NotBlank(message = "El NIT no puede estar vacío")
    @Size(max = 20, message = "El NIT no puede exceder 20 caracteres")
    private String nit;

    @Schema(description = "Nombre de la entidad", example = "Universidad del Cauca")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String name;

    @Schema(description = "Dirección de la entidad", example = "Calle 5 # 4-70")
    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(max = 500, message = "La dirección no puede exceder 500 caracteres")
    private String address;

    @Schema(description = "Email de la entidad", example = "relacionesinter@unicauca.edu.co")
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no tiene un formato válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;

    @Schema(description = "Teléfono de la entidad", example = "3125551234")
    @NotBlank(message = "El teléfono no puede estar vacío")
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String phone;

    @Schema(description = "Funcionario responsable", example = "Juan Pérez")
    @NotBlank(message = "El funcionario responsable no puede estar vacío")
    @Size(max = 200, message = "El funcionario responsable no puede exceder 200 caracteres")
    private String responsibleOfficer;

    @Schema(description = "Cargo del funcionario responsable", example = "Jefe de Nómina")
    @NotBlank(message = "El cargo del funcionario no puede estar vacío")
    @Size(max = 200, message = "El cargo del funcionario no puede exceder 200 caracteres")
    private String officerPosition;

    @Schema(description = "Estado de la entidad", example = "Activo")
    private EstadoEntidad estado;

    @Schema(description = "ID de la persona encargada (opcional)", example = "15")
    private Long idPersonaEncargado;
}
