package com.example.generator_service.service.generator;

import com.example.generator_service.dto.generator.AttributeDefinitionDTO;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DoubleGenerator implements ValueGenerator {

    private final Random random = new Random();

    @Override
    public Object generate(AttributeDefinitionDTO attribute) {

        double min = attribute.min() != null ? attribute.min() : 0.0;
        double max = attribute.max() != null ? attribute.max() : 100.0;

        return min + (max - min) * random.nextDouble();
    }
}