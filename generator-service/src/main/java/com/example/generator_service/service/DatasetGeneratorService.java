package com.example.generator_service.service;

import com.example.generator_service.dto.generator.AttributeDefinitionDTO;
import com.example.generator_service.dto.generator.DatasetDefinitionDTO;
import com.example.generator_service.dto.generator.EntityDefinitionDTO;
import com.example.generator_service.model.Dataset;
import com.example.generator_service.service.generator.GeneratorRegistry;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DatasetGeneratorService {

    private final GeneratorRegistry generatorRegistry;
    private final Faker faker = new Faker();

    public Dataset generate(DatasetDefinitionDTO definition, int count) {

        Dataset dataset = new Dataset(definition.projectName());

        // Root entities are the ones provided at top level
        List<EntityDefinitionDTO> rootEntities = definition.entities();

        for (int i = 0; i < count; i++) {
            Map<String, Object> record = new LinkedHashMap<>();

            for (EntityDefinitionDTO entity : rootEntities) {
                record.put(entity.name(), generateEntity(entity));
            }

            dataset.addRecord(record);
        }

        return dataset;
    }

    private Map<String, Object> generateEntity(EntityDefinitionDTO entity) {
        Map<String, Object> result = new LinkedHashMap<>();

        // 1) Simple attributes
        if (entity.attributes() != null) {
            for (AttributeDefinitionDTO attr : entity.attributes()) {
                Object value = generateFakeValue(entity.name(), attr);
                result.put(attr.name(), value);
            }
        }

        // 2) Sub-entities -> list (1..3)
        if (entity.subEntities() != null) {
            for (EntityDefinitionDTO sub : entity.subEntities()) {
                int subCount = 1 + (int) (Math.random() * 3);
                List<Map<String, Object>> children = new ArrayList<>();

                for (int i = 0; i < subCount; i++) {
                    children.add(generateEntity(sub));
                }

                result.put(sub.name(), children);
            }
        }

        return result;
    }

    private Object generateFakeValue(String entityName, AttributeDefinitionDTO attr) {
        String attrName = attr.name().toLowerCase();

        if ("personne".equalsIgnoreCase(entityName) || "population française".equalsIgnoreCase(entityName)) {
            return switch (attrName) {
                case "nom" -> faker.name().lastName();
                case "prenom" -> faker.name().firstName();
                case "ville" -> faker.address().city();
                case "age" -> faker.number().numberBetween(0, 100);
                default -> generatorRegistry.generate(attr); // fallback instead of null
            };
        }

        if ("product".equalsIgnoreCase(entityName)) {
            return switch (attrName) {
                case "name" -> faker.commerce().productName();
                case "price" -> {
                    double min = attr.min() != null ? attr.min() : 0.0;
                    double max = attr.max() != null ? attr.max() : 100.0;
                    yield faker.number().randomDouble(2, (long) min, (long) max);
                }
                case "instock" -> faker.bool().bool();
                default -> generatorRegistry.generate(attr);
            };
        }

        // Generic fallback
        return generatorRegistry.generate(attr);
    }
}