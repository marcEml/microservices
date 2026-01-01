package com.example.datasetManagerService.dto.Attribute;

import com.example.datasetManagerService.model.AttributeModel.AttributeType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Création d'un attribut")
public record AttributeCreateDTO(

        @Schema(example = "age")
        String name,

        AttributeType type,

        Integer min,
        Integer max,
        Integer length,

        @Schema(example = "1")
        Long entityId

) {}
