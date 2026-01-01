package com.example.generator_service.service.generator;

import com.example.generator_service.dto.generator.AttributeDefinitionDTO;

public interface ValueGenerator {
    Object generate(AttributeDefinitionDTO attribute);
}

