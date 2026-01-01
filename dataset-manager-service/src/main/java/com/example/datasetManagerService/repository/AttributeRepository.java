package com.example.datasetManagerService.repository;

import com.example.datasetManagerService.model.AttributeModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeRepository extends JpaRepository<AttributeModel,Long> {
}
