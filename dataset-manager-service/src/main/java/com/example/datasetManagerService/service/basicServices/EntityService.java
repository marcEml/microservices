package com.example.datasetManagerService.service.basicServices;

import com.example.datasetManagerService.model.EntityModel;
import com.example.datasetManagerService.repository.EntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EntityService implements IEntityService {

    private final EntityRepository entityRepository;

    @Override
    public EntityModel saveEntity(EntityModel entity) {
        entityRepository.save(entity);
        return entity;
    }

    @Override
    public List<EntityModel> getAllEntities() {
        return entityRepository.findAll();
    }

    @Override
    public EntityModel getEntityById(Long id) {
        return entityRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found"));
    }

    @Override
    public List<EntityModel> getSubEntities(Long parentId) {
        EntityModel parent = entityRepository.findById(parentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found"));

        return parent.getSubEntities();
    }

    @Override
    public List<EntityModel> findAll() {
        return entityRepository.findAll();
    }


    @Override
    public void deleteEntity(Long id) {
        entityRepository.deleteById(id);
    }



}