package com.example.datasetManagerService.dto.Project;

import com.example.datasetManagerService.dto.Entity.EntityResponseDto;
import lombok.Data;
import java.util.List;

@Data
public class ProjectResponseDto {
    private Long id;
    private String name;
    private String description;

    private List<EntityResponseDto> entities; // hiérarchie complète
}
