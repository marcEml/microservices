package com.example.datasetManagerService.controller.Rest;

import com.example.datasetManagerService.dto.Entity.EntityCreateDTO;
import com.example.datasetManagerService.dto.Entity.EntityResponseDto;
import com.example.datasetManagerService.mapper.EntityMapper;
import com.example.datasetManagerService.model.EntityModel;
import com.example.datasetManagerService.model.ProjectModel;
import com.example.datasetManagerService.service.basicServices.IEntityService;
import com.example.datasetManagerService.service.basicServices.IProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/entities")
public class EntityRestController {

    private final IEntityService entityService;
    private final IProjectService projectService;

    // ---------------- GET SUBENTITIES ----------------
    @Operation(summary = "Récupérer les sous-entities d'une entité")
    @GetMapping("/{id}/subentities")
    public List<EntityResponseDto> getSubEntities(
            @Parameter(description = "ID de l'entité parent") @PathVariable Long id) {
        EntityModel parent = entityService.getEntityById(id);
        if (parent == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent entity not found");
        }
        return EntityMapper.toDtoList(parent.getSubEntities());
    }

    // ---------------- CREATE ENTITY ----------------
    @Operation(summary = "Créer une entité")
    @PostMapping
    public EntityResponseDto createEntity(@RequestBody EntityCreateDTO dto) {
        ProjectModel project = projectService.getProjectById(dto.projectId());
        if (project == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project not found");
        }

        EntityModel parentEntity = null;
        if (dto.parentEntityId() != null && dto.parentEntityId() > 0) {
            parentEntity = entityService.getEntityById(dto.parentEntityId());
            if (parentEntity == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parent entity not found");
            }
        }

        EntityModel entity = EntityModel.builder()
                .name(dto.name())
                .project(project)
                .parentEntity(parentEntity)
                .build();

        return EntityMapper.toDto(entityService.saveEntity(entity));
    }

    // ---------------- DELETE ENTITY ----------------
    @Operation(summary = "Supprimer une entité")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEntity(
            @Parameter(description = "ID de l'entité à supprimer") @PathVariable Long id) {
        EntityModel entity = entityService.getEntityById(id);
        if (entity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found");
        }
        entityService.deleteEntity(id);
    }

    // ---------------- GET ALL ENTITIES (optionnel) ----------------
    @Operation(summary = "Récupérer toutes les entités")
    @GetMapping
    public List<EntityResponseDto> getAllEntities() {
        List<EntityModel> entities = entityService.getAllEntities();
        return EntityMapper.toDtoList(entities);
    }

    @GetMapping("/{id}")
    public EntityModel getOne(@PathVariable Long id) {
        return entityService.getEntityById(id);
    }
}
