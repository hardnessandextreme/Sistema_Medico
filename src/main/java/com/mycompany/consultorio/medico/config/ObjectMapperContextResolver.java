package com.mycompany.consultorio.medico.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {
    
    private final ObjectMapper mapper;
    
    public ObjectMapperContextResolver() {
        mapper = new ObjectMapper();
        // Registrar módulo para LocalDateTime y otros tipos de Java 8 date/time
        mapper.registerModule(new JavaTimeModule());
        // Desactivar escritura de fechas como timestamps (escribir como ISO-8601)
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
    }
    
    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}
