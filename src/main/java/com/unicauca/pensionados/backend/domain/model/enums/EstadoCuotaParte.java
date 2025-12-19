package com.unicauca.pensionados.backend.domain.model.enums;

/**
 * Enum para representar los estados posibles de una cuota parte
 * (tanto por cobrar como por pagar)
 */
public enum EstadoCuotaParte {
    PENDIENTE("Pendiente de pago"),
    PAGADA("Pagada"),
    PARCIALMENTE_PAGADA("Parcialmente pagada"),
    EN_PROCESO("En proceso de pago"),
    VENCIDA("Vencida"),
    ANULADA("Anulada"),
    EN_REVISION("En revisión"),
    APROBADA("Aprobada para pago");

    private final String descripcion;

    EstadoCuotaParte(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

