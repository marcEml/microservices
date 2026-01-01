package com.example.datasetManagerService.controller.MVC;

import com.example.datasetManagerService.client.dto.generator.DatasetDefinitionDTO;
import com.example.datasetManagerService.model.ProjectModel;
import com.example.datasetManagerService.service.basicServices.IProjectService;
import com.example.datasetManagerService.service.preset.PresetService;
import com.example.datasetManagerService.service.remote.DatasetDefinitionMapper;
import com.example.datasetManagerService.service.remote.RemoteGenerationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final IProjectService projectService;
    private final RemoteGenerationService remoteGenerationService; // ✅ instead of DatasetGeneratorService
    private final PresetService presetService;
    private final DatasetDefinitionMapper datasetDefinitionMapper;

    @GetMapping
    public String listProjects(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("project", new ProjectModel());
        return "projects/listProjects";
    }

    @PostMapping("/saveProject")
    public String saveProject(@ModelAttribute ProjectModel projectModel) {
        projectService.saveProject(projectModel);
        return "redirect:/projects";
    }

    @GetMapping("/edit/{id}")
    public String editProject(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectService.getProjectById(id));
        model.addAttribute("projects", projectService.getAllProjects());
        return "projects/listProjects";
    }

    @GetMapping("/delete/{id}")
    public String deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return "redirect:/projects";
    }

    @GetMapping("/preview/{id}")
    public String previewProject(
            @PathVariable Long id,
            @RequestParam(defaultValue = "5") int count,
            Model model
    ) throws Exception {

        ProjectModel project = projectService.getProjectById(id);

        // ✅ map Project → DatasetDefinitionDTO
        DatasetDefinitionDTO definition = datasetDefinitionMapper.fromProject(project);

        // ✅ call generator
        byte[] jsonBytes = remoteGenerationService.generate(definition, count, "json");
        String json = new String(jsonBytes, StandardCharsets.UTF_8);

        ObjectMapper mapper = new ObjectMapper();
        Object obj = mapper.readValue(json, Object.class);
        String pretty = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);

        model.addAttribute("project", project);
        model.addAttribute("jsonPreview", pretty);

        return "projects/preview";
    }

    @PostMapping("/presets/apply")
    public String applyPreset(
            @RequestParam Long projectId,
            @RequestParam String presetName
    ) {
        ProjectModel project = projectService.getProjectById(projectId);
        presetService.applyPreset(project, presetName);
        return "redirect:/projects";
    }

    @ModelAttribute("availablePresets")
    public List<String> populatePresets() {
        return presetService.availablePresets();
    }
}