package com.example.generator_service.service.generator;

import com.example.generator_service.dto.generator.AttributeDefinitionDTO;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

@Component
public class StringGenerator implements ValueGenerator {

    private final Faker faker = new Faker();

    @Override
    public Object generate(AttributeDefinitionDTO attribute) {

        String name = attribute.name() != null ? attribute.name().toLowerCase() : "";

        if (name.contains("email")) return faker.internet().emailAddress();
        if (name.contains("name")) return faker.name().fullName();
        if (name.contains("phone")) return faker.phoneNumber().cellPhone();
        if (name.contains("city")) return faker.address().city();
        if (name.contains("country")) return faker.address().country();

        int length = attribute.length() != null ? attribute.length() : 10;
        return faker.lorem().characters(length);
    }
}