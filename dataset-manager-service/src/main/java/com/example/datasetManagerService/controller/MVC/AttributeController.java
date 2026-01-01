package com.example.datasetManagerService.controller.MVC;

import com.example.datasetManagerService.model.AttributeModel;
import com.example.datasetManagerService.model.EntityModel;
import com.example.datasetManagerService.repository.EntityRepository;
import com.example.datasetManagerService.service.basicServices.IAttributeService;
import com.example.datasetManagerService.service.basicServices.IEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/attributes")
public class AttributeController {

    private final IAttributeService attributeService;
    private final IEntityService entityService;
    private final EntityRepository entityRepository;

    @GetMapping
    public String attributesPage(Model model) {
        model.addAttribute("attribute", new AttributeModel());
        model.addAttribute("attributes", attributeService.findAll());
        model.addAttribute("entities", entityService.findAll());
        model.addAttribute("types", AttributeModel.AttributeType.values()); // 👈 IMPORTANT
        return "attributes/listAttributes";
    }

    @PostMapping("/saveAttribute")
    public String saveAttribute(@ModelAttribute AttributeModel attribute) {
        Long entityId = attribute.getEntityModel().getEntity_id();
        EntityModel entity = entityService.getEntityById(entityId);
        attribute.setEntityModel(entity);
        attributeService.saveAttribute(attribute);
        return "redirect:/attributes";
    }



    @GetMapping("/edit/{id}")
    public String editAttribute(@PathVariable Long id, Model model) {
        AttributeModel attribute = attributeService.getAttributeById(id);

        model.addAttribute("attribute", attribute);
        model.addAttribute("attributes", attributeService.getAllAttributes());
        model.addAttribute("entities", entityService.getAllEntities());

        return "attributes/listAttributes";

    }

    @GetMapping("/delete/{id}")
    public String deleteAttribute(@PathVariable Long id) {
        attributeService.deleteAttribute(id);
        return "redirect:/attributes";
    }




}
