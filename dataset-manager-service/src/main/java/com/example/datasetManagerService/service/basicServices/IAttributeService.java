package com.example.datasetManagerService.service.basicServices;

import com.example.datasetManagerService.model.AttributeModel;
import java.util.List;

public interface IAttributeService {
    AttributeModel saveAttribute(AttributeModel attributeModel);
    List <AttributeModel> getAllAttributes();
    AttributeModel getAttributeById(Long id);
    void deleteAttribute(Long id);

    Object findAll();
}