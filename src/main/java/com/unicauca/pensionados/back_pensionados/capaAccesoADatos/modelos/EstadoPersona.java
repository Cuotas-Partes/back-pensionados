package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

/**
 * Enumeración para el estado de una persona.
 * Persistencia: INT mediante @Enumerated(EnumType.ORDINAL) en la entidad.
 */
public enum EstadoPersona {
    ACTIVO(1),
    FALLECIDO(2),
    INACTIVO(3);

    private final int codigo;

    EstadoPersona(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static EstadoPersona fromCodigo(int codigo) {
        for (EstadoPersona value : values()) {
            if (value.codigo == codigo) {
                return value;
            }
        }
        throw new IllegalArgumentException("Código de EstadoPersona no válido: " + codigo);
    }
}


