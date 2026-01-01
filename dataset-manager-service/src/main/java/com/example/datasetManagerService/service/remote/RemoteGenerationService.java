package com.example.datasetManagerService.service.remote;

import com.example.datasetManagerService.client.dto.DatasetRequest;
import com.example.datasetManagerService.client.dto.generator.DatasetDefinitionDTO;
import com.example.datasetManagerService.model.ProjectModel;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class RemoteGenerationService {

    private final GeneratorClient generatorClient;

    @CircuitBreaker(name = "generatorService", fallbackMethod = "fallback")
    public byte[] generate(DatasetDefinitionDTO definition, int count, String format) {
        return generatorClient.generate(new DatasetRequest(definition, count, format));
    }

    // ✅ fallback doit retourner le MEME type que la méthode: byte[]
    private byte[] fallback(DatasetDefinitionDTO definition, int count, String format, Throwable ex) {
        String json = """
                {
                  "status": "PARTIAL",
                  "message": "Service de génération momentanément indisponible",
                  "data": []
                }
                """;
        return json.getBytes(StandardCharsets.UTF_8);
    }
}