package com.example.datasetManagerService.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Dataset {

    private String projectName;
    private List<Map<String, Object>> records = new ArrayList<>();

    public Dataset(String projectName, List<Map<String, Object>> records) {
        this.projectName = projectName;
        this.records = records;
    }

    public Dataset(String projectName) {
        this.projectName = projectName;
        this.records = new ArrayList<>();
    }

    public void addRecord(Map<String, Object> record) {
        this.records.add(record);
    }
}
