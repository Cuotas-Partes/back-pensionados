package com.unicauca.pensionados.backend.domain.model.enums;

/**
 * Enumeración para el tipo de resolución.
 */
public enum TipoResolucion {
    PENSION_INICIAL(true, true),           // Modifica valor, requiere valor
    AUMENTO_PENSION(true, true),           // Modifica valor, requiere valor
    AJUSTE_PENSION(true, true),            // Modifica valor, requiere valor
    RELIQUIDACION(true, true),             // Modifica valor, requiere valor
    NOMBRAMIENTO_SUSTITUTO(false, false),  // NO modifica valor, NO requiere valor
    RETIRO_SUSTITUTO(false, false),        // NO modifica valor, NO requiere valor
    SUSPENSION(false, false),              // NO modifica valor, NO requiere valor
    REACTIVACION(false, false);            // NO modifica valor, NO requiere valor

    private final boolean modificaValor;
    private final boolean requiereValor;

    TipoResolucion(boolean modificaValor, boolean requiereValor) {
        this.modificaValor = modificaValor;
        this.requiereValor = requiereValor;
    }

    public boolean modificaValor() {
        return modificaValor;
    }

    public boolean requiereValor() {
        return requiereValor;
    }

    public String getDescripcion() {
        return switch (this) {
            case PENSION_INICIAL -> "Pensión Inicial";
            case AUMENTO_PENSION -> "Aumento de Pensión";
            case AJUSTE_PENSION -> "Ajuste de Pensión";
            case RELIQUIDACION -> "Reliquidación";
            case NOMBRAMIENTO_SUSTITUTO -> "Nombramiento de Sustituto";
            case RETIRO_SUSTITUTO -> "Retiro de Sustituto";
            case SUSPENSION -> "Suspensión";
            case REACTIVACION -> "Reactivación";
        };
    }
}
