package com.example.datasetManagerService.service.preset;

import com.example.datasetManagerService.model.ProjectModel;

public interface EntityPreset {
    String getName();
    void apply(ProjectModel project);
}