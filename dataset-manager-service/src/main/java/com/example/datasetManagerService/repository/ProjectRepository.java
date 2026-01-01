package com.example.datasetManagerService.repository;

import com.example.datasetManagerService.model.ProjectModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectModel,Long>{
}
