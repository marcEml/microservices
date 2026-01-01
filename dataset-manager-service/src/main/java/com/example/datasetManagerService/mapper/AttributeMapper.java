package com.example.datasetManagerService.mapper;

import com.example.datasetManagerService.dto.Attribute.AttributeResponseDto;
import com.example.datasetManagerService.model.AttributeModel;

public class AttributeMapper {

    public static AttributeResponseDto toDto(AttributeModel a) {
        if (a == null) return null;

        AttributeResponseDto dto = new AttributeResponseDto();
        dto.setId(a.getAttribute_id());
        dto.setName(a.getName());
        dto.setType(String.valueOf(a.getType()));

        if (a.getEntityModel() != null)
            dto.setEntityId(a.getEntityModel().getEntity_id());

        return dto;
    }
}
