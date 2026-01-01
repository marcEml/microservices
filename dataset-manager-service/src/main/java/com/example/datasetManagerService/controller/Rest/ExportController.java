package com.example.datasetManagerService.controller.Rest;

import com.example.datasetManagerService.client.dto.generator.DatasetDefinitionDTO;
import com.example.datasetManagerService.model.ProjectModel;
import com.example.datasetManagerService.service.basicServices.ProjectService;
import com.example.datasetManagerService.service.remote.DatasetDefinitionMapper;
import com.example.datasetManagerService.service.remote.RemoteGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class ExportController {

    private final ProjectService projectService;
    private final RemoteGenerationService remoteGenerationService;
    private final DatasetDefinitionMapper datasetDefinitionMapper;

    @GetMapping
    public ResponseEntity<byte[]> export(
            @RequestParam Long projectId,
            @RequestParam(defaultValue = "json") String format,
            @RequestParam(defaultValue = "50") int count
    ) {

        ProjectModel project = projectService.getProjectById(projectId);
        DatasetDefinitionDTO definition = datasetDefinitionMapper.fromProject(project);

        byte[] bytes = remoteGenerationService.generate(definition, count, format);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"dataset." + format + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bytes);
    }
}