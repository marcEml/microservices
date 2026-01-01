package com.example.datasetManagerService.mapper;

import com.example.datasetManagerService.dto.Entity.EntityResponseDto;
import com.example.datasetManagerService.model.EntityModel;

import java.util.List;
import java.util.stream.Collectors;

public class EntityMapper {

    public static EntityResponseDto toDto(EntityModel entity) {
        if (entity == null) return null;

        EntityResponseDto dto = new EntityResponseDto();
        dto.setId(entity.getEntity_id());
        dto.setName(entity.getName());

        if (entity.getProject() != null)
            dto.setProjectId(entity.getProject().getProject_id());

        if (entity.getParentEntity() != null)
            dto.setParentId(entity.getParentEntity().getEntity_id());

        // mapping récursif des enfants
        if (entity.getSubEntities() != null) {
            dto.setSubEntities(
                    entity.getSubEntities()
                            .stream()
                            .map(EntityMapper::toDto)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }


    public static List<EntityResponseDto> toDtoList(List<EntityModel> entities) {
        return entities.stream()
                .map(EntityMapper::toDto)
                .collect(Collectors.toList());
    }
}
