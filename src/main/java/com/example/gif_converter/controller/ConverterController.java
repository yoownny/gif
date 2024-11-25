package com.example.gif_converter.controller;

import com.example.gif_converter.service.VideoService;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/converter")
public class ConverterController {
    private final VideoService videoService;

    public ConverterController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping(value = "/convert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> convertVideo(@RequestParam("file") MultipartFile file) {
        try {
            String outputFileName = videoService.convertVideo(file);
            return ResponseEntity.ok(outputFileName);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("변환 실패: " + e.getMessage());
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
        Path filePath = Paths.get(System.getProperty("java.io.tmpdir"), fileName);
        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_GIF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + "\"")
                .body(resource);
    }
}