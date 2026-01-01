package com.example.datasetManagerService.client.dto.generator;

import java.util.List;

public record DatasetDefinitionDTO(
        String projectName,
        List<EntityDefinitionDTO> entities
) {}
