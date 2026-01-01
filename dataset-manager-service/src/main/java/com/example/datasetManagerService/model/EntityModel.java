package com.example.datasetManagerService.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long entity_id;

    private String name;

    // @ManyToOne
    // @JoinColumn(name = "project_id")
    // private ProjectModel project;
    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonBackReference
    private ProjectModel project;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private EntityModel parentEntity;

    @OneToMany(mappedBy = "parentEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EntityModel> subEntities = new ArrayList<>();

    // @OneToMany(mappedBy = "entityModel", cascade = CascadeType.ALL, orphanRemoval = true)
    // @Builder.Default
    // private List<AttributeModel> attributes = new ArrayList<>();
    @OneToMany(mappedBy = "entityModel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("entity-attribute")
    @Builder.Default
    private List<AttributeModel> attributes = new ArrayList<>();
}
