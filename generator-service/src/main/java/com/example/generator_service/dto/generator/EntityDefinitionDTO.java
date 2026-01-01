package com.example.generator_service.dto.generator;

import java.util.List;

public record EntityDefinitionDTO(
        String name,
        List<AttributeDefinitionDTO> attributes,
        List<EntityDefinitionDTO> subEntities
) {}
