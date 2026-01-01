package com.example.datasetManagerService.service.preset;

import com.example.datasetManagerService.model.AttributeModel;
import com.example.datasetManagerService.model.EntityModel;
import com.example.datasetManagerService.model.ProjectModel;
import com.example.datasetManagerService.service.basicServices.IAttributeService;
import com.example.datasetManagerService.service.basicServices.IEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FrenchPopulationPreset implements EntityPreset {

    private final IEntityService entityService;
    private final IAttributeService attributeService;

    @Override
    public String getName() {
        return "Population Française";
    }

    @Override
    public void apply(ProjectModel project) {
        EntityModel person = EntityModel.builder()
                .name("Personne")
                .project(project)
                .build();
        entityService.saveEntity(person);

        attributeService.saveAttribute(AttributeModel.builder().name("nom").type(AttributeModel.AttributeType.STRING).length(50).entityModel(person).build());
        attributeService.saveAttribute(AttributeModel.builder().name("prenom").type(AttributeModel.AttributeType.STRING).length(50).entityModel(person).build());
        attributeService.saveAttribute(AttributeModel.builder().name("age").type(AttributeModel.AttributeType.INTEGER).min(0).max(100).entityModel(person).build());
        attributeService.saveAttribute(AttributeModel.builder().name("ville").type(AttributeModel.AttributeType.STRING).length(50).entityModel(person).build());
    }
}
