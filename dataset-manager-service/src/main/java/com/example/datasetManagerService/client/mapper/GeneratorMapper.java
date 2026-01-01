package com.example.datasetManagerService.client.mapper;

import com.example.datasetManagerService.client.dto.generator.AttributeDefinitionDTO;
import com.example.datasetManagerService.client.dto.generator.DatasetDefinitionDTO;
import com.example.datasetManagerService.client.dto.generator.EntityDefinitionDTO;
import com.example.datasetManagerService.model.AttributeModel;
import com.example.datasetManagerService.model.EntityModel;
import com.example.datasetManagerService.model.ProjectModel;

import java.util.List;

public class GeneratorMapper {

    public static DatasetDefinitionDTO toDefinition(ProjectModel project) {

        List<EntityDefinitionDTO> roots = project.getEntities().stream()
                .filter(e -> e.getParentEntity() == null)
                .map(GeneratorMapper::toEntity)
                .toList();

        return new DatasetDefinitionDTO(project.getName(), roots);
    }

    private static EntityDefinitionDTO toEntity(EntityModel entity) {

        List<AttributeDefinitionDTO> attrs = entity.getAttributes().stream()
                .map(GeneratorMapper::toAttr)
                .toList();

        List<EntityDefinitionDTO> subs = entity.getSubEntities().stream()
                .map(GeneratorMapper::toEntity)
                .toList();

        return new EntityDefinitionDTO(entity.getName(), attrs, subs);
    }

    private static AttributeDefinitionDTO toAttr(AttributeModel a) {
        return new AttributeDefinitionDTO(
                a.getName(),
                AttributeDefinitionDTO.AttributeType.valueOf(a.getType().name()),
                a.getMin(),
                a.getMax(),
                a.getLength()
        );
    }
}