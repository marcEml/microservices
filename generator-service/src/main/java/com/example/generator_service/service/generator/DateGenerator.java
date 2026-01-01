package com.example.generator_service.service.generator;

import com.example.generator_service.dto.generator.AttributeDefinitionDTO;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DateGenerator implements ValueGenerator {

    private final Faker faker;

    @Override
    public Object generate(AttributeDefinitionDTO attribute) {
        return faker.timeAndDate()
                .birthday()
                .toString(); // ISO-8601 // retourne un java.util.Date ✔️
    }
}

