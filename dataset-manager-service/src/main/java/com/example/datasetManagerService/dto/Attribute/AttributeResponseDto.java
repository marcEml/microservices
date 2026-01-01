package com.example.datasetManagerService.dto.Attribute;

import lombok.Data;

@Data
public class AttributeResponseDto {
    private Long id;
    private String name;
    private String type = "String";
    private Long entityId;
}
