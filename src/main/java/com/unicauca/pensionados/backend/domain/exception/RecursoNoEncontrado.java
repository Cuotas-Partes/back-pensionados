package com.unicauca.pensionados.backend.domain.exception;

public class RecursoNoEncontrado extends RuntimeException {
    
    public RecursoNoEncontrado(String mensaje) {
        super(mensaje);
    }
    
    public RecursoNoEncontrado(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
