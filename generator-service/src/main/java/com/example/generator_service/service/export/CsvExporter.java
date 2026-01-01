package com.example.generator_service.service.export;

import com.example.generator_service.model.Dataset;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class CsvExporter implements Exporter {

    @Override
    public byte[] export(Dataset dataset) {
        StringBuilder sb = new StringBuilder();

        // Entête (prend les clés de la première ligne)
        if (!dataset.getRecords().isEmpty()) {
            dataset.getRecords().get(0).forEach((entity, attrs) -> {
                if (attrs instanceof Map<?, ?> map) {
                    map.keySet().forEach(k -> sb.append(entity).append("_").append(k).append(","));
                }
            });
            sb.setLength(sb.length() - 1); // supprimer la dernière virgule
            sb.append("\n");
        }

        // Contenu
        for (var record : dataset.getRecords()) {
            record.forEach((entity, attrs) -> {
                if (attrs instanceof Map<?, ?> map) {
                    map.values().forEach(v -> sb.append(v).append(","));
                }
            });
            sb.setLength(sb.length() - 1);
            sb.append("\n");
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String getContentType() {
        return "text/csv";
    }

    @Override
    public String getFileExtension() {
        return "csv";
    }
}
