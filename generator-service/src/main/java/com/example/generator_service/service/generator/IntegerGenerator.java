package com.example.generator_service.service.generator;

import com.example.generator_service.dto.generator.AttributeDefinitionDTO;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class IntegerGenerator implements ValueGenerator {

    private final Random random = new Random();

    @Override
    public Object generate(AttributeDefinitionDTO attribute) {

        int min = attribute.min() != null ? attribute.min() : 0;
        int max = attribute.max() != null ? attribute.max() : 100;
        return min + new Random().nextInt(max - min + 1);
    }
}



