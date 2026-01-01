package com.example.datasetManagerService.dto.Entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Création d'une entité")
public record EntityCreateDTO(

        @Schema(example = "User")
        String name,

        @Schema(example = "1")
        Long projectId,

        @Schema(description = "ID de l'entité parente (optionnel)")
        Long parentEntityId

) {}
