package com.example.generator_service.service.generator;

import com.example.generator_service.dto.generator.AttributeDefinitionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class GeneratorRegistry {

    private final Map<AttributeDefinitionDTO.AttributeType, ValueGenerator> generators;

    public Object generate(AttributeDefinitionDTO attribute) {
        ValueGenerator generator = generators.get(attribute.type());
        if (generator == null) {
            throw new IllegalArgumentException("No generator for type " + attribute.type());
        }
        return generator.generate(attribute);
    }
}
