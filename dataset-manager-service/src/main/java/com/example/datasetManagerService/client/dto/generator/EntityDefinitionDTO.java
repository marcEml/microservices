package com.example.datasetManagerService.client.dto.generator;

import java.util.List;

public record EntityDefinitionDTO(
        String name,
        List<AttributeDefinitionDTO> attributes,
        List<EntityDefinitionDTO> subEntities
) {}
