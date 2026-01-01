package com.example.datasetManagerService.service.basicServices;

import com.example.datasetManagerService.model.AttributeModel;
import com.example.datasetManagerService.repository.AttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AttributeService implements IAttributeService {

    private final AttributeRepository attributeRepository;

    @Override
    public AttributeModel saveAttribute(AttributeModel attributeModel){
        return attributeRepository.save(attributeModel);
    }

    @Override
    public List<AttributeModel> getAllAttributes(){
        return attributeRepository.findAll();
    }

    @Override
    public AttributeModel getAttributeById(Long id) {
        return attributeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attribute not found"));
    }

    @Override
    public void deleteAttribute(Long id) {
        attributeRepository.deleteById(id);
    }

    @Override
    public List<AttributeModel> findAll() {
        return attributeRepository.findAll();
    }



}