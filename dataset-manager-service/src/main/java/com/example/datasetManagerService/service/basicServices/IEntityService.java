package com.example.datasetManagerService.service.basicServices;

import com.example.datasetManagerService.model.EntityModel;

import java.util.List;

public interface IEntityService {
    EntityModel saveEntity(EntityModel entity);
    List<EntityModel> getAllEntities();

    EntityModel getEntityById(Long id);
    void deleteEntity(Long id);
    List<EntityModel> getSubEntities(Long id);

    Object findAll();
}