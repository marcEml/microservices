package com.example.datasetManagerService.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProjectModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long project_id;

    private String name;
    private String description;

    // @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    // @ToString.Exclude
    // private List<EntityModel> entities;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<EntityModel> entities;
}
