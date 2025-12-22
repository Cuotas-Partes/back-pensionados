package com.unicauca.pensionados.backend.application.service.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Clase utilitaria para realizar cálculos relacionados con cuotas partes.
 * Centraliza toda la lógica de cálculo para evitar duplicación de código.
 */
public class CalculadoraCuotas {

    private CalculadoraCuotas() {
        // Constructor privado para prevenir instanciación
        throw new IllegalStateException("Clase utilitaria - no debe instanciarse");
    }

    /**
     * Calcula el porcentaje de cuota parte basado en días trabajados
     * Fórmula: % = (Días Entidad / Total Días) × 100
     *
     * @param diasEntidad Días trabajados en la entidad específica
     * @param diasTotales Total de días trabajados del pensionado
     * @return Porcentaje de cuota parte con 2 decimales
     */
    public static BigDecimal calcularPorcentajeCuotaParte(Integer diasEntidad, Integer diasTotales) {
        if (diasEntidad == null || diasTotales == null || diasTotales == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(diasEntidad)
                .divide(BigDecimal.valueOf(diasTotales), 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula el valor mensual de la cuota parte
     * Fórmula: Valor Cuota = Valor Pensión × (% Cuota Parte / 100)
     *
     * @param valorPension Valor de la pensión mensual del pensionado
     * @param porcentajeCuotaParte Porcentaje que corresponde a la entidad
     * @return Valor mensual de la cuota parte con 2 decimales
     */
    public static BigDecimal calcularValorCuotaParte(BigDecimal valorPension, BigDecimal porcentajeCuotaParte) {
        if (valorPension == null || porcentajeCuotaParte == null) {
            return BigDecimal.ZERO;
        }
        return valorPension
                .multiply(porcentajeCuotaParte)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula el valor total de las cuotas para múltiples meses
     * Fórmula: Total = Valor Cuota Mensual × Cantidad de Meses
     *
     * @param valorCuotaParte Valor mensual de la cuota parte
     * @param cantidadCuotas Número de meses/cuotas
     * @return Valor total de todas las cuotas con 2 decimales
     */
    public static BigDecimal calcularValorCuotasTotal(BigDecimal valorCuotaParte, Integer cantidadCuotas) {
        if (valorCuotaParte == null || cantidadCuotas == null) {
            return BigDecimal.ZERO;
        }
        return valorCuotaParte
                .multiply(BigDecimal.valueOf(cantidadCuotas))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula el total sumando cuotas y ajustes (SIN intereses)
     * Fórmula: Total = Cuotas + Ajustes
     * NOTA: Los intereses NO se calculan en cuotas normales
     *
     * @param valorCuotasTotal Total de las cuotas
     * @param valorInteresTotal Siempre debe ser ZERO (mantenido por compatibilidad)
     * @param ajuste Ajuste manual (puede ser positivo o negativo)
     * @return Total con 2 decimales
     */
    public static BigDecimal calcularTotal(
            BigDecimal valorCuotasTotal,
            BigDecimal valorInteresTotal,
            BigDecimal ajuste) {

        BigDecimal total = BigDecimal.ZERO;

        if (valorCuotasTotal != null) {
            total = total.add(valorCuotasTotal);
        }
        // valorInteresTotal siempre será 0, pero se valida por compatibilidad
        if (valorInteresTotal != null && valorInteresTotal.compareTo(BigDecimal.ZERO) > 0) {
            total = total.add(valorInteresTotal);
        }
        if (ajuste != null) {
            total = total.add(ajuste);
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula el valor que debe pagar una entidad según un porcentaje
     * Fórmula: Valor = Total × (Porcentaje / 100)
     *
     * @param total Total sobre el cual calcular el porcentaje
     * @param porcentaje Porcentaje a aplicar
     * @return Valor calculado con 2 decimales
     */
    public static BigDecimal calcularValorPorPorcentaje(BigDecimal total, BigDecimal porcentaje) {
        if (total == null || porcentaje == null) {
            return BigDecimal.ZERO;
        }

        return total
                .multiply(porcentaje)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}

