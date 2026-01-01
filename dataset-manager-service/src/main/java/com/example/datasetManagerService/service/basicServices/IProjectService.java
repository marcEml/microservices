package com.example.datasetManagerService.service.basicServices;

import com.example.datasetManagerService.model.ProjectModel;

import java.util.List;

public interface IProjectService {
    ProjectModel saveProject(ProjectModel project);
    List<ProjectModel> getAllProjects();
    ProjectModel getProjectById(Long id);
    void deleteProject(Long id);

}