package com.example.datasetManagerService.controller.MVC;

import com.example.datasetManagerService.model.EntityModel;
import com.example.datasetManagerService.model.ProjectModel;
import com.example.datasetManagerService.repository.ProjectRepository;
import com.example.datasetManagerService.service.basicServices.IEntityService;
import com.example.datasetManagerService.service.basicServices.IProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/entities")
public class EntityController {

    private final IEntityService entityService;
    private final IProjectService projectService;
    private final ProjectRepository projectRepository;

    @GetMapping
    public String listEntities(Model model) {
        model.addAttribute("entities", entityService.getAllEntities());
        model.addAttribute("entity", new EntityModel());
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("allEntities", entityService.getAllEntities());
        return "entities/listEntities";
    }

    @PostMapping("/saveEntity")
    public String saveEntity(@ModelAttribute EntityModel entityModel) {


        Long projectId = entityModel.getProject().getProject_id();
        ProjectModel project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        entityModel.setProject(project);

        if (entityModel.getParentEntity() != null &&
                entityModel.getParentEntity().getEntity_id() != null) {

            EntityModel parent = entityService.getEntityById(
                    entityModel.getParentEntity().getEntity_id());
            entityModel.setParentEntity(parent);
        } else {
            entityModel.setParentEntity(null);
        }

        entityService.saveEntity(entityModel);

        return "redirect:/entities";
    }

    @GetMapping("/edit/{id}")
    public String editEntity(@PathVariable Long id, Model model) {
        EntityModel entity = entityService.getEntityById(id);

        model.addAttribute("entity", entity);
        model.addAttribute("entities", entityService.getAllEntities());
        model.addAttribute("allEntities", entityService.getAllEntities());
        model.addAttribute("projects", projectService.getAllProjects());

        return "entities/listEntities";
    }

    @GetMapping("/delete/{id}")
    public String deleteEntity(@PathVariable Long id) {
        entityService.deleteEntity(id);
        return "redirect:/entities";
    }

}
