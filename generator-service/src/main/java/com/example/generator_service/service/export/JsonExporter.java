package com.example.generator_service.service.export;

import com.example.generator_service.model.Dataset;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Component;

@Component
public class JsonExporter implements Exporter {

    private final ObjectMapper mapper;

    public JsonExporter() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT); // pretty print
    }

    @Override
    public byte[] export(Dataset dataset) {
        try {
            return mapper.writeValueAsBytes(dataset);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'export JSON", e);
        }
    }

    @Override
    public String getContentType() {
        return "application/json";
    }

    @Override
    public String getFileExtension() {
        return "json";
    }
}
