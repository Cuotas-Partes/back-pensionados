package com.unicauca.pensionados.backend.domain.model.enums;

/**
 * Enumeración para el tipo de evento en el historial.
 */
public enum TipoEvento {
    CREACION_PENSIONADO,
    CREACION_SUCESOR,
    ACTUALIZACION_DATOS,
    CAMBIO_ESTADO,
    CAMBIO_BENEFICIARIO,
    LIQUIDACION_INICIAL,
    RELIQUIDACION,
    AJUSTE_VALOR,
    SUSPENSION,
    REACTIVACION,
    FALLECIMIENTO,
    SUSTITUCION,
    REINTEGRO
}
