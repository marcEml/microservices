package com.example.generator_service.controller;

import com.example.generator_service.dto.DatasetRequest;
import com.example.generator_service.model.Dataset;
import com.example.generator_service.service.DatasetGeneratorService;
import com.example.generator_service.service.export.Exporter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/generate")
@RequiredArgsConstructor
public class GeneratorController {

    private final DatasetGeneratorService generatorService;
    private final List<Exporter> exporters;

    @PostMapping
    public ResponseEntity<byte[]> generate(@RequestBody DatasetRequest request) {

        Dataset dataset = generatorService.generate(
                request.dataset(),
                request.count()
        );

        Exporter exporter = exporters.stream()
                .filter(e -> e.getFileExtension().equalsIgnoreCase(request.format()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unsupported format"));

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=data." + exporter.getFileExtension()
                )
                .contentType(MediaType.parseMediaType(exporter.getContentType()))
                .body(exporter.export(dataset));
    }
}