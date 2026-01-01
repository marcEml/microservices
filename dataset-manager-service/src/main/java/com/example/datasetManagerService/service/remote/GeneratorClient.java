package com.example.datasetManagerService.service.remote;

import com.example.datasetManagerService.client.dto.DatasetRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "GENERATOR-SERVICE", url = "http://localhost:8082")
public interface GeneratorClient {

    @PostMapping(value = "/api/generate", consumes = "application/json")
    byte[] generate(@RequestBody DatasetRequest request);
}