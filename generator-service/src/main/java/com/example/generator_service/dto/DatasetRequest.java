package com.example.generator_service.dto;

import com.example.generator_service.dto.generator.DatasetDefinitionDTO;

public record DatasetRequest(
        DatasetDefinitionDTO dataset,
        int count,
        String format
) {}