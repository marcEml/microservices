package com.example.datasetManagerService.service.preset;

import com.example.datasetManagerService.model.ProjectModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PresetService {

    private final List<EntityPreset> presets;

    public List<String> availablePresets() {
        return presets.stream().map(EntityPreset::getName).toList();
    }

    public void applyPreset(ProjectModel project, String preset) {
        // chercher un preset enregistré
        presets.stream()
                .filter(p -> p.getName().equalsIgnoreCase(preset))
                .findFirst()
                .ifPresent(p -> p.apply(project));
    }
}
