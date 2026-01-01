package com.example.datasetManagerService.client.dto;

import com.example.datasetManagerService.client.dto.generator.DatasetDefinitionDTO;

public record DatasetRequest(
        DatasetDefinitionDTO dataset,
        int count,
        String format
) {}
