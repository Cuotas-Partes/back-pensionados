package com.unicauca.pensionados.back_pensionados.CapaServicio.excepciones;

public class BusinessValidationException extends RuntimeException {
    public BusinessValidationException(String message) {
        super(message);
    }
}
