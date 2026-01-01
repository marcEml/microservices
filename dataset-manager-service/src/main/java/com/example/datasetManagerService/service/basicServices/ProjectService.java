package com.example.datasetManagerService.service.basicServices;

import com.example.datasetManagerService.exception.ProjectNotFoundException;
import com.example.datasetManagerService.model.ProjectModel;
import com.example.datasetManagerService.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService implements IProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public ProjectModel saveProject(ProjectModel project) {
        return projectRepository.save(project);
    }

    @Override
    public List<ProjectModel> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public ProjectModel getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }


}