package com.unicauca.pensionados.backend.application.dto.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Deserializador personalizado para fechas que acepta múltiples formatos.
 */
public class MultiDateDeserializer extends JsonDeserializer<Date> {

    private static final String[] DATE_FORMATS = {
            "yyyy-MM-dd",
            "dd/MM/yyyy",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            "dd-MM-yyyy"
    };

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        String date = jsonParser.getText();
        
        if (date == null || date.trim().isEmpty()) {
            return null;
        }

        for (String format : DATE_FORMATS) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                sdf.setLenient(false);
                return sdf.parse(date);
            } catch (ParseException e) {
                // Intentar con el siguiente formato
            }
        }

        throw new IOException("No se pudo parsear la fecha: " + date + ". Formatos soportados: " 
                + String.join(", ", DATE_FORMATS));
    }
}
