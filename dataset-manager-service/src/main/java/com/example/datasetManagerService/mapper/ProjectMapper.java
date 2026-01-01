package com.example.datasetManagerService.mapper;

import com.example.datasetManagerService.dto.Project.ProjectResponseDto;
import com.example.datasetManagerService.model.ProjectModel;

import java.util.stream.Collectors;

public class ProjectMapper {

    public static ProjectResponseDto toDto(ProjectModel project) {
        if (project == null) return null;

        ProjectResponseDto dto = new ProjectResponseDto();
        dto.setId(project.getProject_id());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());

        if (project.getEntities() != null) {
            dto.setEntities(
                    project.getEntities().stream()
                            .map(EntityMapper::toDto)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }
}
