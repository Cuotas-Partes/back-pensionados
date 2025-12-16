package com.unicauca.pensionados.backend.application.dto.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Deserializador personalizado para fechas que acepta múltiples formatos.
 */
public class MultiDateDeserializer extends JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ISO_DATE
    };

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        String date = jsonParser.getText();
        
        if (date == null || date.trim().isEmpty()) {
            return null;
        }

        // Si la fecha contiene 'T' (formato ISO DateTime), extraer solo la fecha
        if (date.contains("T")) {
            date = date.split("T")[0];
        }

        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                // Intentar con el siguiente formato
            }
        }

        throw new IOException("No se pudo parsear la fecha: " + date + ". Formatos soportados: yyyy-MM-dd, dd/MM/yyyy, dd-MM-yyyy");
    }
}
