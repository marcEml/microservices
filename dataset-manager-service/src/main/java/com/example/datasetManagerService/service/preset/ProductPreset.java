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
public class ProductPreset implements EntityPreset {

    private final IEntityService entityService;
    private final IAttributeService attributeService;

    @Override
    public String getName() {
        return "Product";
    }

    @Override
    public void apply(ProjectModel project) {
        // 1️⃣ Créer l'entité Product
        EntityModel product = EntityModel.builder()
                .name("Product")
                .project(project)
                .build();
        entityService.saveEntity(product);

        // 2️⃣ Ajouter les attributs
        attributeService.saveAttribute(AttributeModel.builder()
                .name("name")
                .type(AttributeModel.AttributeType.STRING)
                .length(50)
                .entityModel(product)
                .build());

        attributeService.saveAttribute(AttributeModel.builder()
                .name("price")
                .type(AttributeModel.AttributeType.DOUBLE)
                .min(1)
                .max(1000)
                .entityModel(product)
                .build());

        attributeService.saveAttribute(AttributeModel.builder()
                .name("inStock")
                .type(AttributeModel.AttributeType.BOOLEAN)
                .entityModel(product)
                .build());
    }
}
