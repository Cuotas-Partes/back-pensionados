package com.unicauca.pensionados.back_pensionados.CapaServicio.excepciones;

public class RecursoNoEncontrado extends RuntimeException {
    
    public RecursoNoEncontrado(String mensaje) {
        super(mensaje);
    }
    
    public RecursoNoEncontrado(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
