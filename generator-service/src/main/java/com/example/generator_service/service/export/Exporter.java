package com.example.generator_service.service.export;

import com.example.generator_service.model.Dataset;

public interface Exporter {

    // Convertit le Dataset en bytes (CSV, JSON, etc.)
    byte[] export(Dataset dataset);

    // Type MIME pour le ResponseEntity
    String getContentType();

    // Extension du fichier exporté, ex: "csv" ou "json"
    String getFileExtension();
}
