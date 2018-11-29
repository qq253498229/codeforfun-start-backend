package com.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author wangbin
 */
@RestController
public class GenerateController {
    private GenerateService generateService;

    public GenerateController(GenerateService generateService) {
        this.generateService = generateService;
    }

    /**
     * 获取服务列表
     */
    @GetMapping("/")
    public ResponseEntity serviceList() throws IOException {
        File file = ResourceUtils.getFile("classpath:static/services.json");
        ObjectMapper om = new ObjectMapper();
        List<Services> services = om.readValue(file, new TypeReference<List<Services>>() {
        });
        return ok(services);
    }

    @PostMapping("/generate")
    public ResponseEntity generate(@RequestBody List<Services> services) throws IOException {
        File file = generateService.generate(services);
        Map<String, Object> map = new HashMap<>();
        map.put("name", file.getName());
        return ok().body(map);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity download(@PathVariable String fileName) throws FileNotFoundException {
        File file = generateService.download(fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        return ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
