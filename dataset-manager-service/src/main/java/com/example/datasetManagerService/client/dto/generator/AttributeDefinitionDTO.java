package com.example.datasetManagerService.client.dto.generator;

public record AttributeDefinitionDTO(
        String name,
        AttributeType type,
        Integer min,
        Integer max,
        Integer length
) {
    public enum AttributeType { STRING, INTEGER, DOUBLE, BOOLEAN, DATE }
}