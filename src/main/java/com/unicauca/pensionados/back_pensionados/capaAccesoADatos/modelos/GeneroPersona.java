package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

/**
 * Enumeración para el género de una persona.
 * Persistencia: INT mediante @Enumerated(EnumType.ORDINAL) en la entidad.
 */
public enum GeneroPersona {
    MASCULINO(1),
    FEMENINO(2),
    NO_BINARIO(3),
    NO_INFORMA(4);

    private final int codigo;

    GeneroPersona(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static GeneroPersona fromCodigo(int codigo) {
        for (GeneroPersona value : values()) {
            if (value.codigo == codigo) {
                return value;
            }
        }
        throw new IllegalArgumentException("Código de GeneroPersona no válido: " + codigo);
    }
}


