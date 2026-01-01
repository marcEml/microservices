package com.example.generator_service.service.generator;

import com.example.generator_service.dto.generator.AttributeDefinitionDTO;

import org.springframework.stereotype.Component;


@Component
public class BooleanGenerator implements ValueGenerator {

    @Override
    public Object generate(AttributeDefinitionDTO attribute) {
        return Math.random() > 0.5;
    }
}

