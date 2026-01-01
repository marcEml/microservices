package com.example.datasetManagerService.repository;

import com.example.datasetManagerService.model.EntityModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityRepository extends JpaRepository<EntityModel,Long> {
}
