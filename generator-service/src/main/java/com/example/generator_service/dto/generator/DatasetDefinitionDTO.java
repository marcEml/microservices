package com.example.generator_service.dto.generator;

import java.util.List;

public record DatasetDefinitionDTO(
        String projectName,
        List<EntityDefinitionDTO> entities,
        int count,
        String format
) {}
