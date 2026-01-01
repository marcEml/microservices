package com.example.datasetManagerService.controller.Rest;

import com.example.datasetManagerService.dto.Attribute.AttributeCreateDTO;
import com.example.datasetManagerService.dto.Attribute.AttributeResponseDto;
import com.example.datasetManagerService.mapper.AttributeMapper;
import com.example.datasetManagerService.model.AttributeModel;
import com.example.datasetManagerService.model.EntityModel;
import com.example.datasetManagerService.service.basicServices.IAttributeService;
import com.example.datasetManagerService.service.basicServices.IEntityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/attributes")
@RequiredArgsConstructor
public class AttributeRestController {

    private final IAttributeService attributeService;
    private final IEntityService entityService;

    // ---------------- GET ALL ATTRIBUTES ----------------
    @Operation(summary = "Récupérer tous les attributs")
    @GetMapping
    public List<AttributeResponseDto> getAll() {
        return attributeService.getAllAttributes()
                .stream()
                .map(AttributeMapper::toDto)
                .toList();
    }

    // ---------------- GET ATTRIBUTE BY ID ----------------
    @Operation(summary = "Récupérer un attribut par son ID")
    @GetMapping("/{id}")
    public AttributeResponseDto get(
            @Parameter(description = "ID de l'attribut") @PathVariable Long id) {
        AttributeModel attribute = attributeService.getAttributeById(id);
        if (attribute == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attribute not found");
        }
        return AttributeMapper.toDto(attribute);
    }

    // ---------------- CREATE ATTRIBUTE ----------------
    @Operation(summary = "Créer un nouvel attribut")
    @PostMapping
    public AttributeResponseDto createAttribute(
            @Parameter(description = "Données de création de l'attribut") @RequestBody AttributeCreateDTO dto) {

        // Vérifier que l'entité associée existe
        EntityModel entity = entityService.getEntityById(dto.entityId());
        if (entity == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entity not found");
        }

        // Créer l'attribut avec conversion sécurisée de l'enum
        AttributeModel attribute = AttributeModel.builder()
                .name(dto.name())
                .type(AttributeModel.AttributeType.fromString(String.valueOf(dto.type())))
                .entityModel(entity)
                .min(dto.min())
                .max(dto.max())
                .length(dto.length())
                .build();

        return AttributeMapper.toDto(attributeService.saveAttribute(attribute));
    }

    // ---------------- DELETE ATTRIBUTE ----------------
    @Operation(summary = "Supprimer un attribut par son ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "ID de l'attribut") @PathVariable Long id) {
        AttributeModel attribute = attributeService.getAttributeById(id);
        if (attribute == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attribute not found");
        }
        attributeService.deleteAttribute(id);
    }
}
