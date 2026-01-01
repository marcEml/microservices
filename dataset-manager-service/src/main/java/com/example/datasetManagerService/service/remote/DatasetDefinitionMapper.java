package com.example.datasetManagerService.service.remote;

import com.example.datasetManagerService.client.dto.generator.AttributeDefinitionDTO;
import com.example.datasetManagerService.client.dto.generator.DatasetDefinitionDTO;
import com.example.datasetManagerService.client.dto.generator.EntityDefinitionDTO;
import com.example.datasetManagerService.model.AttributeModel;
import com.example.datasetManagerService.model.EntityModel;
import com.example.datasetManagerService.model.ProjectModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatasetDefinitionMapper {

    public DatasetDefinitionDTO fromProject(ProjectModel project) {
        List<EntityDefinitionDTO> rootEntities =
                (project.getEntities() == null)
                        ? List.of()
                        : project.getEntities().stream()
                        .filter(e -> e.getParentEntity() == null)
                        .map(this::fromEntity)
                        .toList();

        return new DatasetDefinitionDTO(project.getName(), rootEntities);
    }

    private EntityDefinitionDTO fromEntity(EntityModel entity) {

        List<AttributeDefinitionDTO> attrs = (entity.getAttributes() == null)
                ? List.of()
                : entity.getAttributes().stream()
                .map(this::fromAttribute)
                .toList();

        List<EntityDefinitionDTO> subs = (entity.getSubEntities() == null)
                ? List.of()
                : entity.getSubEntities().stream()
                .map(this::fromEntity)
                .toList();

        return new EntityDefinitionDTO(entity.getName(), attrs, subs);
    }

    private AttributeDefinitionDTO fromAttribute(AttributeModel a) {

        // ⚠️ Il faut que AttributeDefinitionDTO.AttributeType corresponde à celui du generator-service.
        // Ici je mappe en utilisant le nom de l'enum.
        AttributeDefinitionDTO.AttributeType type =
                AttributeDefinitionDTO.AttributeType.valueOf(a.getType().name());

        return new AttributeDefinitionDTO(
                a.getName(),
                type,
                a.getMin(),
                a.getMax(),
                a.getLength()
        );
    }
}