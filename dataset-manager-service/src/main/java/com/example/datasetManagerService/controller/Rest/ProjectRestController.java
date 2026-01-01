package com.example.datasetManagerService.controller.Rest;

import com.example.datasetManagerService.dto.Project.ProjectCreateDTO;
import com.example.datasetManagerService.dto.Project.ProjectResponseDto;
import com.example.datasetManagerService.mapper.ProjectMapper;
import com.example.datasetManagerService.model.ProjectModel;
import com.example.datasetManagerService.service.basicServices.IProjectService;
import com.example.datasetManagerService.service.preset.PresetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectRestController {

    private final IProjectService projectService;
    private final PresetService presetService;

    @Operation(summary = "Récupérer tous les projets")
    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getAll() {
        List<ProjectModel> projects = projectService.getAllProjects();

        List<ProjectResponseDto> result = projects.stream()
                .map(ProjectMapper::toDto)
                .toList();

        return ResponseEntity.ok(result);
    }


    @Operation(summary = "Récupérer un projet par son ID")
    @GetMapping("/{id}")
    public ProjectResponseDto get(@PathVariable Long id) {
        ProjectModel project = projectService.getProjectById(id);
        if (project == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }
        return ProjectMapper.toDto(project);
    }

    @Operation(summary = "Créer un projet")
    @PostMapping
    public ProjectResponseDto createProject(@RequestBody ProjectCreateDTO dto) {
        ProjectModel project = ProjectModel.builder()
                .name(dto.name())
                .description(dto.description())
                .build();
        project = projectService.saveProject(project);
        return ProjectMapper.toDto(project);
    }

    @Operation(summary = "Supprimer un projet par son ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        ProjectModel project = projectService.getProjectById(id);
        if (project == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }
        projectService.deleteProject(id);
    }

    @Operation(summary = "Appliquer un preset sur un projet (Population Française, Product")
    @PostMapping("/presets/apply")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void applyPreset(
            @Parameter(description = "ID du projet") @RequestParam Long projectId,
            @Parameter(description = "Nom du preset à appliquer") @RequestParam String presetName
    ) {
        ProjectModel project = projectService.getProjectById(projectId);
        if (project == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }
        presetService.applyPreset(project, presetName);
    }
}

