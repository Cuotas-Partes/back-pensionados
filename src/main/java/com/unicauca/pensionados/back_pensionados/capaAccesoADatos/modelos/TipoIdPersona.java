package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

/**
 * Enumeración para el tipo de identificación de una persona.
 * Persistencia: INT mediante @Enumerated(EnumType.ORDINAL) en la entidad.
 */
public enum TipoIdPersona {
    CC(1), // Cédula de ciudadanía
    TI(2), // Tarjeta de identidad
    CE(3), // Cédula de extranjería
    PASAPORTE(4),
    NIT(5);

    private final int codigo;

    TipoIdPersona(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static TipoIdPersona fromCodigo(int codigo) {
        for (TipoIdPersona value : values()) {
            if (value.codigo == codigo) {
                return value;
            }
        }
        throw new IllegalArgumentException("Código de TipoIdPersona no válido: " + codigo);
    }
}


