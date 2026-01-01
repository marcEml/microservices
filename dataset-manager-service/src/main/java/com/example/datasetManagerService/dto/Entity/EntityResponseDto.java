package com.example.datasetManagerService.dto.Entity;

import lombok.Data;

import java.util.List;

@Data
public class EntityResponseDto {
    private Long id;
    private String name;
    private Long projectId;
    private Long parentId;

    private List<EntityResponseDto> subEntities;
}
